package com.juner.mvp.bean;


//课程
public class CourseListItemEntity {


    String pic;
    String title;
    String synopsis;
    String price;

    public CourseListItemEntity(String pic, String title, String synopsis, String price) {
        this.pic = pic;
        this.title = title;
        this.synopsis = synopsis;
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
