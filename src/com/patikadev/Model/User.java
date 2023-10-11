package com.patikadev.Model;

import com.mysql.cj.util.DnsSrv;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    private int id;
    private String name,uname,pass,type;

    public User(){

    }

    public User(int id, String name, String uname, String pass, String type) {
        this.id = id;
        this.name = name;
        this.uname = uname;
        this.pass = pass;
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ArrayList<User> getList(){
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("userType"));

                userList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public static int add(String name,String uname,String pass,String type){
        String query = "INSERT INTO user (name,uname,pass,userType) VALUES (?,?,?,?)";
        User user = getFetch(uname);

        if (user == null){
            try {
                PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
                pr.setString(1,name);
                pr.setString(2,uname);
                pr.setString(3,pass);
                pr.setString(4,type);
                int result = pr.executeUpdate();
                if (result == 1){
                    return 1;
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());;
            }
        }else{
            return -1;
        }

        return 0;
    }

    public static User getFetch(String uname){
        User obj = null;
        String query = "SELECT * FROM user WHERE uname = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,uname);
            ResultSet rs = pr.executeQuery();

            if (rs.next()){
                obj = new User(rs.getInt("id"),rs.getString("name"), rs.getString("uname"),rs.getString("pass"),rs.getString("userType"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM user WHERE id = ?";
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

    public static boolean update(int id , String name, String nickName, String pass, String userType){
        String query = "UPDATE user SET name = ?, uname = ?, pass = ?, userType = ? WHERE id = ?";

        User user = getFetch(nickName);

        if (user != null && user.getId() != id){
            Helper.showMessage("There is something wrong!", "Error!");
        }else{
            if (userType.equalsIgnoreCase("operator") || userType.equalsIgnoreCase("student") || userType.equalsIgnoreCase("educator")){
                try {
                    PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
                    pr.setString(1,name);
                    pr.setString(2,nickName);
                    pr.setString(3,pass);
                    pr.setString(4,userType);
                    pr.setInt(5, id);

                    int result = pr.executeUpdate();
                    if (result == 1){
                        return true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    public static ArrayList<User> searchUserList(String query){
        ArrayList<User> userList = new ArrayList<>();
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("userType"));

                userList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public static String searchQuery(String name,String uName,String type){
        String query = "SELECT * FROM user WHERE uname LIKE '%{{uname}}%' AND name ILIKE '%{{name}}%'";
        query = query.replace("{{uname}}", uName);
        query = query.replace("{{name}}", name);
        if (type.isEmpty()){
            query +=  "AND type LIKE '{{userType}}'";
            query = query.replace("{{userType}}", type);
        }

        return query;
    }
}
