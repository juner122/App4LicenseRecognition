package com.frank.plate.bean;

public class Card {

    //卡号
    private String cardNumber;
    //开户行
    private String bankName;
    //开户行地址
    private String bankAddr;
    //持卡人
    private String cardholder;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddr() {
        return bankAddr;
    }

    public void setBankAddr(String bankAddr) {
        this.bankAddr = bankAddr;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber=" + cardNumber +
                ", bankName='" + bankName + '\'' +
                ", bankAddr='" + bankAddr + '\'' +
                ", cardholder='" + cardholder + '\'' +
                '}';
    }
}
