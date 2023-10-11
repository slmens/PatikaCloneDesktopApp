package com.patikadev.View;

import com.patikadev.Helper.Constants;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Patika;

import javax.swing.*;

public class UpdatePatikaGUI extends JFrame {
    private JPanel wrapper;
    private JTextField txt_field_patika_name_update;
    private JButton btn_update_patika;
    private Patika patika;

    public UpdatePatikaGUI(Patika patika){
        this.patika = patika;
        add(wrapper);
        setSize(300,150);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.PROJECT_TITLE);
        setVisible(true);

        txt_field_patika_name_update.setText(patika.getName());

        btn_update_patika.addActionListener(e -> {
            if (txt_field_patika_name_update.getText().isEmpty()){
                Helper.showMessage("fill","Warning!");
            }else {
                if (Patika.update(patika.getId(),txt_field_patika_name_update.getText())){
                    Helper.showMessage("Success","Success");
                }else {
                    Helper.showMessage("Error","Error ");
                }
                dispose();
            }
        });
    }
}
