package com.patikadev.Model;

public class Enrollment {
    private int course_id;

    public Enrollment(int courseID) {
        this.course_id = courseID;
    }

    public int getCourseID() {
        return course_id;
    }
}
