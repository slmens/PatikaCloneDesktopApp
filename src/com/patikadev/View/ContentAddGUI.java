package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Quiz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ContentAddGUI extends JFrame {
    private JPanel wrapper;
    private JLabel lbl_content_name;
    private JTextField txt_content_name;
    private JLabel lbl_content_descp;
    private JTextField txt_content_link;
    private JTextField txt_content_question;
    private JTextField txt_true_answer;
    private JTextField txt_false_answer_1;
    private JFormattedTextField txt_content_descp;
    private JTextField txt_false_answer_2;
    private JTextField txt_false_answer_3;
    private JButton btn_add_quiz;
    private JTextField txt_course_that_belong;
    private JButton btn_add_content;
    private String course_belong_to;
    ArrayList<Quiz> pseudoQuizArray = new ArrayList<Quiz>();

    // Bu guide quiz tuşuna basılınca bu arraya quiz oluşturup aticaz sonra content add tuşuna basınca bu arrrayi contentin constructırına göndericez ve bu arrayi falan sıfirlicaz veya belki de gerek kalmaz.
    // quizlerde ait oldukları contentin adı denilen bir yer olucak bu da content eklenirken contente girilen name otomatik olarak alınacak quiz constructorına verilicek arka tarafta sonra biz bu quizleri istediğimiz zaman databaseden şu content ismine sahip olanları çek dicez.

    public ContentAddGUI(String course_belong_to){
        this.course_belong_to = course_belong_to;
        add(wrapper);
        setSize(600,800);
        int x = Helper.screenCenter("x",getSize());
        int y = Helper.screenCenter("y",getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.PROJECT_TITLE);
        setVisible(true);

        txt_course_that_belong.setText(course_belong_to);

        btn_add_quiz.addActionListener(e -> {
            if (txt_content_question.getText().isEmpty() || txt_true_answer.getText().isEmpty() || txt_false_answer_1.getText().isEmpty() || txt_false_answer_2.getText().isEmpty() || txt_false_answer_3.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else{
                Quiz quiz = new Quiz(txt_content_question.getText(),txt_true_answer.getText(),txt_false_answer_1.getText(),txt_false_answer_2.getText(),txt_false_answer_3.getText(),txt_content_name.getText());
                boolean result = Quiz.add(quiz);
                if (result){
                    pseudoQuizArray.add(quiz);
                }else {
                    Helper.showMessage("Quiz couldn't be addet","Error!");
                }
                txt_content_question.setText(null);
                txt_true_answer.setText(null);
                txt_false_answer_1.setText(null);
                txt_false_answer_2.setText(null);
                txt_false_answer_3.setText(null);
            }
        });


        btn_add_content.addActionListener(e -> {
            String contentDescp = txt_content_descp.toString();
            if (pseudoQuizArray.isEmpty() || contentDescp.isEmpty() || txt_content_name.getText().isEmpty() || txt_content_link.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else {
                // burda content sınıfı üstünden database e add yapmak lazım
                boolean result = Content.add(txt_content_name.getText(),txt_course_that_belong.getText(),txt_content_descp.getText(),txt_content_link.getText());
                if (result){
                    Helper.showMessage("Success","Success");
                }else {
                    Helper.showMessage("Error","Error");
                }
            }
            dispose();
        });
    }
}
