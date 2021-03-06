package view;

import com.mindfusion.scheduling.Calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class Login extends JFrame {

    JLabel usernameLabel;
    JLabel passwordLabel;
    JTextField textUser;
    JTextField textPassword;
    JButton register;
    JButton log;

    public Login(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(368, 200);
        setTitle("view.Login");

        Container cp = getContentPane();

        cp.setLayout(null);

        usernameLabel = new JLabel();
        usernameLabel.setLocation(10,10);
        usernameLabel.setText("Username");
        usernameLabel.setSize(100,20);
        cp.add(usernameLabel);

        passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        passwordLabel.setLocation(10,30);
        passwordLabel.setSize(100,20);
        cp.add(passwordLabel);

        textUser = new JTextField();
        textUser.setLocation(200,10);
        textUser.setSize(120,20);
        cp.add(textUser);

        textPassword = new JTextField();
        textPassword.setLocation(200,30);
        textPassword.setSize(120,20);
        cp.add(textPassword);

        register = new JButton();
        register.setText("view.Register");
        register.setLocation(200,60);
        register.setSize(120,30);
        cp.add(register);

        log = new JButton();
        log.setText("view.Login");
        log.setLocation(20,60);
        log.setSize(120,30);
        cp.add(log);

    }

    public String getUsername(){

        return textUser.getText();
    }

    public String getPassword(){

        return textPassword.getText();
    }


    void addLoginListener(ActionListener loginListener){

        log.addActionListener(loginListener);

    }

    void addRegisterListener(ActionListener registerListener){

        register.addActionListener(registerListener);
    }

}