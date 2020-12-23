package org.geektime.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.geektime.entity.DollarCapital;
import org.geektime.provider.mapper.DollarMapper;
import org.geektime.service.IDollarService;

import java.util.Objects;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/19
 * @since 1.8
 **/
@DubboService(version = "1.0.0", interfaceClass = IDollarService.class)
public class DollarService extends ServiceImpl<DollarMapper, DollarCapital> implements IDollarService {
    @Override
    public DollarCapital getByUserName(String userName) {
        return this.getOne(new QueryWrapper<DollarCapital>().eq("user_name", userName));
    }

    @Override
    public boolean saveOrUpdate(DollarCapital entity) {
        if (Objects.nonNull(entity.getId())) {
            entity.setUserName(null);
        }
        return super.saveOrUpdate(entity);
    }
}
