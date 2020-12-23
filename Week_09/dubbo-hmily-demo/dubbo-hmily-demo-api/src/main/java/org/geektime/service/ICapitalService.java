package org.geektime.service;

import org.geektime.common.CurrencyType;
import org.geektime.entity.Capital;

/**
 * 账户服务类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/18
 * @since 1.8
 **/
public interface ICapitalService {
    /**
     * 完成转账的的操作
     * @param sourceUserName 进行转账的用户
     * @param sourceAccount  转账的金额大小
     * @param sourceType     转账的货币类型
     * @param destUserName   转账的目标用户
     * @param destType       转账的目标的货币类型
     * @return 转账成功则返回 true
     */
    boolean transfer(String sourceUserName, Long sourceAccount, CurrencyType sourceType, String destUserName, CurrencyType destType);

    Capital getCapital(String userName, CurrencyType currencyType);
}
