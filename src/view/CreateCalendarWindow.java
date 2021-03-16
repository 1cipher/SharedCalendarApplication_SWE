package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CreateCalendarWindow extends JFrame {

    private JLabel calendarName;
    private JTextField calendarText;
    private JButton createCalendar;

    public CreateCalendarWindow(){

        setTitle("Create Calendar");
        setSize(300,200);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        Container c = getContentPane();
        c.setLayout(null);

        calendarName = new JLabel("<html>Choose a name<br>for your calendar:</html> ",SwingConstants.CENTER);
        calendarName.setLocation(20,50);
        calendarName.setSize(100,30);

        calendarText = new JTextField();
        calendarText.setLocation(130,50);
        calendarText.setSize(150,20);

        createCalendar = new JButton("Create");
        createCalendar.setLocation(100,120);
        createCalendar.setSize(100,20);

        c.add(calendarName);
        c.add(calendarText);
        c.add(createCalendar);
    }

    @Override
    public String getName(){

        return calendarText.getText();
    }



    public void addCreateCalendarListener(ActionListener createCalendarListener){

        this.createCalendar.addActionListener(createCalendarListener);
    }

    public void close(){
        setVisible(false);
        dispose();
    }

}
