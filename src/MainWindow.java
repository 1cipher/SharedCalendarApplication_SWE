import com.mindfusion.scheduling.Calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Date;

public class MainWindow extends JFrame implements PropertyChangeListener {

    JFormattedTextField textField = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));

    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(368, 362);
        setTitle("Calendar");

        Container cp = getContentPane();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        cp.setLayout(gridBagLayout);
        cp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        textField.setValue(new Date());
        textField.setPreferredSize(new Dimension(130, 30));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;

        cp.add(textField,c);

        JButton calButton = new JButton("ciao");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        cp.add(calButton,c);


        CalendarWindow calendarwindow = new CalendarWindow();
        calendarwindow.setUndecorated(true);
        calendarwindow.addPropertyChangeListener(this);
        Calendar calendar = new Calendar();
        c.fill = GridBagConstraints.CENTER;
        c.gridy = 1;
        cp.add(calendar,c);

        calButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendarwindow.setLocation(textField.getLocationOnScreen().x,
                        (textField.getLocationOnScreen().y + textField.getHeight()));

                calendarwindow.setVisible(true);


            }
        });


    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if (event.getPropertyName().equals("selectedDate")) {

            java.util.Calendar cal = (java.util.Calendar) event.getNewValue();
            Date selDate = cal.getTime();

            textField.setValue(selDate);
        }
    }
}
