package org.geektime.consumer.app;

import org.geektime.common.CurrencyType;
import org.geektime.service.ICapitalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 转账测试的工具类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/23
 * @since 1.8
 **/
@Component
public class TransferTest implements ApplicationRunner {
    private final static Logger logger = LoggerFactory.getLogger(TransferTest.class);

    @Autowired
    private ICapitalService capitalService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("{} 向 {} 转 {} {}", "A", "A", 1, CurrencyType.DOLLAR.getDesc());
        capitalService.transfer("A", 1L, CurrencyType.DOLLAR,  "A", CurrencyType.RMB);
    }
}
