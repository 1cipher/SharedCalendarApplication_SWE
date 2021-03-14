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

        setName("Share current calendar with...");
        setSize(300,200);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(null);

        label = new JLabel("Username: ",SwingConstants.CENTER);
        label.setLocation(30,50);
        label.setSize(100,20);

        username = new JTextField();
        username.setLocation(180,50);
        username.setSize(100,20);

        permission = new JComboBox<>();
        permission.setLocation(180,80);
        permission.setSize(100,20);
        permission.addItem("User");
        permission.addItem("Owner");

        button = new JButton("Share");
        button.setLocation(100,120);
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