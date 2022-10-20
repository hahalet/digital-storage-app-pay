package com.zhongqijia.pay.service.impl;

import com.zhongqijia.pay.bean.MyOrder;
import com.zhongqijia.pay.mapper.MyOrderMapper;
import com.zhongqijia.pay.service.MyOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 我的订单 服务实现类
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Service
public class MyOrderServiceImpl extends ServiceImpl<MyOrderMapper, MyOrder> implements MyOrderService {

}
