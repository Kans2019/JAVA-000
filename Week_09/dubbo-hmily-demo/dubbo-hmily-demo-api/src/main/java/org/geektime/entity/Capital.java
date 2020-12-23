package org.geektime.entity;

/**
 * 资产基类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/21
 * @since 1.8
 **/
public class Capital implements java.io.Serializable {
    /**
     * 用户名
     */
    protected String userName;

    /**
     * 可支配的资产 单位分
     */
    protected Long disposalAmount;

    /**
     * 冻结的资产 单位分
     */
    protected Long freezeAmount;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDisposalAmount() {
        return disposalAmount;
    }

    public void setDisposalAmount(Long disposalAmount) {
        this.disposalAmount = disposalAmount;
    }

    public Long getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }
}
