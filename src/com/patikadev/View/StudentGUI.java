package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Course;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Patika;
import com.patikadev.Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentGUI extends JFrame{
    private JPanel wrapper;
    private JTabbedPane tabbedPane1;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTable tbl_patikas;
    private JTable tbl_courses;
    private JTable tbl_my_courses;
    private JTable tbl_contents;
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

        courseMenu = new JPopupMenu();
        JMenuItem enrollCourse = new JMenuItem("Enroll To This Course");
        courseMenu.add(enrollCourse);
        tbl_courses.setComponentPopupMenu(courseMenu);

        enrollCourse.addActionListener(e -> {
            int studentID = this.student.getId();
            int courseID = Integer.parseInt(tbl_courses.getValueAt(tbl_courses.getSelectedRow(),0).toString());

            boolean result = Course.enroll(studentID,courseID);
            if (result){
                Helper.showMessage("You have been enrolled!","Success");
            }else {
                Helper.showMessage("Can't enroll","Success");
            }
        });

        tbl_patikas.getSelectionModel().addListSelectionListener(e -> {
            int patikaID = Integer.parseInt(tbl_patikas.getValueAt(tbl_patikas.getSelectedRow(),0).toString());
            updateCourseTable(mdl_course_list,row_course_list,tbl_courses,patikaID);


        });


        // LOG OUT
        btn_logout.addActionListener(e -> {
            dispose();
            LogInGUI login = new LogInGUI();
        });
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

}
