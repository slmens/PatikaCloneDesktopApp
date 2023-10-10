package com.patikadev.View;

import com.patikadev.Helper.*;
import com.patikadev.Model.Operator;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private final Operator operator;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;

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

        btn_user_add.addActionListener(e -> {
            if (txt_user_name_field.getText().isEmpty() || txt_nick_name.getText().isEmpty() || txt_pass.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else{
                int result = User.add(txt_user_name_field.getText(),txt_nick_name.getText(),txt_pass.getText(), combo_user_type.getSelectedItem().toString());
                if (result == 1){
                    Helper.showMessage("Your user have been successfully created!","Success'");
                    updateTable(mdl_user_list,row_user_list,tbl_user_list);
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

            }
        });

        btn_user_delete.addActionListener(e -> {
            if (txt_user_ID.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else {
                int fld_user_id = Integer.parseInt(lbl_user_ID.getText());
                if (User.delete(fld_user_id)){
                    Helper.showMessage("You have been successfully delete this user!","Warning!");
                    updateTable(mdl_user_list,row_user_list,tbl_user_list);
                }else {
                    Helper.showMessage("This user couldn't be deleted!", "Error!");
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
        });
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
