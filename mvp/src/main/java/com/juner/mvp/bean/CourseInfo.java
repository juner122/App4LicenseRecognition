package com.juner.mvp.bean;

import java.util.List;

public class CourseInfo {

    Courses course;
   List< ResourcePojos> resourcePojos;

    public Courses getCourses() {
        return course;
    }

    public void setCourses(Courses course) {
        this.course = course;
    }

    public List<ResourcePojos> getResourcePojos() {
        return resourcePojos;
    }

    public void setResourcePojos(List<ResourcePojos> resourcePojos) {
        this.resourcePojos = resourcePojos;
    }
}
