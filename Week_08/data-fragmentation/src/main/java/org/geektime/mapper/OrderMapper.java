package org.geektime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.geektime.pojo.Order;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/8
 * @since 1.8
 **/
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
