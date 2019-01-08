package com.juner.mvp.bean;

public class MyBalanceEntity {


    String askMoney;
    String balance;
    String authMoney;

    public String getAskMoney() {
        return null == askMoney ? "0.0" : askMoney;
    }



    public void setAskMoney(String askMoney) {
        this.askMoney = askMoney;
    }

    public String getBalance() {
        return balance;
    }

    public double getBalanceDouble() {
        return Double.parseDouble(balance);
    }

    public float getBalancefloat() {
        return Float.parseFloat(balance);
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAuthMoney() {
        return null == authMoney ? "0.0" : authMoney;
    }

    public void setAuthMoney(String authMoney) {
        this.authMoney = authMoney;
    }

    @Override
    public String toString() {
        return "MyBalanceEntity{" +
                "askMoney='" + askMoney + '\'' +
                ", balance='" + balance + '\'' +
                ", authMoney='" + authMoney + '\'' +
                '}';
    }
}
