package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Student;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wTop;
    private JPanel wBottom;
    private JTextField txt_user_login_name;
    private JTextField txt_user_login_pass;
    private JButton btn_user_login;
    private JButton btn_signup;

    public LogInGUI(){
        add(wrapper);
        setSize(670,500);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_user_login.addActionListener(e -> {
            if (txt_user_login_name.getText().isEmpty() || txt_user_login_pass.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else {
                User u = User.getFetch(txt_user_login_name.getText(),txt_user_login_pass.getText());
                if (u == null){
                    Helper.showMessage("User not found!","Error!");
                }else{
                    switch (u.getType()){
                        case "operator":
                            OperatorGUI operatorGUI = new OperatorGUI((Operator) u);
                            break;
                        case "educator":
                            EducatorGUI educatorGUI = new EducatorGUI((Educator) u);
                            break;
                        case "student":
                            StudentGUI studentGUI = new StudentGUI((Student) u);
                            break;

                    }
                    dispose();
                }
            }
        });
        btn_signup.addActionListener(e -> {
            SignUpGUI signUpGUI = new SignUpGUI();
            dispose();
        });
    }

    public static void main(String[] args) {
        Helper.themeSelecter();
        LogInGUI loginGUI = new LogInGUI();
    }
}
