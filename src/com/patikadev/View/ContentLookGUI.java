package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Quiz;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class ContentLookGUI extends JFrame{
    private JPanel wrapper;
    private JLabel lbl_content_name;
    private JLabel lbl_course_belong_to;
    private JLabel lbl_desc;
    private JLabel lbl_youtube_link;
    private JLabel lbl_quiz_question;
    private JRadioButton lbl_first_radio;
    private JRadioButton lbl_second_radio;
    private JRadioButton lbl_third_radio;
    private JRadioButton lbl_fourth_radio;
    private JButton btn_submit_button;
    private JButton completeTheContentButton;
    private String selectedContentID;
    private Content content;
    ArrayList<Quiz> quizList = new ArrayList<>();
    Quiz quiz;
    private String selectedRadioText;

    public ContentLookGUI(String selectedContentID) {
        this.selectedContentID = selectedContentID;
        add(wrapper);
        setSize(1000,500);
        int x = Helper.screenCenter("x",getSize());
        int y = Helper.screenCenter("y",getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.PROJECT_TITLE);
        setVisible(true);

        content = Content.getContentByID(selectedContentID);
        if (content == null){
            Helper.showMessage("Content coulndn't found!","Error");
            dispose();
        }
        lbl_content_name.setText(content.getContentName());
        lbl_course_belong_to.setText(content.getCourse_belong_to());
        lbl_desc.setText(content.getContentDescription());
        lbl_youtube_link.setText(content.getContentLink());

        updateQuiz(0);




        btn_submit_button.addActionListener(e -> {
            /*// öncelikle bir tane radio işaretlenmiş olduğundan emin oluyorum / sonra o radio doğru mu diye kontrol ediyorum doğruysa doğru diyorum ve diğer soruya geçiyorum yanlış ise
            // yanlış diyorum ve bekliyorum doğru yapana kadar /


            if (lbl_first_radio.isSelected() || lbl_second_radio.isSelected() || lbl_third_radio.isSelected() || lbl_fourth_radio.isSelected()){
                if (Objects.equals(selectedRadioText, quiz.getQuizTrueAnswer())){
                    Helper.showMessage("You answer correct this quiz!","Success!");
                    Quiz.trueAnswered(quiz.getQuizQuestion());
                }
            }else{
                Helper.showMessage("Please choose an answer!","Warning!");
            }
            */

            Helper.showMessage("You can't answer quiz because you are not student!","Warning!");

        });
        completeTheContentButton.addActionListener(e -> {
            // Bütün quizler bitmiş mi onu kontrol et/ bitmişse complete yap ve o contenti database de complete yap / content listte de görünsün bu ana ekranda
            dispose();
        });

        ActionListener listener = e -> {
            if (lbl_first_radio.isSelected()){
                selectedRadioText = lbl_first_radio.getText();
            }else if (lbl_second_radio.isSelected()){
                selectedRadioText = lbl_second_radio.getText();
            }else if (lbl_third_radio.isSelected()){
                selectedRadioText = lbl_third_radio.getText();
            }else if (lbl_fourth_radio.isSelected()){
                selectedRadioText = lbl_fourth_radio.getText();
            }
        };
        lbl_first_radio.addActionListener(listener);
        lbl_second_radio.addActionListener(listener);
        lbl_third_radio.addActionListener(listener);
        lbl_fourth_radio.addActionListener(listener);

    }






    public void updateQuiz(int quizCount){
        int rand = randNumber();
        quizList = content.getQuizArray();
        quiz = quizList.get(quizCount);

        lbl_quiz_question.setText(quiz.getQuizQuestion());

        if (rand < 2){
            lbl_first_radio.setText(quiz.getQuizTrueAnswer());
            lbl_second_radio.setText(quiz.getQuizFalseAnswer1());
            lbl_third_radio.setText(quiz.getQuizFalseAnswer3());
            lbl_fourth_radio.setText(quiz.getQuizFalseAnswer2());
        }else if(rand < 5){
            lbl_first_radio.setText(quiz.getQuizFalseAnswer3());
            lbl_second_radio.setText(quiz.getQuizFalseAnswer2());
            lbl_third_radio.setText(quiz.getQuizFalseAnswer1());
            lbl_fourth_radio.setText(quiz.getQuizTrueAnswer());
        }else if(rand < 8){
            lbl_first_radio.setText(quiz.getQuizFalseAnswer3());
            lbl_second_radio.setText(quiz.getQuizTrueAnswer());
            lbl_third_radio.setText(quiz.getQuizFalseAnswer1());
            lbl_fourth_radio.setText(quiz.getQuizFalseAnswer2());
        }else {
            lbl_first_radio.setText(quiz.getQuizFalseAnswer3());
            lbl_second_radio.setText(quiz.getQuizFalseAnswer1());
            lbl_third_radio.setText(quiz.getQuizTrueAnswer());
            lbl_fourth_radio.setText(quiz.getQuizFalseAnswer2());
        }
    }


    public int randNumber(){
        return (int)(Math.random() * 10);
    }
}
