package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;

public class EducatorGUI extends JFrame{
    private JPanel wrapper;
    private JTabbedPane wrapper_bottom;
    private JButton btn_log_out;
    private JLabel lbl_welcome;
    private JTable tbl_courses;
    private JTable tbl_contents;
    private JPanel wrapper_top;
    private JLabel lbl_contents;
    private JTextField txt_search_content_name;
    private JTextField txt_search_course_name;
    private JButton btn_search;
    Educator educator;

    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private JPopupMenu courseMenu;

    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;
    private JPopupMenu contentMenu;

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
        updateCourseTable(mdl_course_list,row_course_list,tbl_courses,educator);
        tbl_contents.getTableHeader().setReorderingAllowed(false);
        tbl_courses.getTableHeader().setReorderingAllowed(false);
        tbl_courses.getColumnModel().getColumn(0).setMaxWidth(75);

        updateContentsTable(mdl_content_list,row_content_list,tbl_contents);
        tbl_contents.getColumnModel().getColumn(0).setMaxWidth(50);
        tbl_contents.getColumnModel().getColumn(1).setMinWidth(100);
        tbl_contents.getColumnModel().getColumn(2).setMinWidth(150);

        contentMenu = new JPopupMenu();
        JMenuItem contentLook = new JMenuItem("Enter the Content");
        JMenuItem contentDelete = new JMenuItem("Delete");
        contentMenu.add(contentLook);
        contentMenu.add(contentDelete);
        tbl_contents.setComponentPopupMenu(contentMenu);


        contentDelete.addActionListener(e -> {
            try {
                int selectedContentID = (int) tbl_contents.getValueAt(tbl_contents.getSelectedRow(),0);
                Content content = Content.getContentByID(String.valueOf(selectedContentID));

                boolean result = Content.delete(selectedContentID);
                if (result){
                    Helper.showMessage("Success","Success");
                    updateContentsTable(mdl_content_list,row_content_list,tbl_contents,content.getCourse_belong_to());
                }
            }catch (Exception ex){
                System.out.println("Bu satırda sorun var");
            }
        });

        contentLook.addActionListener(e -> {
            try {
                String selectedContentID = tbl_contents.getValueAt(tbl_contents.getSelectedRow(),0).toString();
                ContentLookGUI contentLookGUI = new ContentLookGUI(selectedContentID);
            }catch (Exception ex){
                //Helper.showMessage("Please select the row properly!","Warning!" );
            }

        });

        courseMenu = new JPopupMenu();
        JMenuItem addContentMenu = new JMenuItem("Add Content");
        courseMenu.add(addContentMenu);
        tbl_courses.setComponentPopupMenu(courseMenu);

        addContentMenu.addActionListener(e -> {
            String selectedCourseName = tbl_courses.getValueAt(tbl_courses.getSelectedRow(),1).toString();
            ContentAddGUI contentGUI = new ContentAddGUI(selectedCourseName);
            contentGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    updateContentsTable(mdl_content_list,row_content_list,tbl_contents,selectedCourseName);
                }
            });

        });

        // SELECTION MODEL
        tbl_courses.getSelectionModel().addListSelectionListener(e -> {
            try {
                String selected_course_name = tbl_courses.getValueAt(tbl_courses.getSelectedRow(),1).toString();
                updateContentsTable(mdl_content_list,row_content_list,tbl_contents,selected_course_name);
                tbl_contents.getColumnModel().getColumn(0).setMaxWidth(50);
                tbl_contents.getColumnModel().getColumn(1).setMinWidth(150);
                tbl_contents.getColumnModel().getColumn(2).setMinWidth(150);

            }catch (Exception exc){
                System.out.println("seçilemedi");
            }
        });

        // LOG OUT
        btn_log_out.addActionListener(e -> {
            dispose();
            LogInGUI login = new LogInGUI();
        });

        btn_search.addActionListener(e -> {
            String contentName = txt_search_content_name.getText();
            String courseName = txt_search_course_name.getText();
            ArrayList<Content> filteredContentList = new ArrayList<>();

            if (!contentName.isEmpty() || !courseName.isEmpty()){
                if (!Objects.equals(courseName, " ") || !contentName.equals(" ")){
                    filteredContentList = Content.searchContentList(contentName,courseName);

                    updateContentsTable(mdl_content_list,row_content_list,tbl_contents,filteredContentList);
                }
            }else {
                String selected_course_name = tbl_courses.getValueAt(tbl_courses.getSelectedRow(),1).toString();
                updateContentsTable(mdl_content_list,row_content_list,tbl_contents,selected_course_name);
            }
        });
    }




    public static void updateCourseTable(DefaultTableModel mdl_course_list, Object[] row_course_list, JTable tbl_courses, Educator educator){
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
        courseList = Course.getListByUser(educator.getId());
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

    public static void updateContentsTable(DefaultTableModel mdl_content_list,Object[] row_content_list,JTable tbl_contents,ArrayList<Content> contentList){
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
