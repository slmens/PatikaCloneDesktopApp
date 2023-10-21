package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Quiz;
import com.patikadev.Model.Student;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.chrono.HijrahEra;
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
    private User user;
    private boolean isAllQuizAnswered = false;
    int quizCountWeCurrentlyIn;
    int counter;

    public ContentLookGUI(String selectedContentID,User user) {
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

        int quizQuestNumber = howMuchQuiz();
        quizCountWeCurrentlyIn = 1;
        counter = 0;
        updateQuiz(counter);

        if (user.getType().equals("student")){
            if (Content.isStudentCompletedContent(user.getId(),content.getId())){
                completeTheContentButton.setText("COMPLETED");
                completeTheContentButton.setEnabled(false);
            };
        }


        btn_submit_button.addActionListener(e -> {
            // öncelikle bir tane radio işaretlenmiş olduğundan emin oluyorum / sonra o radio doğru mu diye kontrol ediyorum doğruysa doğru diyorum ve diğer soruya geçiyorum yanlış ise
            // yanlış diyorum ve bekliyorum doğru yapana kadar /

            if (!Objects.equals(user.getType(), "student")){
                Helper.showMessage("You can't answer quiz because you are not student!","Warning!");

            } else {
                if (isAllQuizAnswered){
                    Helper.showMessage("You answered all the questions!","Warning!");
                }else if (lbl_first_radio.isSelected() || lbl_second_radio.isSelected() || lbl_third_radio.isSelected() || lbl_fourth_radio.isSelected()){
                    if (Objects.equals(selectedRadioText, quiz.getQuizTrueAnswer())){
                        Helper.showMessage("You answer correct this quiz!","Success!");
                        quizCountWeCurrentlyIn += 1;
                        updateQuiz(counter);
                        if (quizCountWeCurrentlyIn > quizQuestNumber){
                            isAllQuizAnswered = true;
                        }
                        counter++;
                    }else {
                        Helper.showMessage("Wrong choice!","Warning!");
                    }
                }else{
                    Helper.showMessage("Please choose an answer!","Warning!");
                }
            }

        });
        completeTheContentButton.addActionListener(e -> {
            // Bütün quizler bitmiş mi onu kontrol et/ bitmişse complete yap ve o contenti database de complete yap / content listte de görünsün bu ana ekranda
            // sonra da bu ekran tekrar açıldığı zaman başta bir kontrol yap eğer bu student bu contenti bitirmişse you already complete that olsun bu tuş ve enabled olmasın

            if (user.getType().equals("student")){
                if (isAllQuizAnswered){
                    boolean result = Content.studentCompleteContent(user.getId(),content.getId());
                    if (result){
                        Helper.showMessage("You complete this content!","Success!");
                    }else {
                        Helper.showMessage("There is something wrong!","Error!");
                    }
                    //student id ve content id olan bir table yap ana sayfada contentleri çekerken aynı zamanda bu tableı çekersin ve bu content tamamlanmışsa iscompleted YES dersin
                    dispose();
                }else{
                    Helper.showMessage("You didn't complete the quiz!","Warning!");
                }
            }else {
                dispose();
            }


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





    public int howMuchQuiz(){
        ArrayList<Quiz> quizList = content.getQuizArray();
        return quizList.size();
    }

    public void updateQuiz(int quizCount){
        int rand = randNumber();
        quizList = Quiz.getList(content.getContentName());
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
