package com.patikadev.Helper;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme;

import javax.swing.*;
import java.awt.*;

public class Helper {

    public static int screenCenter(String point , Dimension size){
        int planeDimension;
        switch (point){
            case "x":
                planeDimension = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width)/2;
                break;
            case "y":
                planeDimension = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height)/2;
                break;
            default:
                planeDimension = 0;
                break;
        }
        return planeDimension;
    }

    public static void themeSelecter(){
        /*
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            System.out.println(info.getClassName());
            if ("metal".equals(info.getName())){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
            }
            break;
        }*/

        FlatSolarizedDarkIJTheme.setup();
    }

    public static void showMessage(String strMSG,String strTitle){
        String msg;
        switch (strMSG){
            case "fill":
                msg = "Please fill all the areas...";
                break;
            default:
                msg = strMSG;
                break;
        }

        JOptionPane.showMessageDialog(null,msg,strTitle,JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str){
        String msg;
        switch (str){
            case "sure":
                msg = "Are you sure that you want to execute this action?";
                break;
            default:
                msg = str;
        }

        return JOptionPane.showConfirmDialog(null,msg,"Warning!",JOptionPane.YES_NO_OPTION) == 0;
    }
}
