package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.annotation.TripAccessValidate;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.TripExpenseCreateUpdateDto;
import com.bread.traveler.entity.TripExpenses;
import com.bread.traveler.enums.ExpenseType;
import com.bread.traveler.enums.MemberRole;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.TripExpensesService;
import com.bread.traveler.mapper.TripExpensesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huang
 * @description 针对表【expenses】的数据库操作Service实现
 * @createDate 2025-11-14 12:09:43
 */
@Slf4j
@Service
public class TripExpensesServiceImpl extends ServiceImpl<TripExpensesMapper, TripExpenses> implements TripExpensesService {

    @Autowired
    @Lazy
    private TripExpensesService selfProxy;

    @Override
    @TripAccessValidate(lowestRole = MemberRole.VIEWER) // 只有成员才能添加属于自己的花费信息
    public TripExpenses addExpense(UUID userId, UUID tripId, TripExpenseCreateUpdateDto dto) {
        log.info("Add expense: user {}, trip {}, dto {}", userId, tripId, dto);
        Assert.notNull(dto, "dto cannot be null");
        Assert.notNull(dto.getAmount(), Constant.EXPENSE_AMOUNT_NOT_NULL);
        Assert.notNull(dto.getCategory(), Constant.EXPENSE_CATEGORY_NOT_NULL);
        TripExpenses expenses = BeanUtil.copyProperties(dto, TripExpenses.class);
        expenses.setExpenseId(UUID.randomUUID());
        expenses.setUserId(userId);
        expenses.setTripId(tripId);
        expenses.setExpenseTime(dto.getExpenseTime() == null ? OffsetDateTime.now(ZoneId.systemDefault()) : dto.getExpenseTime());
        if (save(expenses)) {
            log.info("Expense added success: {}", expenses);
            return expenses;
        }
        log.error("Expense added failed: {}", expenses);
        throw new BusinessException(Constant.EXPENSE_ADD_FAILED);
    }

    @Override
    @TripAccessValidate(lowestRole = MemberRole.VIEWER) // 只有成员才能添加属于自己的花费信息
    public boolean batchAddExpenses(UUID userId, UUID tripId, List<TripExpenseCreateUpdateDto> dtos) {
        log.info("Batch add expenses: user {}, trip {}, dtos {}", userId, tripId, dtos);
        Assert.notNull(dtos, "dtos cannot be null");
        Assert.notEmpty(dtos, "dtos cannot be empty");
        // 将dtos转换为 expenses
        List<TripExpenses> expenses = dtos.stream().map(dto -> {
            Assert.notNull(dto, "dto cannot be null");
            Assert.notNull(dto.getAmount(), Constant.EXPENSE_AMOUNT_NOT_NULL);
            Assert.notNull(dto.getCategory(), Constant.EXPENSE_CATEGORY_NOT_NULL);
            TripExpenses expense = BeanUtil.copyProperties(dto, TripExpenses.class);
            expense.setExpenseId(UUID.randomUUID());
            expense.setUserId(userId);
            expense.setTripId(tripId);
            expense.setExpenseTime(dto.getExpenseTime() == null ? OffsetDateTime.now(ZoneId.systemDefault()) : dto.getExpenseTime());
            return expense;
        }).toList();
        if (selfProxy.saveBatch(expenses)) {
            log.info("Batch add expenses success: {}", expenses);
            return true;
        }
        log.error("Batch add expenses failed: {}", expenses);
        throw new RuntimeException(Constant.EXPENSE_ADD_FAILED);
    }

    @Override
    public boolean deleteExpense(UUID userId, UUID expenseId) {
        log.info("Delete expense: user {}, expense {}", userId, expenseId);
        Assert.notNull(expenseId, "expenseId cannot be null");
        Assert.notNull(userId, "userId cannot be null");
        // 检查expense是否存在，且检查用户是否是创建者
        TripExpenses expense = validateAndGet(userId, expenseId);
        // 删除
        if (removeById(expenseId)) {
            log.info("Expense deleted success: {}", expense);
            return true;
        }
        log.error("Expense deleted failed: {}", expense);
        throw new BusinessException(Constant.EXPENSE_DELETE_FAILED);
    }

    @Override
    public TripExpenses updateExpense(UUID userId, UUID expenseId, TripExpenseCreateUpdateDto dto) {
        log.info("Update expense: user {}, expense {}, dto {}", userId, expenseId, dto);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(expenseId, "expenseId cannot be null");
        Assert.notNull(dto, "dto cannot be null");
        // 检查expense是否存在，且检查用户是否是创建者
        TripExpenses expense = validateAndGet(userId, expenseId);
        // 更新, 将dto中的值复制到expense中，不会忽略null值
        BeanUtil.copyProperties(dto, expense);
        if (updateById(expense)){
            log.info("Expense updated success: {}", expense);
            return expense;
        }
        log.error("Expense updated failed: {}", expense);
        throw new BusinessException(Constant.EXPENSE_UPDATE_FAILED);
    }

    @Override
    public TripExpenses getExpenseById(UUID userId, UUID expenseId) {
        log.info("Get expense: user {}, expense {}", userId, expenseId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(expenseId, "expenseId cannot be null");
        return validateAndGet(userId, expenseId);
    }

    @Override
    public List<TripExpenses> getExpensesByTripId(UUID userId, UUID tripId) {
        log.info("Get expenses by trip id: user {}, trip {}", userId, tripId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        List<TripExpenses> expensesList = lambdaQuery().eq(TripExpenses::getTripId, tripId).eq(TripExpenses::getUserId, userId).list();
        if (expensesList == null){
            log.info("Empty expenses of user {} for trip {}", userId, tripId);
            return Collections.emptyList();
        }
        // 按照花费时间降序排序
        expensesList.sort(Comparator.comparing(TripExpenses::getExpenseTime).reversed());
        return expensesList;
    }

    @Override
    public DoubleSummaryStatistics getTotalExpenseStatisticsByTripId(UUID userId, UUID tripId) {
        log.info("Get total expense statistics: user {}, trip {}", userId, tripId);
        List<TripExpenses> allExpenses = getExpensesByTripId(userId, tripId);
        if (allExpenses.isEmpty()){
            // 没有支出信息，返回null
            return null;
        }
        return allExpenses.stream().collect(Collectors.summarizingDouble(expense -> expense.getAmount().doubleValue()));
    }

    @Override
    public Map<ExpenseType, DoubleSummaryStatistics> getCategoryExpenseStatisticsByTripId(UUID userId, UUID tripId) {
        log.info("Get expense statistics by category: user {}, trip {}", userId, tripId);
        List<TripExpenses> allExpenses = getExpensesByTripId(userId, tripId);
        if (allExpenses.isEmpty()){
            // 没有支出信息，返回空
            return Collections.emptyMap();
        }
        return allExpenses.stream().collect(
                Collectors.groupingBy(
                        TripExpenses::getCategory,
                        Collectors.summarizingDouble(expense -> expense.getAmount().doubleValue())
                ));
    }

    /**
     * 检查expense是否存在，且检查当前用户是否是创建者
     * @param userId 当前用户id
     * @param expenseId expenseId
     * @return 如果验证通过，返回expenseId对应的Expenses对象
     * @throws BusinessException 如果验证不通过，则抛出异常 EXPENSE_NOT_EXIST
     */
    private TripExpenses validateAndGet(UUID userId, UUID expenseId) {
        TripExpenses expense = getById(expenseId);
        if (expense == null || !userId.equals(expense.getUserId())){
            log.error("Expense not found: user {}, expense {}", userId, expenseId);
            throw new BusinessException(Constant.EXPENSE_NOT_EXIST);
        }
        return expense;
    }
}
