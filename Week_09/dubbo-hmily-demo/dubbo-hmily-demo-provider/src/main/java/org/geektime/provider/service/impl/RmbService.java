package org.geektime.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.geektime.entity.RmbCapital;
import org.geektime.provider.mapper.RmbMapper;
import org.geektime.service.IRmbService;

import java.util.Objects;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/18
 * @since 1.8
 **/
@DubboService(version = "1.0.0", interfaceClass = IRmbService.class)
public class RmbService extends ServiceImpl<RmbMapper, RmbCapital> implements IRmbService {
    @Override
    public RmbCapital getByUserName(String userName) {
        return this.getOne(this.lambdaQuery().eq(RmbCapital::getUserName, userName));
    }

    @Override
    public boolean saveOrUpdate(RmbCapital entity) {
        if (Objects.nonNull(entity.getId())) {
            entity.setUserName(null);
        }
        return super.saveOrUpdate(entity);
    }
}
