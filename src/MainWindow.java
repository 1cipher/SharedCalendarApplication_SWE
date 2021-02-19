import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.CalendarView;
import com.mindfusion.scheduling.ThemeType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;

public class MainWindow extends JFrame implements PropertyChangeListener {

    JFormattedTextField textField = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
    Calendar calendar;
    JButton changeVisualizationMode;
    JButton search;
    JButton addEvent;
    JButton logout;
    JTextField searchBox;



    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(368, 362);
        setTitle("Calendar");

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        Calendar calendar = new Calendar();
        calendar.beginInit();
        calendar.setCurrentView(CalendarView.Timetable);
        calendar.setTheme(ThemeType.Light);
        calendar.setLocation(0,40);
        calendar.setSize(1000,1000);
        calendar.setVisible(true);
        calendar.endInit();

        addEvent = new JButton();
        addEvent.setText("Create Event");
        addEvent.setLocation(0,10);
        addEvent.setSize(200,20);


        searchBox = new JTextField();
        searchBox.setLocation(210,10);
        searchBox.setSize(300,20);

        search = new JButton(new ImageIcon("C:\\search.png"));
        search.setLocation(520,10);
        search.setSize(20,20);



        cp.add(calendar);
        cp.add(addEvent);
        cp.add(searchBox);
        cp.add(search);
        /*
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

        cp.add(cal);

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
        });*/


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
