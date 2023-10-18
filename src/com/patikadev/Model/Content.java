package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.View.ContentLookGUI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Content {
    private int id;
    private String contentName;
    private String course_belong_to;
    private String contentDescription;
    private String contentLink;
    private int isCompleted;
    ArrayList<Quiz> quizArray = new ArrayList<>();

    public Content(int id, String contentName, String course_belong_to,String contentDescription,String contentLink,ArrayList<Quiz> quizArr) {
        this.id = id;
        this.contentName = contentName;
        this.course_belong_to = course_belong_to;
        this.contentDescription = contentDescription;
        this.contentLink = contentLink;
        this.quizArray = quizArr;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM contents WHERE id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);

            int result = pr.executeUpdate();

            if (result == 1){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static ArrayList<Content> searchContentList(String name,String courseName){
        String query = "SELECT * FROM contents WHERE name LIKE '%{{name}}%' AND course_belong_to LIKE '%{{course_belong_to}}%'";
        query = query.replace("{{name}}", name);
        query = query.replace("{{course_belong_to}}", courseName);
        ArrayList<Content> contentList = new ArrayList<>();
        ArrayList<Quiz> quizList = new ArrayList<>();
        Content obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){

                quizList = Quiz.getList("name");
                obj = new Content(rs.getInt("id"),rs.getString("name"),rs.getString("course_belong_to"),rs.getString("content_desc"),rs.getString("content_link"),quizList);

                contentList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contentList;
    }

    public static Content getContentByID(String id){
        Content obj = null;
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement("SELECT * FROM contents WHERE id = ?");
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()){
                int ID = rs.getInt("id");
                String name = rs.getString("name");
                String course_belong_to = rs.getString("course_belong_to");
                String content_desc = rs.getString("content_desc");
                String content_link = rs.getString("content_link");

                ArrayList<Quiz> quizList = new ArrayList<>();
                quizList = Quiz.getList(name);

                obj = new Content(ID,name,course_belong_to,content_desc,content_link,quizList);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }

    public static boolean add(String name,String course_belong_to,String content_desc,String content_link){
        String query = "INSERT INTO contents (name,course_belong_to,content_desc,content_link) VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,course_belong_to);
            pr.setString(3,content_desc);
            pr.setString(4,content_link);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Content> getList(String courseName){
        ArrayList<Content> contentList = new ArrayList<>();
        ArrayList<Quiz> quizList = new ArrayList<>();
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
                String content_desc = rs.getString("content_desc");
                String content_link = rs.getString("content_link");

                quizList = Quiz.getList(contentName);

                obj = new Content(id,contentName,courseNameBelongTo,content_desc,content_link,quizList);
                contentList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contentList;
    }

    public ArrayList<Quiz> getQuizArray() {
        return quizArray;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public String getContentLink() {
        return contentLink;
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
