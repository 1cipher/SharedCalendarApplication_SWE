package view;

import utils.ACL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ShareView extends JFrame {

    private JLabel label;
    private JTextField username;
    private JButton button;
    private JComboBox<String> permission;

    public ShareView(){

        setTitle("Share current calendar with...");
        setSize(400,200);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        Container c = getContentPane();
        c.setLayout(null);

        label = new JLabel("<html>Select username <br>and permission:</html>",SwingConstants.CENTER);
        label.setLocation(10,55);
        label.setSize(140,40);

        username = new JTextField();
        username.setLocation(140,50);
        username.setSize(200,20);

        permission = new JComboBox<>();
        permission.setLocation(140,80);
        permission.setSize(200,20);
        permission.addItem("User");
        permission.addItem("Owner");

        button = new JButton("Share");
        button.setLocation(150,120);
        button.setSize(100,20);

        c.add(label);
        c.add(username);
        c.add(button);
        c.add(permission);
    }

    public String getUsername(){
        return username.getText();
    }

    public int getPermission(){
        if (permission.getSelectedItem()=="Owner")
            return ACL.getOwnerPermission();
        if (permission.getSelectedItem()=="User")
            return ACL.getUserPermission();
        return -1;
    }

    public void addShareButtonListener(ActionListener shareButtonListener){
        this.button.addActionListener(shareButtonListener);
    }

    public void close(){
        setVisible(false);
        dispose();
    }

}