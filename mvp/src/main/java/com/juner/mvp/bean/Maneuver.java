package com.juner.mvp.bean;

import java.util.List;

public class Maneuver {


    String id;
    String img;
    String name;
    String explain;
    String createTime;
    String endApplyTime;//报名截止时间
    String startTime;//活动开始时间
    String endTime;//活动结束时间
    Integer joinNum;////参加数
    Integer mommentNum;//评论数
    List<ImgOneList> imgOneList;
    List<ImgOneList> imgTwoList;

    List<Join> joinList;//已参加的门店列表

    int myJoinStatus;//报名 1是审核中   2通过

    public int getMyJoinStatus() {
        return myJoinStatus;
    }

    public void setMyJoinStatus(int myJoinStatus) {
        this.myJoinStatus = myJoinStatus;
    }

    public List<Join> getJoinList() {
        return joinList;
    }

    public void setJoinList(List<Join> joinList) {
        this.joinList = joinList;
    }

    public List<ImgOneList> getImgOneList() {
        return imgOneList;
    }

    public void setImgOneList(List<ImgOneList> imgOneList) {
        this.imgOneList = imgOneList;
    }

    public List<ImgOneList> getImgTwoList() {
        return imgTwoList;
    }

    public void setImgTwoList(List<ImgOneList> imgTwoList) {
        this.imgTwoList = imgTwoList;
    }

    public class ImgOneList {

        String img;
        int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }

    public class Join{

        String unityImg;

        int status;//1审核中2已通过3未通过

        public String getUnityImg() {
            return unityImg;
        }

        public void setUnityImg(String unityImg) {
            this.unityImg = unityImg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndApplyTime() {
        return endApplyTime;
    }

    public void setEndApplyTime(String endApplyTime) {
        this.endApplyTime = endApplyTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(Integer joinNum) {
        this.joinNum = joinNum;
    }

    public Integer getMommentNum() {
        return mommentNum;
    }

    public void setMommentNum(Integer mommentNum) {
        this.mommentNum = mommentNum;
    }
}
