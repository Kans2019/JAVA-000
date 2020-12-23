package org.geektime.common;

/**
 * 货币类型
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/18
 * @since 1.8
 **/
public enum CurrencyType {
    RMB("人民币"),
    DOLLAR("美元");

    private final String desc;

    CurrencyType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
