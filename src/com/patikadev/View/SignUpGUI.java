package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.User;

import javax.swing.*;

public class SignUpGUI extends JFrame{
    private JPanel wrapper;
    private JTextField txt_signup_name;
    private JTextField txt_signup_pass;
    private JButton signUpButton;
    private JTextField txt_student_username;

    public SignUpGUI(){
        add(wrapper);
        setSize(670,500);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        // iki kutucuk dolu mu kontrol et
        // isim daha önce var mı kontrol et
        // isim ve pass en az 3 karakter mi kontrol et
        // eğer her şey tamam olursa yeni bir login page oluştur


        signUpButton.addActionListener(e -> {
            if (txt_student_username.getText().isEmpty() || txt_signup_name.getText().isEmpty() || txt_signup_pass.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else{
                if (txt_signup_name.getText().length() < 3 || txt_student_username.getText().length() < 3 || txt_signup_pass.getText().length() < 6){
                    Helper.showMessage("Please fill all the areas with minimum 3 character name and minimum 6 character pass!","Warning!");
                }else {
                    int result = User.add(txt_signup_name.getText(), txt_student_username.getText(),txt_signup_pass.getText(),"student");
                    switch (result){
                        case 1:
                            Helper.showMessage("Succesfully created your account!","Success");
                            break;
                        case -1:
                            Helper.showMessage("There is another user that has the same name!","Warning!");
                            break;
                        case 0:
                            Helper.showMessage("There is something wrong!","Error!");
                            break;
                    }

                }
            }

            LogInGUI logInGUI = new LogInGUI();
            dispose();
        });
    }
}
