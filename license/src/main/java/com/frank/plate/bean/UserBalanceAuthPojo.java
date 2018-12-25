package com.frank.plate.bean;

public class UserBalanceAuthPojo {



    //体现金额
    private float balance;
    //备注
    private String remark;
    //银行卡id
    private Integer bankId;

    public UserBalanceAuthPojo(float balance, Integer bankId) {
        this.balance = balance;
        this.bankId = bankId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }
}
