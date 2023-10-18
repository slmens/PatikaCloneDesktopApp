package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Quiz {
    private final String quizContent;
    private String quizQuestion;
    private String quizTrueAnswer;
    private String quizFalseAnswer1;
    private String quizFalseAnswer2;
    private String quizFalseAnswer3;
    private int isAnswered = 0;


    public Quiz(String quizQuestion, String quizTrueAnswer, String quizFalseAnswer1, String quizFalseAnswer2, String quizFalseAnswer3,String quizContent) {
        this.quizQuestion = quizQuestion;
        this.quizContent = quizContent;
        this.quizTrueAnswer = quizTrueAnswer;
        this.quizFalseAnswer1 = quizFalseAnswer1;
        this.quizFalseAnswer2 = quizFalseAnswer2;
        this.quizFalseAnswer3 = quizFalseAnswer3;
    }

    public static boolean trueAnswered(String quizQuestion){
        String query = "UPDATE quiz SET is_answered = 1 WHERE quiz_question = ? ";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,quizQuestion);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean add(Quiz quiz){
        String query = "INSERT INTO quiz (quiz_content,quiz_question,quiz_true_answer,quiz_false_answer_1,quiz_false_answer_2,quiz_false_answer_3) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,quiz.quizContent);
            pr.setString(2,quiz.quizQuestion);
            pr.setString(3,quiz.quizTrueAnswer);
            pr.setString(4,quiz.quizFalseAnswer1);
            pr.setString(5,quiz.quizFalseAnswer2);
            pr.setString(6,quiz.quizFalseAnswer3);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static ArrayList<Quiz> getList(String contentName){
        ArrayList<Quiz> quiztList = new ArrayList<>();
        Quiz obj;
        String query = "SELECT * FROM quiz WHERE quiz_content = ?";
        try {
            PreparedStatement st = DBConnector.getInstance().prepareStatement(query);
            st.setString(1,contentName);
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                String quizQuestion = rs.getString("quiz_question");
                String quizTrue = rs.getString("quiz_true_answer");
                String quizFalse1 = rs.getString("quiz_false_answer_1");
                String quizFalse2 = rs.getString("quiz_false_answer_2");
                String quizFalse3 = rs.getString("quiz_false_answer_3");

                obj = new Quiz(contentName,quizQuestion,quizTrue,quizFalse1,quizFalse2,quizFalse3);
                quiztList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return quiztList;
    }

    public String getQuizContent() {
        return quizContent;
    }

    public String getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(String quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public String getQuizTrueAnswer() {
        return quizTrueAnswer;
    }

    public void setQuizTrueAnswer(String quizTrueAnswer) {
        this.quizTrueAnswer = quizTrueAnswer;
    }

    public String getQuizFalseAnswer1() {
        return quizFalseAnswer1;
    }

    public void setQuizFalseAnswer1(String quizFalseAnswer1) {
        this.quizFalseAnswer1 = quizFalseAnswer1;
    }

    public String getQuizFalseAnswer2() {
        return quizFalseAnswer2;
    }

    public void setQuizFalseAnswer2(String quizFalseAnswer2) {
        this.quizFalseAnswer2 = quizFalseAnswer2;
    }

    public String getQuizFalseAnswer3() {
        return quizFalseAnswer3;
    }

    public void setQuizFalseAnswer3(String quizFalseAnswer3) {
        this.quizFalseAnswer3 = quizFalseAnswer3;
    }
}
