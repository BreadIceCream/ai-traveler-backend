package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.entity.Expenses;
import com.bread.traveler.service.ExpensesService;
import com.bread.traveler.mapper.ExpensesMapper;
import org.springframework.stereotype.Service;

/**
* @author huang
* @description 针对表【expenses】的数据库操作Service实现
* @createDate 2025-11-14 12:09:43
*/
@Service
public class ExpensesServiceImpl extends ServiceImpl<ExpensesMapper, Expenses>
    implements ExpensesService{

}




