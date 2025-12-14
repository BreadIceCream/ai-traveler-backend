package com.bread.traveler.service;

import com.bread.traveler.dto.TripLogsDto;
import com.bread.traveler.entity.TripLogs;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_logs】的数据库操作Service
* @createDate 2025-11-14 12:09:43
*/
public interface TripLogsService extends IService<TripLogs> {

    // 该接口的删改查方法都需要校验当前用户是否为Log的创建者

    /**
     * 创建日志
     * 只有旅程成员才能添加日志
     *
     * @param userId   当前用户ID
     * @param tripId   关联的旅程ID
     * @param content  文本内容
     * @param imgFiles 图片文件
     * @param isPublic 是否公开，默认为false
     * @return 创建的结果信息
     */
    String createLog(UUID userId, UUID tripId, String content, List<MultipartFile> imgFiles, @Nullable Boolean isPublic);

    /**
     * 删除指定的日志
     *
     * @param userId 当前用户ID
     * @param logId  日志ID
     * @return 是否删除成功
     */
    boolean deleteLog(UUID userId, UUID logId);

    /**
     * 修改日志的公开可见性
     * @param userId 当前用户ID
     * @param logId  日志ID
     * @param isPublic 是否公开
     * @return 是否修改成功
     */
    boolean changeLogVisibility(UUID userId, UUID logId, Boolean isPublic);

    /**
     * 获取当前用户的某个旅程下的所有日志（时间轴模式）
     *
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @return 日志列表 （按创建时间倒序排列）. 如果没有日志则返回空列表
     */
    List<TripLogs> getLogsOfUserByTripId(UUID userId, UUID tripId);

    /**
     * 获取指定旅程的公开日志，需要校验旅程trip对当前用户是否可见
     * 场景：旅程成员公开日志，其他成员可以查看
     *
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @return 公开日志列表，按创建时间倒序排序，如果没有公开日志则返回空列表
     */
    List<TripLogsDto> getPublicLogsByTripId(UUID userId, UUID tripId);
    
}
