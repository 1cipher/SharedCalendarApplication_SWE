import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {

    public Register()  {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(368, 200);
        setTitle("Login");

        Container cp = getContentPane();

        cp.setLayout(null);

        JLabel usernameLabel = new JLabel();
        usernameLabel.setLocation(10,10);
        usernameLabel.setText("Username");
        usernameLabel.setSize(100,20);
        cp.add(usernameLabel);

        JLabel passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        passwordLabel.setLocation(10,30);
        passwordLabel.setSize(100,20);
        cp.add(passwordLabel);

        JTextField textUser = new JTextField();
        textUser.setLocation(200,10);
        textUser.setSize(120,20);
        cp.add(textUser);

        JTextField textPassword = new JTextField();
        textPassword.setLocation(200,30);
        textPassword.setSize(120,20);
        cp.add(textPassword);

        JButton reg = new JButton();
        reg.setText("Register now");
        reg.setLocation(123,60);
        reg.setSize(120,30);
        cp.add(reg);
        reg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userToReg = textUser.getText();
                String passToReg = textPassword.getText();
                //TODO: registra su database
            }
        });

    }
}
