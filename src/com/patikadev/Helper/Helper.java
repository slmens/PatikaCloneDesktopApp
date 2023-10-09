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
}
