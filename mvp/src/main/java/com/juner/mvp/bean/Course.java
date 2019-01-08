package com.juner.mvp.bean;

public class Course {


    int id;
    String courseName;
    String courseMarke;
    String coursePrice;
    String courseImg;
    int courseType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
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

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }
}
