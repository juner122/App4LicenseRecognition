package com.juner.mvp.bean;


import java.util.List;

//汽车检测报告
public class CarCheckResul {

    Integer id;
    Integer carId;
    Integer infoNum;
    String carNo;

    String postscript;
    String resultSn;//编号

    // 暂存传值为0 生成报告传值为1
    private Integer type;

    Long addTime;

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    List<CheckOptions> optionsList;

    public Integer getCarId() {
        return carId;
    }

    public Integer getInfoNum() {
        return infoNum;
    }

    public void setInfoNum(Integer infoNum) {
        this.infoNum = infoNum;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getCarNo() {
        return carNo;
    }

    public String getResultSn() {
        return resultSn;
    }

    public void setResultSn(String resultSn) {
        this.resultSn = resultSn;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<CheckOptions> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<CheckOptions> optionsList) {
        this.optionsList = optionsList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
