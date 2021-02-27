import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SuccessBuilder extends JFrame implements LogDialogBuilder {

    Container cp;
    JButton button;
    JLabel l;

    @Override
    public void addLabel(String label) {

        l = new JLabel(label,SwingConstants.CENTER);
        l.setForeground(Color.green);
        l.setLocation(100,30);
        l.setSize(200,20);

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

        SuccessBuilder model = new SuccessBuilder();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(400,200);

        cp = getContentPane();
        cp.setLayout(null);


        return model;
    }

    public void addButtonListener(ActionListener buttonListener){


    }
}
