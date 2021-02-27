import javax.swing.*;
import java.awt.*;

public class FailBuilder extends JFrame implements LogDialogBuilder {

    Container cp;
    JButton button;
    JLabel l;

    @Override
    public void addLabel(String Label) {

        l = new JLabel(Label);
        l.setForeground(Color.red);

        cp.add(l);

    }

    @Override
    public void addButton() {

        button = new JButton("Ok");
        button.setLocation(130,80);
        button.setSize(40,20);

        cp.add(button);

    }

    @Override
    public JFrame getDialog() {

        FailBuilder model = new FailBuilder();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(400,200);

        cp = getContentPane();
        cp.setLayout(null);

        return model;
    }
}
