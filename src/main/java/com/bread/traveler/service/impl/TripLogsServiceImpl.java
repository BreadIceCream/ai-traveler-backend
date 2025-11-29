package com.bread.traveler.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.annotation.TripVisibilityValidate;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.TripLogs;
import com.bread.traveler.enums.LogType;
import com.bread.traveler.service.TripLogsService;
import com.bread.traveler.mapper.TripLogsMapper;
import com.bread.traveler.utils.AliyunOssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_logs】的数据库操作Service实现
* @createDate 2025-11-14 12:09:43
*/
@Service
@Slf4j
public class TripLogsServiceImpl extends ServiceImpl<TripLogsMapper, TripLogs> implements TripLogsService{

    @Autowired
    private AliyunOssUtils aliyunOssUtils;

    @Override
    @TripVisibilityValidate
    public TripLogs createNoteLog(UUID userId, UUID tripId, String content) {
        log.info("Create NOTE noteLog: user {}, trip {}, content {}", userId, tripId, content);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.isTrue(!StrUtil.isBlank(content), Constant.TRIP_LOG_CONTENT_NOT_EMPTY);
        // 创建NOTE日志
        TripLogs noteLog = new TripLogs();
        noteLog.setLogId(UUID.randomUUID());
        noteLog.setUserId(userId);
        noteLog.setTripId(tripId);
        noteLog.setLogType(LogType.NOTE);
        noteLog.setContent(content);
        noteLog.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        if (save(noteLog)) {
            log.info("Create NOTE noteLog success: {}", noteLog.getLogId());
            return noteLog;
        }
        log.error("Create NOTE noteLog failed: {}", noteLog.getLogId());
        throw new RuntimeException(Constant.TRIP_LOG_CREATE_FAILED);
    }

    @Override
    @TripVisibilityValidate
    public TripLogs createImgLog(UUID userId, UUID tripId, MultipartFile imgFile) {
        log.info("Create IMAGE noteLog: user {}, trip {}, img {}", userId, tripId, imgFile);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(imgFile, "imgFile cannot be null");
        // 将img上传至aliyun oss
        String url = null;
        try {
             url = aliyunOssUtils.uploadFile(imgFile);
        } catch (ClientException e) {
            throw new RuntimeException(Constant.TRIP_LOG_UPLOAD_FILE_FAILED);
        }
        // 创建IMAGE日志
        TripLogs imgLog = new TripLogs();
        imgLog.setLogId(UUID.randomUUID());
        imgLog.setUserId(userId);
        imgLog.setTripId(tripId);
        imgLog.setLogType(LogType.IMAGE);
        imgLog.setContent(url);
        imgLog.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        if (save(imgLog)) {
            log.info("Create IMAGE noteLog success: {}", imgLog.getLogId());
            return imgLog;
        }
        log.error("Create IMAGE noteLog failed: {}", imgLog.getLogId());
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
    public List<TripLogs> getLogsByTripId(UUID userId, UUID tripId) {
        log.info("Get logs by trip id: user {}, trip {}", userId, tripId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        // 获取日志
        List<TripLogs> logs = lambdaQuery().eq(TripLogs::getUserId, userId).eq(TripLogs::getTripId, tripId).list();
        if (logs == null) {
            return Collections.emptyList();
        }
        // 按照创建时间正序排列
        logs.sort(Comparator.comparing(TripLogs::getCreatedAt));
        return logs;
    }

    @Override
    public List<TripLogs> getLogsByTripIdAndType(UUID userId, UUID tripId, LogType type) {
        log.info("Get logs by trip id and type: user {}, trip {}, type {}", userId, tripId, type);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(type, "type cannot be null");
        // 获取全部日志
        List<TripLogs> logs = lambdaQuery().eq(TripLogs::getUserId, userId).eq(TripLogs::getTripId, tripId).list();
        if (logs == null) {
            return Collections.emptyList();
        }
        // 过滤日志，只保留和type相同类型的。并按照创建时间正序排列
        return logs.stream()
                .filter(tripLog -> type.equals(tripLog.getLogType()))
                .sorted(Comparator.comparing(TripLogs::getCreatedAt))
                .toList();
    }
}




