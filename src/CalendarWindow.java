import javax.swing.*;

import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.ThemeType;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class CalendarWindow extends JFrame {

    Calendar calendar;
    JTextField startDate;
    JTextField startHour;
    JTextField startMin;
    JTextField endDate;
    JTextField endHour;
    JTextField endMin;
    JLabel separator1;
    JLabel separator2;
    JLabel startDateLabel;
    JLabel endDateLabel;
    JLabel nameLabel;
    JTextField name;
    JLabel locationLabel;
    JTextField location;
    JComboBox<String> color;
    JLabel colorLabel;
    JLabel descrLabel;
    JTextArea descr;
    JButton createEvent;
    JLabel isEventCreated;


    public CalendarWindow(){
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        setSize(850,300);

        Container cp = getContentPane();
        cp.setLayout(null);

        calendar = new Calendar();
        calendar.setLocation(0,0);
        calendar.setSize(400,200);
        calendar.setTheme(ThemeType.Light);

        startDateLabel = new JLabel("Start Date: ");
        startDateLabel.setLocation(410,40);
        startDateLabel.setSize(50,20);

        startDate = new JTextField();
        startDate.setLocation(500,40);
        startDate.setSize(100,20);

        startHour = new JTextField();
        startHour.setLocation(610,40);
        startHour.setSize(50,20);

        separator1 = new JLabel(":");
        separator1.setLocation(665,40);
        separator1.setSize(10,20);

        startMin = new JTextField();
        startMin.setLocation(675,40);
        startMin.setSize(50,20);

        endDateLabel = new JLabel("End Date: ");
        endDateLabel.setLocation(410,70);
        endDateLabel.setSize(50,20);

        endDate = new JTextField();
        endDate.setLocation(500,70);
        endDate.setSize(100,20);

        endHour = new JTextField();
        endHour.setLocation(610,70);
        endHour.setSize(50,20);

        separator2 = new JLabel(":");
        separator2.setLocation(665,70);
        separator2.setSize(10,20);

        endMin = new JTextField();
        endMin.setLocation(675,70);
        endMin.setSize(50,20);

        nameLabel = new JLabel("Name: ");
        nameLabel.setLocation(410,10);
        nameLabel.setSize(50,20);

        name = new JTextField();
        name.setLocation(500,10);
        name.setSize(300,20);

        locationLabel = new JLabel("Location: ");
        locationLabel.setLocation(410,110);
        locationLabel.setSize(50,20);

        location = new JTextField();
        location.setLocation(500,110);
        location.setSize(300,20);

        descrLabel = new JLabel("Description: ");
        descrLabel.setLocation(410,140);
        descrLabel.setSize(60,20);

        descr = new JTextArea();
        descr.setLocation(500,140);
        descr.setSize(300,50);

        colorLabel = new JLabel("Colour: ");
        colorLabel.setLocation(410,200);
        colorLabel.setSize(50,20);

        color = new JComboBox<String>();
        color.setLocation(500,200);
        color.setSize(100,20);
        color.addItem("Black");
        color.addItem("Red");
        color.addItem("Green");
        color.addItem("Blue");

        createEvent = new JButton("Create Event");
        createEvent.setLocation(700,200);
        createEvent.setSize(150,20);

        isEventCreated = new JLabel("");
        isEventCreated.setLocation(700,230);
        isEventCreated.setSize(150,20);
        isEventCreated.setVisible(false);



        cp.add(calendar);
        cp.add(startDateLabel);
        cp.add(startDate);
        cp.add(name);
        cp.add(nameLabel);
        cp.add(locationLabel);
        cp.add(location);
        cp.add(descr);
        cp.add(descrLabel);
        cp.add(colorLabel);
        cp.add(color);
        cp.add(createEvent);
        cp.add(isEventCreated);
        cp.add(startHour);
        cp.add(startMin);
        cp.add(separator1);
        cp.add(endDate);
        cp.add(endDateLabel);
        cp.add(endMin);
        cp.add(endHour);
        cp.add(separator2);




    }


    public void setSelectedStartDate(java.sql.Date newDate){

        java.sql.Date selectedDate = newDate;

        startDate.setText((selectedDate.toString()));
    }

    public void setSelectedEndDate(java.sql.Date newDate){

        java.sql.Date selectedDate = newDate;

        endDate.setText((selectedDate.toString()));
    }


    public void addCreateEventListener(ActionListener createEventListener){

        this.createEvent.addActionListener(createEventListener);
    }

    public void addCalendarPressListener(MouseListener calendarPressedListener){

        this.calendar.addMouseListener(calendarPressedListener);
    }


}
