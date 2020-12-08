package org.geektime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.geektime.mapper.OrderMapper;
import org.geektime.pojo.Order;
import org.geektime.service.IOrderService;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/8
 * @since 1.8
 **/
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IOrderService {
}
