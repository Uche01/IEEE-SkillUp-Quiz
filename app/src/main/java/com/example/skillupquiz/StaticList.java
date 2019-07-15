package com.example.skillupquiz;

import java.util.ArrayList;

public class StaticList {

    private StaticList(){

    }

    private static StaticList instance;
    private static ArrayList<CourseModel> courses;
    public static StaticList getInstance(){
        if(instance != null){
            return instance;
        }
        else {
            instance = new StaticList();
            return instance;
        }
    }

    public void setCourses(ArrayList<CourseModel> courses) {
        StaticList.courses = courses;
    }

    public ArrayList<CourseModel> getCourses() {
        return courses;
    }
}
