import com.mindfusion.scheduling.Calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class Login extends JFrame {

    public Login(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(368, 362);
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

        JButton log = new JButton();
        log.setText("Login");
        log.setLocation(50,60);
        log.setSize(120,30);
        log.setVisible(true);
        cp.add(log);
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String insertedUser = textUser.getText();
                String insertedPassword = textPassword.getText();
                String correctUser = "Alessio";
                String correctPassword = "Bonacchi";
                if(correctUser.compareTo(insertedUser)==0){
                    log.setText("positive");
                    MainWindow mw = new MainWindow();
                    mw.show();
                    Main.log.setVisible(false);
                }
                else
                    log.setText("Negative");
            }
        });
    }
}
