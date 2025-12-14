package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.annotation.TripAccessValidate;
import com.bread.traveler.annotation.TripVisibilityValidate;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.TripLogsDto;
import com.bread.traveler.entity.TripLogs;
import com.bread.traveler.entity.Users;
import com.bread.traveler.enums.MemberRole;
import com.bread.traveler.service.TripLogsService;
import com.bread.traveler.mapper.TripLogsMapper;
import com.bread.traveler.service.UsersService;
import com.bread.traveler.utils.AliyunOssUtils;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author huang
 * @description 针对表【trip_logs】的数据库操作Service实现
 * @createDate 2025-11-14 12:09:43
 */
@Service
@Slf4j
public class TripLogsServiceImpl extends ServiceImpl<TripLogsMapper, TripLogs> implements TripLogsService {

    @Autowired
    private AliyunOssUtils aliyunOssUtils;
    @Autowired
    private UsersService usersService;

    private static final ExecutorService UPLOAD_EXECUTOR = new ThreadPoolExecutor(
            12, 20,
            5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(200)
    );

    @Override
    @TripAccessValidate(lowestRole = MemberRole.VIEWER)
    public String createLog(UUID userId, UUID tripId, String content, List<MultipartFile> imgFiles, @Nullable Boolean isPublic) {
        log.info("Create NOTE noteLog: user {}, trip {}, content {}, isPublic {}", userId, tripId, content, isPublic);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.isTrue(!StrUtil.isBlank(content), Constant.TRIP_LOG_CONTENT_NOT_EMPTY);
        if (isPublic == null) {
            // 默认为非公开
            isPublic = false;
        }
        // 将imgs上传至aliyun oss
        List<String> urls = new ArrayList<>();
        StringBuilder failedImgs = new StringBuilder();
        if (imgFiles != null && !imgFiles.isEmpty()) {
            List<CompletableFuture<String>> futures = imgFiles.stream()
                    .map(file -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return aliyunOssUtils.uploadFile(file);
                        } catch (ClientException e) {
                            log.error("Upload file failed: {}", file.getOriginalFilename(), e);
                            return "FAILED:" + file.getOriginalFilename();
                        }
                    }, UPLOAD_EXECUTOR))
                    .toList();
            for (CompletableFuture<String> future : futures) {
                try {
                    String url = future.get(30, TimeUnit.SECONDS); // 设置超时时间
                    if (url != null) {
                        if (url.startsWith("FAILED:")) {
                            failedImgs.append(url.substring(7)).append("\n");
                        } else {
                            urls.add(url);
                        }
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    log.error("Get upload result failed", e);
                }
            }
        }
        String urlJson = urls.isEmpty() ? null : JSONUtil.toJsonStr(urls);
        // 创建日志
        TripLogs noteLog = new TripLogs();
        noteLog.setLogId(UUID.randomUUID());
        noteLog.setUserId(userId);
        noteLog.setTripId(tripId);
        noteLog.setContent(content);
        noteLog.setImgs(urlJson);
        noteLog.setIsPublic(isPublic);
        noteLog.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        if (save(noteLog)) {
            log.info("Create NOTE noteLog success: {}", noteLog.getLogId());
            String failedImgsStr = failedImgs.toString();
            return StrUtil.isBlank(failedImgsStr) ? "SUCCESS" : "上传失败图片：\n" + failedImgsStr;
        }
        log.error("Create NOTE noteLog failed: {}", noteLog.getLogId());
        throw new RuntimeException(Constant.TRIP_LOG_CREATE_FAILED);
    }

    @Override
    public boolean deleteLog(UUID userId, UUID logId) {
        log.info("Delete log: user {}, log {}", userId, logId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(logId, "logId cannot be null");
        // 删除日志
        boolean remove = lambdaUpdate().eq(TripLogs::getLogId, logId).eq(TripLogs::getUserId, userId).remove();
        Assert.isTrue(remove, Constant.TRIP_LOG_NOT_EXISTS);
        return remove;
    }

    @Override
    public boolean changeLogVisibility(UUID userId, UUID logId, Boolean isPublic) {
        log.info("Change log visibility: user {}, log {}, isPublic {}", userId, logId, isPublic);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(logId, "logId cannot be null");
        Assert.notNull(isPublic, "isPublic cannot be null");
        // 修改日志可见性
        boolean update = lambdaUpdate().eq(TripLogs::getLogId, logId).eq(TripLogs::getUserId, userId).set(TripLogs::getIsPublic, isPublic).update();
        Assert.isTrue(update, Constant.TRIP_LOG_NOT_EXISTS);
        return update;
    }

    @Override
    public List<TripLogs> getLogsOfUserByTripId(UUID userId, UUID tripId) {
        log.info("Get logs by trip id: user {}, trip {}", userId, tripId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        // 获取日志
        List<TripLogs> logs = lambdaQuery().eq(TripLogs::getTripId, tripId).eq(TripLogs::getUserId, userId).list();
        if (logs == null || logs.isEmpty()) {
            log.info("Get EMPTY logs by trip id: user {}, trip {}, no logs", userId, tripId);
            return Collections.emptyList();
        }
        // 按照创建时间倒序排列
        logs.sort(Comparator.comparing(TripLogs::getCreatedAt).reversed());
        return logs;
    }

    @Override
    @TripVisibilityValidate
    public List<TripLogsDto> getPublicLogsByTripId(UUID userId, UUID tripId) {
        log.info("Get public logs by trip id: user {}, trip {}", userId, tripId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        // 获取当前trip的全部日志
        List<TripLogs> allLogs = lambdaQuery().eq(TripLogs::getTripId, tripId).list();
        // 筛选出公开日志，并按照创建用户id进行分类
        Map<UUID, List<TripLogs>> userIdToLogs = allLogs.stream()
                .filter(TripLogs::getIsPublic)
                .collect(Collectors.groupingBy(TripLogs::getUserId));
        if (userIdToLogs.isEmpty()){
            // 没有公开日志
            log.info("Get EMPTY public logs by trip id: trip {}", tripId);
            return Collections.emptyList();
        }
        // 获取userId对应的用户名
        Map<UUID, String> userIdToName = usersService.listByIds(userIdToLogs.keySet())
                .stream().collect(Collectors.toMap(Users::getUserId, Users::getUsername));
        // 转为TripLogsDto，填充用户名
        List<TripLogsDto> result = new ArrayList<>();
        userIdToLogs.forEach((key, value) -> {
            String username = userIdToName.get(key);
            List<TripLogsDto> dtos = value.stream().map(tripLog -> {
                TripLogsDto tripLogsDto = BeanUtil.copyProperties(tripLog, TripLogsDto.class);
                tripLogsDto.setUsername(username);
                return tripLogsDto;
            }).toList();
            result.addAll(dtos);
        });
        // 按照创建时间倒序排列
        result.sort(Comparator.comparing(TripLogsDto::getCreatedAt).reversed());
        return result;
    }
}




