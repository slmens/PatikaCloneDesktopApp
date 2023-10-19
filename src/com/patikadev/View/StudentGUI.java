package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

// metoda studentid yi vericem gidicek o iddeki bütün enrolmentları gezicek. Enrollmentlardaki course idleri alıcak. Sonra her bir kursu idsinden çağırıp row a yazıcak

public class StudentGUI extends JFrame{
    private JPanel wrapper;
    private JTabbedPane tabbedPane1;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTable tbl_patikas;
    private JTable tbl_courses;
    private JTable tbl_contents;
    private JTable tbl_my_courses;
    Student student;

    // PATİKA
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;


    // ALL COURSES
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private JPopupMenu courseMenu;

    // MY COURSES
    private DefaultTableModel mdl_mycourse_list;
    private Object[] row_mycourse_list;
    private JPopupMenu myCourseMenu;

    // CONTENT
    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;
    private JPopupMenu contentMenu;

    public StudentGUI(Student student){
        this.student = student;
        add(wrapper);
        setSize(1000,500);
        int x = Helper.screenCenter("x",getSize());
        int y = Helper.screenCenter("y",getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.PROJECT_TITLE);
        setVisible(true);

        updateCourseTable(mdl_course_list,row_course_list,tbl_courses);
        lbl_welcome.setText("Welcome " + student.getName());

        updatePatikaTable(mdl_patika_list,row_patika_list,tbl_patikas);
        tbl_patikas.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_patikas.getColumnModel().getColumn(1).setMinWidth(150);
        tbl_patikas.getTableHeader().setReorderingAllowed(false);

        tbl_courses.getTableHeader().setReorderingAllowed(false);
        tbl_courses.getColumnModel().getColumn(0).setMaxWidth(75);

        updateMyCourseTable(mdl_mycourse_list,row_mycourse_list,tbl_my_courses,student.getId());
        tbl_my_courses.getColumnModel().getColumn(0).setMaxWidth(50);
        tbl_my_courses.getColumnModel().getColumn(1).setMaxWidth(100);
        tbl_my_courses.getColumnModel().getColumn(3).setMinWidth(200);
        tbl_my_courses.getColumnModel().getColumn(4).setMaxWidth(75);

        courseMenu = new JPopupMenu();
        JMenuItem enrollCourse = new JMenuItem("Enroll To This Course");
        courseMenu.add(enrollCourse);
        tbl_courses.setComponentPopupMenu(courseMenu);

        myCourseMenu = new JPopupMenu();
        JMenuItem deleteCourse = new JMenuItem("Unregister");
        myCourseMenu.add(deleteCourse);
        tbl_my_courses.setComponentPopupMenu(myCourseMenu);

        deleteCourse.addActionListener(e -> {
            int studentID = this.student.getId();
            int courseID = Integer.parseInt(tbl_my_courses.getValueAt(tbl_my_courses.getSelectedRow(),0).toString());

            boolean result = Course.unregister(studentID,courseID);

            if (result){
                Helper.showMessage("You have been unregistered!","Success");
                updateMyCourseTable(mdl_mycourse_list,row_mycourse_list,tbl_my_courses,studentID);
            }else {
                Helper.showMessage("Can't unregister","Warning!");
            }
        });

        enrollCourse.addActionListener(e -> {
            int studentID = this.student.getId();
            int courseID = Integer.parseInt(tbl_courses.getValueAt(tbl_courses.getSelectedRow(),0).toString());

            boolean result = Course.enroll(studentID,courseID);
            if (result){
                Helper.showMessage("You have been enrolled!","Success");
                updateMyCourseTable(mdl_mycourse_list,row_mycourse_list,tbl_my_courses,studentID);
            }else {
                Helper.showMessage("Can't enroll","Warning!");
            }
        });

        tbl_patikas.getSelectionModel().addListSelectionListener(e -> {
            int patikaID = Integer.parseInt(tbl_patikas.getValueAt(tbl_patikas.getSelectedRow(),0).toString());
            updateCourseTable(mdl_course_list,row_course_list,tbl_courses,patikaID);


        });

        tbl_my_courses.getSelectionModel().addListSelectionListener(e -> {
            String courseName = tbl_my_courses.getValueAt(tbl_my_courses.getSelectedRow(),1).toString();
            updateContents(mdl_content_list,row_content_list,tbl_contents,courseName);
        });


        // LOG OUT
        btn_logout.addActionListener(e -> {
            dispose();
            LogInGUI login = new LogInGUI();
        });
    }

    public static void updateContents(DefaultTableModel mdl_content_list, Object[] row_content_list, JTable tbl_contents, String course_belong_to){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_contents.getModel();
        clearModel.setRowCount(0);

        mdl_content_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
                //return super.isCellEditable(row, column);
            }
        };

        Object[] col_content_list = {"ID","Content Name","Is Completed"};
        mdl_content_list.setColumnIdentifiers(col_content_list);

        ArrayList<Content> contentList;
        contentList = Content.getList(course_belong_to);
        String isCompleted;

        for (Content cont:contentList){

            if (cont.isCompleted()){
                isCompleted = "Yes!";
            }else {
                isCompleted = "No!";
            }

            ArrayList<Quiz> quizArr = Quiz.getList(cont.getContentName());
            row_content_list = new Object[]{cont.getId(),cont.getContentName(),isCompleted};
            mdl_content_list.addRow(row_content_list);
        }

        tbl_contents.setModel(mdl_content_list);
    }

    public static void updatePatikaTable(DefaultTableModel mdl_patika_list, Object[] row_patika_list, JTable tbl_patikas){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patikas.getModel();
        clearModel.setRowCount(0);

        mdl_patika_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
                //return super.isCellEditable(row, column);
            }
        };
        Object[] col_patika_list = {"ID","Patika Name"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);

        ArrayList<Patika> patikaList;
        patikaList = Patika.getList();
        for (Patika patika:patikaList){
            row_patika_list = new Object[]{patika.getId(),patika.getName()};
            mdl_patika_list.addRow(row_patika_list);
        }

        tbl_patikas.setModel(mdl_patika_list);
    }

    public static void updateCourseTable(DefaultTableModel mdl_course_list, Object[] row_course_list, JTable tbl_courses,int patikaID){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_courses.getModel();
        clearModel.setRowCount(0);

        mdl_course_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
                //return super.isCellEditable(row, column);
            }
        };
        Object[] col_course_list = {"ID","Course Name","Educator Name","Patika Name","Language"};
        mdl_course_list.setColumnIdentifiers(col_course_list);

        ArrayList<Course> courseList;
        courseList = Course.getListByPatikaID(patikaID);
        for (Course course:courseList){
            row_course_list = new Object[]{course.getId(),course.getName(),course.getEducator().getName(),course.getPatika().getName(),course.getLang()};
            mdl_course_list.addRow(row_course_list);
        }

        tbl_courses.setModel(mdl_course_list);
    }

    public static void updateCourseTable(DefaultTableModel mdl_course_list, Object[] row_course_list, JTable tbl_courses){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_courses.getModel();
        clearModel.setRowCount(0);

        mdl_course_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
                //return super.isCellEditable(row, column);
            }
        };
        Object[] col_course_list = {"ID","Course Name","Educator Name","Patika Name","Language"};
        mdl_course_list.setColumnIdentifiers(col_course_list);


        tbl_courses.setModel(mdl_course_list);
    }



    public static void updateMyCourseTable(DefaultTableModel mdl_course_list, Object[] row_course_list, JTable tbl_courses,int studentID){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_courses.getModel();
        clearModel.setRowCount(0);

        mdl_course_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
                //return super.isCellEditable(row, column);
            }
        };
        Object[] col_course_list = {"ID","Course Name","Educator Name","Patika Name","Language"};
        mdl_course_list.setColumnIdentifiers(col_course_list);

        ArrayList<Course> courseList;
        courseList = searchCoursesByCourseID(searchEnrollmentByID(studentID));
        for (Course course:courseList){
            row_course_list = new Object[]{course.getId(),course.getName(),course.getEducator().getName(),course.getPatika().getName(),course.getLang()};
            mdl_course_list.addRow(row_course_list);
        }

        tbl_courses.setModel(mdl_course_list);
    }


    public static ArrayList<Enrollment> searchEnrollmentByID(int studentID){
        ArrayList<Enrollment> enrollmentArr = new ArrayList<>();
        String query = "SELECT * FROM studentcourseenrollment WHERE student_id = ?";

        try {
            PreparedStatement st = DBConnector.getInstance().prepareStatement(query);
            st.setInt(1,studentID);

            ResultSet rs = st.executeQuery();

            while (rs.next()){
                Enrollment obj = new Enrollment(rs.getInt("course_id"));
                enrollmentArr.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return enrollmentArr;
    }

    public static ArrayList<Course> searchCoursesByCourseID(ArrayList<Enrollment> arr){
        ArrayList<Course> courseArr = new ArrayList<>();
        String query = "SELECT * FROM course WHERE id = ?";

        try {
            PreparedStatement st = DBConnector.getInstance().prepareStatement(query);

            for (Enrollment enr : arr){
                st.setInt(1,enr.getCourseID());
                ResultSet rs = st.executeQuery();

                if (rs.next()){
                    Course obj = new Course(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("patika_id"),rs.getString("name"),rs.getString("lang"));
                    courseArr.add(obj);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courseArr;
    }

}
