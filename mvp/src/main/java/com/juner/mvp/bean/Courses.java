package com.juner.mvp.bean;

public class Courses {

    int id;
    int pageView;//看过人数
    String courseName;
    String courseMarke;
    String courseType;
    String courseMv;
    String courseImg;
    String coursePrice;
    long createTime;
    long courseStart;
    long courseEnd;

    public int getPageView() {
        return pageView;
    }

    public void setPageView(int pageView) {
        this.pageView = pageView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseMarke() {
        return courseMarke;
    }

    public void setCourseMarke(String courseMarke) {
        this.courseMarke = courseMarke;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseMv() {
        return courseMv;
    }

    public void setCourseMv(String courseMv) {
        this.courseMv = courseMv;
    }

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(long courseStart) {
        this.courseStart = courseStart;
    }

    public long getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(long courseEnd) {
        this.courseEnd = courseEnd;
    }
}
