package com.bread.traveler.service;

import com.bread.traveler.dto.TripExpenseCreateUpdateDto;
import com.bread.traveler.entity.TripExpenses;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.enums.ExpenseType;
import com.bread.traveler.exception.BusinessException;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
* @author huang
* @description 针对表【expenses】的数据库操作Service
* @createDate 2025-11-14 12:09:43
*/
public interface TripExpensesService extends IService<TripExpenses> {

    // 该接口的删改查方法都需要校验当前用户是否为Expense的创建者
    
    /**
     * 添加一笔新的支出, 默认支出时间是当前时间
     * 只有旅程成员才能添加支出
     * @param userId  当前用户ID
     * @param tripId  旅程ID
     * @param dto 创建支出参数
     * @return 创建成功后的支出记录（包含生成的ID和时间）
     * @throws BusinessException 如果tripId不存在或金额无效
     */
    TripExpenses addExpense(UUID userId, UUID tripId, TripExpenseCreateUpdateDto dto);

    /**
     * 批量添加支出记录，默认支出时间是当前时间
     * 只有旅程成员才能添加支出
     * 场景：用户一次性录入多笔消费，或者从第三方账单导入
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @param dtos   创建支出参数列表
     * @return 是否全部保存成功
     */
    boolean batchAddExpenses(UUID userId, UUID tripId, List<TripExpenseCreateUpdateDto> dtos);

    /**
     * 删除指定的支出记录
     *
     * @param userId 当前用户ID
     * @param expenseId 支出ID
     * @return 是否删除成功
     */
    boolean deleteExpense(UUID userId, UUID expenseId);

    /**
     * 更新指定的支出记录
     * @param userId 当前用户ID
     * @param expenseId 更新的支出ID
     * @param dto 更新参数
     * @return 更新后的最新信息
     */
    TripExpenses updateExpense(UUID userId, UUID expenseId, TripExpenseCreateUpdateDto dto);

    /**
     * 获取单笔支出详情
     *
     * @param userId 当前用户ID
     * @param expenseId 支出ID
     * @return 支出详情
     */
    TripExpenses getExpenseById(UUID userId, UUID expenseId);

    /**
     * 获取当前用户指定旅程下的支出记录，支持按类型过滤
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @param category  支出类型，如果为null则不过滤
     * @return 支出列表，默认按支出时间倒序排列 (最新的在前)。如果无支出则返回空列表
     */
    List<TripExpenses> getExpensesByTripIdAndCategory(UUID userId, UUID tripId, ExpenseType category);

    /**
     * 获取当前用户指定旅程的总支出统计信息
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @return 总支出统计信息，包括总金额，最大金额，最小金额，平均金额。如果没有支出信息，返回null
     */
    DoubleSummaryStatistics getTotalExpenseStatisticsByTripId(UUID userId, UUID tripId);

    /**
     * 获取当前用户指定旅程的各类支出统计信息
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @return 各类支出的统计信息，包括总金额，最大金额，最小金额，平均金额（以类为单位）。如果没有支出信息，返回emptyMap
     */
    Map<ExpenseType, DoubleSummaryStatistics> getCategoryExpenseStatisticsByTripId(UUID userId, UUID tripId);

}
