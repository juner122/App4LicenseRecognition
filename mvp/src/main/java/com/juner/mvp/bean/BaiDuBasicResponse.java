package com.juner.mvp.bean;


import java.util.List;

//通用文字识别
public class BaiDuBasicResponse {

    String log_id;
    int words_result_num;
    List<Words> words_result;
    String vin;


    //返回中是否有车架号
    public boolean isVin() {
        if (getWords_result_num() == 0) {
            return false;
        }
        for (Words w : getWords_result()) {
            if (w.getWords().length() == 17) {//车架固定长度
                return true;
            }
        }
        return false;
    }


    public String getVin() {

        for (Words w : getWords_result()) {
            if (w.getWords().length() == 17) {//车架固定长度
                return w.getWords();
            }
        }
        return vin;
    }

    public class Words {

        String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<Words> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<Words> words_result) {
        this.words_result = words_result;
    }
}
