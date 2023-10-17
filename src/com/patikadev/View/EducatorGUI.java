package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EducatorGUI extends JFrame{
    private JPanel wrapper;
    private JTabbedPane wrapper_bottom;
    private JButton btn_log_out;
    private JLabel lbl_welcome;
    private JTable tbl_courses;
    private JTable tbl_contents;
    private JPanel wrapper_top;
    private JLabel lbl_contents;
    Educator educator;

    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;

    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;

    public EducatorGUI(Educator educator){
        this.educator = educator;
        add(wrapper);
        setSize(1000,500);
        int x = Helper.screenCenter("x",getSize());
        int y = Helper.screenCenter("y",getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldiniz " + educator.getName());
        updateCourseTable(mdl_course_list,row_course_list,tbl_courses);
        tbl_contents.getTableHeader().setReorderingAllowed(false);
        tbl_courses.getTableHeader().setReorderingAllowed(false);
        tbl_courses.getColumnModel().getColumn(0).setMaxWidth(75);

        updateContentsTable(mdl_content_list,row_content_list,tbl_contents);
        tbl_contents.getColumnModel().getColumn(0).setMaxWidth(50);
        tbl_contents.getColumnModel().getColumn(1).setMinWidth(100);
        tbl_contents.getColumnModel().getColumn(2).setMinWidth(150);



        // SELECTION MODEL
        tbl_courses.getSelectionModel().addListSelectionListener(e -> {
            try {
                String selected_course_name = tbl_courses.getValueAt(tbl_courses.getSelectedRow(),1).toString();
                updateContentsTable(mdl_content_list,row_content_list,tbl_contents,selected_course_name);
                tbl_contents.getColumnModel().getColumn(0).setMaxWidth(50);
                tbl_contents.getColumnModel().getColumn(1).setMinWidth(150);
                tbl_contents.getColumnModel().getColumn(2).setMinWidth(150);

            }catch (Exception exc){

            }
        });

        // LOG OUT
        btn_log_out.addActionListener(e -> {
            dispose();
            LogInGUI login = new LogInGUI();
        });
    }



    public static void updateCourseTable(DefaultTableModel mdl_course_list, Object[] row_course_list, JTable tbl_courses){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_courses.getModel();
        clearModel.setRowCount(0);

        // course id, course name, educator name,patika name,language name

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
        courseList = Course.getList();
        for (Course course:courseList){
            row_course_list = new Object[]{course.getId(),course.getName(),course.getEducator().getName(),course.getPatika().getName(),course.getLang()};
            mdl_course_list.addRow(row_course_list);
        }

        tbl_courses.setModel(mdl_course_list);
    }

    public static void updateContentsTable(DefaultTableModel mdl_content_list,Object[] row_content_list,JTable tbl_contents,String courseName){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_contents.getModel();
        clearModel.setRowCount(0);

        mdl_content_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
                //return super.isCellEditable(row, column);
            }
        };
        Object[] col_content_list = {"ID","Content Name","Course That Content Belongs"};
        mdl_content_list.setColumnIdentifiers(col_content_list);

        ArrayList<Content> contentList;
        contentList = Content.getList(courseName);
        for (Content content: contentList){
            row_content_list = new Object[]{content.getId(),content.getContentName(),content.getCourse_belong_to()};
            mdl_content_list.addRow(row_content_list);
        }

        tbl_contents.setModel(mdl_content_list);
    }

    public static void updateContentsTable(DefaultTableModel mdl_content_list,Object[] row_content_list,JTable tbl_contents){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_contents.getModel();
        clearModel.setRowCount(0);

        mdl_content_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
                //return super.isCellEditable(row, column);
            }
        };
        Object[] col_content_list = {"ID","Content Name","Course That Content Belongs"};
        mdl_content_list.setColumnIdentifiers(col_content_list);

        tbl_contents.setModel(mdl_content_list);
    }



    public static void main(String[] args) {

    }
}
