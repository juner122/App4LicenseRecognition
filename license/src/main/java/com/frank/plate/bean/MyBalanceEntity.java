package com.frank.plate.bean;

public class MyBalanceEntity {


    String askMoney;
    String balance;
    String authMoney;

    public String getAskMoney() {
        return askMoney;
    }

    public void setAskMoney(String askMoney) {
        this.askMoney = askMoney;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAuthMoney() {
        return authMoney;
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
