package com.patikadev.View;

import com.patikadev.Helper.*;
import com.patikadev.Model.Operator;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        mdl_user_list = new DefaultTableModel();
        Object[] col_user_list = {"ID","Ad Soyad","Kullanıcı Adı","Şifre","Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        ArrayList<User> userList;
        userList = User.getList();
        for (User user:userList){
            row_user_list = new Object[]{user.getId(), user.getName(), user.getUname(), user.getPass(), user.getType()};
            mdl_user_list.addRow(row_user_list);
        }

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);
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
