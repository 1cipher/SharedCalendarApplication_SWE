import javax.swing.*;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.ThemeType;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CalendarWindow extends JFrame {

    Calendar calendar;
    JTextField date;
    JLabel dateLabel;
    JLabel nameLabel;
    JTextField name;
    JLabel locationLabel;
    JTextField location;
    JComboBox<String> color;
    JLabel colorLabel;
    JLabel descrLabel;
    JTextArea descr;
    JButton createEvent;

    java.util.Calendar selectedDate = java.util.Calendar.getInstance();


    public CalendarWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(850,300);

        Container cp = getContentPane();
        cp.setLayout(null);

        calendar = new Calendar();
        calendar.setLocation(0,0);
        calendar.setSize(400,200);
        calendar.setTheme(ThemeType.Light);

        dateLabel = new JLabel("Date: ");
        dateLabel.setLocation(410,80);
        dateLabel.setSize(50,20);

        date = new JTextField();
        date.setLocation(500,80);
        date.setSize(300,20);

        nameLabel = new JLabel("Name: ");
        nameLabel.setLocation(410,50);
        nameLabel.setSize(50,20);

        name = new JTextField();
        name.setLocation(500,50);
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



        cp.add(calendar);
        cp.add(dateLabel);
        cp.add(date);
        cp.add(name);
        cp.add(nameLabel);
        cp.add(locationLabel);
        cp.add(location);
        cp.add(descr);
        cp.add(descrLabel);
        cp.add(colorLabel);
        cp.add(color);
        cp.add(createEvent);




        calendar.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){

                    calendar.getSelection().reset();

                    DateTime pointedDate = calendar.getDateAt(e.getX(),e.getY());

                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.set(pointedDate.getYear(),pointedDate.getMonth()-1,pointedDate.getDay());

                    setSelectedDate(cal);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }


    public void setSelectedDate(java.util.Calendar newDate){

        selectedDate = newDate;

        date.setText(selectedDate.toString());
    }


    public void addCreateEventListener(ActionListener createEventListener){

        this.createEvent.addActionListener(createEventListener);
    }

    public void addCalendarPressListener(MouseListener calendarPressedListener){

        this.calendar.addMouseListener(calendarPressedListener);
    }


}
