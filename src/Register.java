import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Register extends JFrame {

    JLabel usernameLabel;
    JLabel passwordLabel;
    JTextField textUser;
    JTextField textPassword;
    JButton reg;
    JLabel multifunctionalLabel;
    ActionListener regListener;

    public Register()  {
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(368, 200);
        setTitle("Register");

        Container cp = getContentPane();

        cp.setLayout(null);

        multifunctionalLabel = new JLabel();
        multifunctionalLabel.setLocation(93,90);
        multifunctionalLabel.setSize(190,30);
        multifunctionalLabel.setText("");
        multifunctionalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        multifunctionalLabel.setForeground(Color.red);
        cp.add(multifunctionalLabel);

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

        reg = new JButton();
        reg.setText("Register now");
        reg.setLocation(123,60);
        reg.setSize(120,30);
        cp.add(reg);


    }

    public String getUsername(){

        return textUser.getText();
    }

    public String getPassword(){

        return textPassword.getText();
    }

    public void addListener(ActionListener RegViewListener){

        reg.addActionListener(RegViewListener);
    }



}
