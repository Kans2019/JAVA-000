package org.geektime.consumer.service.impl;

import com.google.common.base.Objects;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.hmily.annotation.Hmily;
import org.geektime.common.CurrencyType;
import org.geektime.entity.Capital;
import org.geektime.entity.DollarCapital;
import org.geektime.entity.RmbCapital;
import org.geektime.service.ICapitalService;
import org.geektime.service.IDollarService;
import org.geektime.service.IRmbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/19
 * @since 1.8
 **/
@Service
public class CapitalService implements ICapitalService {
    protected long bill = 7; // 美元和人民币的比率

    @DubboReference(version = "1.0.0", url = "dubbo://127.0.0.1:12345", timeout = 3000)
    private IDollarService dollarService;

    @DubboReference(version = "1.0.0", url = "dubbo://127.0.0.1:12345", timeout = 3000)
    private IRmbService rmbService;

    @Transactional
    @Hmily(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean transfer(String sourceUserName, Long sourceAccount, CurrencyType sourceType, String destUserName, CurrencyType destType) {
        Assert.isTrue(this.freeze(sourceUserName, sourceType, sourceAccount), "冻结资产失败");
        return true;
    }

    @Override
    public Capital getCapital(String userName, CurrencyType currencyType) {
        switch (currencyType) {
            case RMB:
                return rmbService.getByUserName(userName);
            case DOLLAR:
                return dollarService.getByUserName(userName);
            default:
                throw new UnsupportedOperationException("不支持的货币类型 " + currencyType);
        }
    }

    /**
     * 冻结一部分账户余额
     * @param userName
     * @param currencyType
     * @param count
     * @return
     */
    private boolean freeze(String userName, CurrencyType currencyType, Long count) {
        Capital capital = this.getCapital(userName, currencyType);
        if (capital.getDisposalAmount() < count) {
            throw new IllegalArgumentException(String.format("用户[%s]余额不足", userName));
        }
        capital.setDisposalAmount(capital.getDisposalAmount() - count);
        capital.setFreezeAmount(capital.getFreezeAmount() + count);
        switch (currencyType) {
            case RMB:
                return rmbService.saveOrUpdate((RmbCapital) capital);
            case DOLLAR:
                return dollarService.saveOrUpdate((DollarCapital) capital);
            default:
                throw new UnsupportedOperationException("不支持的货币类型 " + currencyType);
        }
    }

    @Transactional
    public void confirm(String sourceUserName, Long sourceAccount, CurrencyType sourceType, String destUserName, CurrencyType destType) {
        Capital dest = this.getCapital(destUserName, destType);
        Long account = this.bill(sourceType, destType).multiply(BigDecimal.valueOf(sourceAccount)).longValueExact();
        dest.setDisposalAmount(account + dest.getDisposalAmount());
        switch (destType) {
            case DOLLAR: dollarService.saveOrUpdate((DollarCapital) dest); break;
            case RMB: rmbService.saveOrUpdate((RmbCapital) dest); break;
            default: throw new IllegalArgumentException("不支持的货币类型");
        }
        Capital src = this.getCapital(sourceUserName, sourceType);
        // 解冻
        src.setFreezeAmount(0L);
        switch (sourceType) {
            case RMB: rmbService.saveOrUpdate((RmbCapital) src); break;
            case DOLLAR: dollarService.saveOrUpdate((DollarCapital) src); break;
            default: throw new IllegalArgumentException("不支持的货币类型");
        }
    }

    @Transactional
    public void cancel(String sourceUserName, Long sourceAccount, CurrencyType sourceType, String destUserName, CurrencyType destType) {
        Capital src = this.getCapital(sourceUserName, sourceType);
        // 解冻
        src.setDisposalAmount(src.getDisposalAmount() + src.getFreezeAmount());
        src.setFreezeAmount(0L);
        switch (sourceType) {
            case RMB: rmbService.saveOrUpdate((RmbCapital) src); break;
            case DOLLAR: dollarService.saveOrUpdate((DollarCapital) src); break;
            default: throw new IllegalArgumentException("不支持的货币类型");
        }
    }

    private BigDecimal bill(CurrencyType sourceType, CurrencyType targetType) {
        if (Objects.equal(sourceType, targetType)) {
            return BigDecimal.ONE;
        }

        if (Objects.equal(sourceType, CurrencyType.DOLLAR)) {
            return BigDecimal.ONE.divide(BigDecimal.valueOf(this.bill));
        } else {
            return BigDecimal.valueOf(this.bill);
        }
    }
}
