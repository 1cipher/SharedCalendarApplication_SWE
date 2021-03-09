package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ShareView extends JFrame {

    private JLabel label;
    private JTextField username;
    private JButton button;

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

        button = new JButton("Share");
        button.setLocation(100,120);
        button.setSize(100,20);

        c.add(label);
        c.add(username);
        c.add(button);
    }

    public String getUsername(){
        return username.getText();
    }

    public void addShareButtonListener(ActionListener shareButtonListener){
        this.button.addActionListener(shareButtonListener);
    }

    public void close(){
        setVisible(false);
        dispose();
    }

}