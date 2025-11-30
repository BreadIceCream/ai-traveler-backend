package com.bread.traveler.service;

import com.bread.traveler.entity.TripLogs;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.enums.LogType;
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
     * 创建一条新的Note类型旅程日志
     * 只有旅程成员才能添加日志
     *
     * @param userId   当前用户ID
     * @param tripId   关联的旅程ID
     * @param content  内容 (如果是NOTE则是文本，如果是IMAGE/VIDEO则是URL)
     * @param isPublic 是否公开，默认为false
     * @return 创建成功的日志实体
     */
    TripLogs createNoteLog(UUID userId, UUID tripId, String content, @Nullable Boolean isPublic);

    /**
     * 创建一条新的IMAGE类型旅程日志,会将img保存至Aliyun oss
     * 只有旅程成员才能添加日志
     *
     * @param userId   当前用户ID
     * @param tripId   关联的旅程ID
     * @param imgFile  图片文件
     * @param isPublic 是否公开，默认为false
     * @return
     */
    TripLogs createImgLog(UUID userId, UUID tripId, MultipartFile imgFile, @Nullable Boolean isPublic);

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
     * 获取当前用户的某个旅程下指定类型的日志（相册模式/笔记模式）
     * 场景：用户只想看“相册”（LogType.IMAGE）或者只想看“日记”（LogType.NOTE）
     *
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @param type   日志类型
     * @return 筛选后的日志列表, 按创建时间倒序排列。如果没有日志则返回空列表
     */
    List<TripLogs> getLogsOfUserByTripIdAndType(UUID userId, UUID tripId, LogType type);

    /**
     * 获取指定旅程的公开日志，需要校验旅程trip对当前用户是否可见
     * 场景：旅程成员公开日志，其他成员可以查看
     *
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @return 公开日志列表，按创建时间倒序排序，如果没有公开日志则返回空列表
     */
    List<TripLogs> getPublicLogsByTripId(UUID userId, UUID tripId);
    
}
