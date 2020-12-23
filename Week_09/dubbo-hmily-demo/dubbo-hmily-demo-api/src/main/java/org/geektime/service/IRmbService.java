package org.geektime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.geektime.entity.RmbCapital;

/**
 * 人民币表服务类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/18
 * @since 1.8
 **/
public interface IRmbService extends IService<RmbCapital> {
    RmbCapital getByUserName(String userName);
}
