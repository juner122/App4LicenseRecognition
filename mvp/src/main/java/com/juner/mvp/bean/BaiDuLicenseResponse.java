package com.juner.mvp.bean;

public class BaiDuLicenseResponse {

    Words words_result;

    public class Words {

        String color;
        String number;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    public Words getWords_result() {
        return words_result;
    }

    public void setWords_result(Words words_result) {
        this.words_result = words_result;
    }
}
