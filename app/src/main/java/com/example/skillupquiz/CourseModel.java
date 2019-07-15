package com.example.skillupquiz;

public class CourseModel {
    private String id;
    private String course_title;
    private String instructor_name;
    private String course_description;
    private String instructor_bio;
    private String course_image_url;
    private int student_count;


    public CourseModel() { }

    public CourseModel(String id, String course_title, String instructor_name, String course_description,
                       String instructor_bio, String course_image_url, int student_count) {
        this.id = id;
        this.course_title = course_title;
        this.instructor_name = instructor_name;
        this.course_description = course_description;
        this.instructor_bio = instructor_bio;
        this.course_image_url = course_image_url;
        this.student_count = student_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getInstructor_name() {
        return instructor_name;
    }

    public void setInstructor_name(String instructor_name) {
        this.instructor_name = instructor_name;
    }

    public String getCourse_description() {
        return course_description;
    }

    public void setCourse_description(String course_description) {
        this.course_description = course_description;
    }

    public String getInstructor_bio() {
        return instructor_bio;
    }

    public void setInstructor_bio(String instructor_bio) {
        this.instructor_bio = instructor_bio;
    }

    public String getCourse_image_url() {
        return course_image_url;
    }

    public void setCourse_image_url(String course_image_url) {
        this.course_image_url = course_image_url;
    }

    public int getStudent_count() {
        return student_count;
    }

    public void setStudent_count(int student_count) {
        this.student_count = student_count;
    }
}
