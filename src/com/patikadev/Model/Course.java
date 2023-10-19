package com.patikadev.Model;

import com.mysql.cj.CoreSession;
import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {
    private int id;
    private int user_id;
    private int patika_id;
    private String name;
    private String lang;

    private Patika patika;
    private User educator;

    public Course(int id, int user_id, int patika_id, String name, String lang) {
        this.id = id;
        this.user_id = user_id;
        this.patika_id = patika_id;
        this.name = name;
        this.lang = lang;
        this.patika = Patika.getFetch(patika_id);
        this.educator = User.getFetch(user_id);
    }

    public static ArrayList<Course> getList(){
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course");
            while (rs.next()){
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                int patikaId = rs.getInt("patika_id");
                String courseName = rs.getString("name");
                String courseLang = rs.getString("lang");
                obj = new Course(id,userId,patikaId,courseName,courseLang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courseList;
    }


    public static ArrayList<Course> getListByUser(int user_id){
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE user_id = "+user_id);
            while (rs.next()){
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                int patikaId = rs.getInt("patika_id");
                String courseName = rs.getString("name");
                String courseLang = rs.getString("lang");
                obj = new Course(id,userId,patikaId,courseName,courseLang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courseList;
    }

    public static ArrayList<Course> getListByPatikaID(int patika_id){
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE patika_id = "+patika_id);
            while (rs.next()){
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                int patikaId = rs.getInt("patika_id");
                String courseName = rs.getString("name");
                String courseLang = rs.getString("lang");
                obj = new Course(id,userId,patikaId,courseName,courseLang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courseList;
    }

    public static boolean add(int user_id, int patika_id,String name, String lang){
        String query = "INSERT INTO course (user_id,patika_id,name,lang) VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,user_id);
            pr.setInt(2,patika_id);
            pr.setString(3,name);
            pr.setString(4,lang);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(int id){
        String query = "DELETE FROM course WHERE id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);

            int result = pr.executeUpdate(query);

            if (result == 1){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean enroll(int student_id,int course_id){
        String query = "INSERT INTO studentcourseenrollment (student_id,course_id) VALUES (?,?)";

        if (isEnrolled(student_id,course_id)){
            try {
                PreparedStatement st = DBConnector.getInstance().prepareStatement(query);
                st.setInt(1,student_id);
                st.setInt(2,course_id);

                return st.executeUpdate() != -1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            return false;
        }
    }

    public static boolean isEnrolled(int student_id,int course_id){
        String query = "SELECT * FROM studentcourseenrollment WHERE student_id = ? AND course_id = ? ";

        try {
            PreparedStatement st = DBConnector.getInstance().prepareStatement(query);
            st.setInt(1,student_id);
            st.setInt(2,course_id);

            ResultSet rs = st.executeQuery();

            if (rs.next()){
                return false;
                //enrolled
            }else {
                return true;
                // not enrolled
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean unregister(int student_id,int course_id){
        String query = "DELETE FROM studentcourseenrollment WHERE student_id = ? AND course_id = ?";

        if (isEnrolled(student_id,course_id)){
            return false;
        }else {
            try {
                PreparedStatement st = DBConnector.getInstance().prepareStatement(query);
                st.setInt(1,student_id);
                st.setInt(2,course_id);

                return st.executeUpdate() != -1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPatika_id() {
        return patika_id;
    }

    public void setPatika_id(int patika_id) {
        this.patika_id = patika_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }
}
