package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Content {
    private int id;
    private String contentName;
    private String course_belong_to;

    public Content(int id, String contentName, String course_belong_to) {
        this.id = id;
        this.contentName = contentName;
        this.course_belong_to = course_belong_to;
    }

    public static ArrayList<Content> getList(String courseName){
        ArrayList<Content> contentList = new ArrayList<>();
        Content obj;
        String query = "SELECT * FROM contents WHERE course_belong_to = ?";
        try {
            PreparedStatement st = DBConnector.getInstance().prepareStatement(query);
            st.setString(1,courseName);
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                String contentName = rs.getString("name");
                String courseNameBelongTo = rs.getString("course_belong_to");

                obj = new Content(id,contentName,courseNameBelongTo);
                contentList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contentList;
    }


    public int getId() {
        return id;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getCourse_belong_to() {
        return course_belong_to;
    }

    public void setCourse_belong_to(String course_belong_to) {
        this.course_belong_to = course_belong_to;
    }
}
