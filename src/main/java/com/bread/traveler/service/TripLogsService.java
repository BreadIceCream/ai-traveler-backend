package com.bread.traveler.service;

import com.bread.traveler.entity.TripLogs;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.enums.LogType;
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
     * 需要校验当前用户是否对旅程有访问权限
     *
     * @param userId  当前用户ID
     * @param tripId  关联的旅程ID
     * @param content 内容 (如果是NOTE则是文本，如果是IMAGE/VIDEO则是URL)
     * @return 创建成功的日志实体
     */
    TripLogs createNoteLog(UUID userId, UUID tripId, String content);

    /**
     * 创建一条新的IMAGE类型旅程日志
     * 需要校验当前用户是否对旅程有访问权限
     * 会将img保存至Aliyun oss
     * @param userId 当前用户ID
     * @param tripId 关联的旅程ID
     * @param imgFile 图片文件
     * @return
     */
    TripLogs createImgLog(UUID userId, UUID tripId, MultipartFile imgFile);

    /**
     * 删除指定的日志
     *
     * @param userId 当前用户ID
     * @param logId  日志ID
     * @return 是否删除成功
     */
    boolean deleteLog(UUID userId, UUID logId);

    /**
     * 获取当前用户某个旅程下的所有日志（时间轴模式）
     * 通常用于“全部动态”页面
     *
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @return 日志列表 （按创建时间正序排列）. 如果没有日志则返回空列表
     */
    List<TripLogs> getLogsByTripId(UUID userId, UUID tripId);

    /**
     * 获取当前用户某个旅程下指定类型的日志（相册模式/笔记模式）
     * 场景：用户只想看“相册”（LogType.IMAGE）或者只想看“日记”（LogType.NOTE）
     *
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @param type   日志类型
     * @return 筛选后的日志列表, 按创建时间正序排列。如果没有日志则返回空列表
     */
    List<TripLogs> getLogsByTripIdAndType(UUID userId, UUID tripId, LogType type);
    
}
