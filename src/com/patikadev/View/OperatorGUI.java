package com.patikadev.View;

import com.patikadev.Helper.*;
import com.patikadev.Model.Course;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Patika;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JPanel pnl_top_panel;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JLabel lbl_welcome;
    private JScrollPane pnl_scroll;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JLabel lbl_user_name;
    private JTextField txt_user_name_field;
    private JLabel lbl_user_nick_name;
    private JTextField txt_nick_name;
    private JLabel lbl_pass;
    private JTextField txt_pass;
    private JLabel lbl_user_type;
    private JComboBox combo_user_type;
    private JButton btn_user_add;
    private JLabel lbl_user_ID;
    private JTextField txt_user_ID;
    private JButton btn_user_delete;
    private JTextField txt_search_name;
    private JLabel lbl_name_search;
    private JTextField txt_search_user_name;
    private JComboBox comb_search;
    private JButton btn_search;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField txt_patika_name_add;
    private JButton btn_patika_add;
    private JScrollPane scrl_courses;
    private JTable tbl_courses;
    private JPanel pnl_course_rigth;
    private JTextField txt_course_add_name;
    private JLabel lbl_course_add_name;
    private JTextField txt_course_add_lang;
    private JComboBox cmb_course_add;
    private JLabel lbl_cmb_patika;
    private JComboBox cmb_course_add_educator_name;
    private JLabel lbl_course_add_educator_name;
    private JButton btn_course_add;
    private final Operator operator;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;

    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private JPopupMenu patikaMenu;

    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;

    OperatorGUI(Operator operator){
        this.operator = operator;
        add(wrapper);
        setSize(1000,500);
        int x = Helper.screenCenter("x",getSize());
        int y = Helper.screenCenter("y",getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldiniz " + operator.getName());

        updateTable(mdl_user_list,row_user_list,tbl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String selected_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString();
                txt_user_ID.setText(selected_user_id);
            }catch (Exception exc){

            }
        });

        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Update");
        JMenuItem deleteMenu = new JMenuItem(("Delete"));
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);
        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    loadPatikaModel();
                    loadCourseModel();
                }
            });

        });

        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")){
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
                if (Patika.delete(select_id)){
                    Helper.showMessage("Success","Success");
                    loadPatikaModel();
                    loadCourseModel();
                }else {
                    Helper.showMessage("Error","Error");
                }
            }
        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID","Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(100);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point point = e.getPoint();
                int selectedRow = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selectedRow,selectedRow);
            }
        });

        mdl_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID","Course Name","Programming Language","Patika","Educator"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];
        loadCourseModel();
        tbl_courses.setModel(mdl_course_list);
        tbl_courses.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_courses.getTableHeader().setReorderingAllowed(false);

        loadCourseCombo();
        loadEducatorCombo();


        btn_user_add.addActionListener(e -> {
            if (txt_user_name_field.getText().isEmpty() || txt_nick_name.getText().isEmpty() || txt_pass.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else{
                int result = User.add(txt_user_name_field.getText(),txt_nick_name.getText(),txt_pass.getText(), combo_user_type.getSelectedItem().toString());
                if (result == 1){
                    Helper.showMessage("Your user have been successfully created!","Success'");
                    updateTable(mdl_user_list,row_user_list,tbl_user_list);
                    loadEducatorCombo();
                    txt_nick_name.setText(null);
                    txt_user_name_field.setText(null);
                    txt_pass.setText(null);

                }else if(result == 0){
                    Helper.showMessage("Your user couldn't be created!","Warning!");
                }else if(result == -1){
                    Helper.showMessage("This user name has been taken!","Warning!");
                }

            }
        });

        tbl_user_list.getModel().addTableModelListener(e ->{
            if (e.getType() == TableModelEvent.UPDATE){
                int userID = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString());
                String userName = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),1).toString();
                String userUName = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),2).toString();
                String userPass = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),3).toString();
                String userType = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),4).toString();

                if (User.update(userID,userName,userUName,userPass,userType)){
                    Helper.showMessage("Success!","Done!");
                }else {
                    Helper.showMessage("There is something wrong","Error!");
                }
                updateTable(mdl_user_list,row_user_list,tbl_user_list);
                loadEducatorCombo();
                loadCourseModel();

            }
        });

        btn_user_delete.addActionListener(e -> {
            if (txt_user_ID.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else {
                int fld_user_id = Integer.parseInt(lbl_user_ID.getText());
                if (Helper.confirm("sure")){
                    if (User.delete(fld_user_id)){
                        Helper.showMessage("You have been successfully delete this user!","Warning!");
                        updateTable(mdl_user_list,row_user_list,tbl_user_list);
                        loadEducatorCombo();
                        loadCourseModel();
                        txt_user_ID.setText(null);
                    }else {
                        Helper.showMessage("This user couldn't be deleted!", "Error!");
                    }
                }
            }
        });

        btn_search.addActionListener(e -> {
            String name = txt_search_name.getText();
            String uName = txt_search_user_name.getText();
            String userType = combo_user_type.getSelectedItem().toString();
            String query = User.searchQuery(name,uName,userType);

            ArrayList<User> filteredUsers = User.searchUserList(query);
            updateTable(mdl_user_list,row_user_list,tbl_user_list,filteredUsers);

        });
        btn_logout.addActionListener(e -> {
            dispose();
            LogInGUI login = new LogInGUI();
        });

        btn_patika_add.addActionListener(e -> {
            if (txt_patika_name_add.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else{
                if (Patika.add(txt_patika_name_add.getText())){
                    Helper.showMessage("Your patika has been successfully created!", "Success");
                    loadPatikaModel();
                    txt_patika_name_add.setText(null);

                }
            }
        });
        btn_course_add.addActionListener(e -> {
            Item patikaItem = (Item) cmb_course_add.getSelectedItem();
            Item userItem = (Item) cmb_course_add_educator_name.getSelectedItem();
            if (txt_course_add_name.getText().isEmpty() || txt_course_add_lang.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else{
                if (Course.add(userItem.getKey(),patikaItem.getKey(),txt_course_add_name.getText(),txt_course_add_lang.getText())){
                    Helper.showMessage("Success","Success");
                    loadCourseModel();
                }else {
                    Helper.showMessage("Error!","Error!");
                }
            }

            txt_course_add_lang.setText(null);
            txt_course_add_name.setText(null);
        });
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_courses.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Course course: Course.getList()){
            i = 0;
            row_course_list[i++] = course.getId();
            row_course_list[i++] = course.getName();
            row_course_list[i++] = course.getLang();
            row_course_list[i++] = course.getPatika().getName();
            row_course_list[i++] = course.getEducator().getName();

            mdl_course_list.addRow(row_course_list);
        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;

        for (Patika patika: Patika.getList()){
            i=0;
            row_patika_list[i++] = patika.getId();
            row_patika_list[i++] = patika.getName();
            mdl_patika_list.addRow(row_patika_list);

        }
        loadCourseCombo();
    }

    public static void updateTable(DefaultTableModel mdl_user_list,Object[] row_user_list,JTable tbl_user_list,ArrayList<User> list){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        mdl_user_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_user_list = {"ID","Ad Soyad","Kullanıcı Adı","Şifre","Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        ArrayList<User> userList;
        userList = list;
        for (User user:userList){
            row_user_list = new Object[]{user.getId(), user.getName(), user.getUname(), user.getPass(), user.getType()};
            mdl_user_list.addRow(row_user_list);
        }

        tbl_user_list.setModel(mdl_user_list);
    }

    public static void updateTable(DefaultTableModel mdl_user_list,Object[] row_user_list,JTable tbl_user_list){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        mdl_user_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_user_list = {"ID","Ad Soyad","Kullanıcı Adı","Şifre","Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        ArrayList<User> userList;
        userList = User.getList();
        for (User user:userList){
            row_user_list = new Object[]{user.getId(), user.getName(), user.getUname(), user.getPass(), user.getType()};
            mdl_user_list.addRow(row_user_list);
        }

        tbl_user_list.setModel(mdl_user_list);
    }

    public void loadCourseCombo(){
        cmb_course_add.removeAllItems();
        for (Patika obj: Patika.getList()){
            cmb_course_add.addItem(new Item(obj.getId(),obj.getName()));
        }
    }

    public void loadEducatorCombo(){
        cmb_course_add_educator_name.removeAllItems();
        for (User obj: User.getList()){
            if (obj.getType().equals("educator")){
                cmb_course_add_educator_name.addItem(new Item(obj.getId(),obj.getName()));

            }
        }
    }

    public static void main(String[] args) {
        Operator operator1 = new Operator();
        Helper.themeSelecter();
        operator1.setId(1);
        operator1.setName("Selim");
        operator1.setPass("12345");
        operator1.setUname("Selo");
        operator1.setType("operator");
        OperatorGUI operatorGUI = new OperatorGUI(operator1);

    }
}
