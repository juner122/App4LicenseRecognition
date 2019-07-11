package com.juner.mvp.bean;

public class CarVinRequest {

    String username;


    String password;

Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Data{

        String vinCode;

        public String getVinCode() {
            return vinCode;
        }

        public void setVinCode(String vinCode) {
            this.vinCode = vinCode;
        }
    }
}
