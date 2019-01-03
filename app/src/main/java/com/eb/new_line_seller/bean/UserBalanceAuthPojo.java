package com.eb.new_line_seller.bean;

public class UserBalanceAuthPojo {



    //体现金额
    private float balance;
    //备注
    private String remark;
    //银行卡id
    private int bankId;

    public UserBalanceAuthPojo(float balance, int bankId) {
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

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }
}
