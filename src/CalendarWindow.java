import javax.swing.*;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.ThemeType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CalendarWindow extends JFrame {

    Calendar calendar;
    JTextField date;
    JLabel dateLabel;

    java.util.Calendar selectedDate = java.util.Calendar.getInstance();

    protected PropertyChangeSupport changeSupport;

    public CalendarWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(1000,500);

        Container cp = getContentPane();
        cp.setLayout(null);

        calendar = new Calendar();
        calendar.setLocation(0,0);
        calendar.setSize(400,200);
        calendar.setTheme(ThemeType.Light);

        dateLabel = new JLabel("Date: ");
        dateLabel.setLocation(420,80);
        dateLabel.setSize(50,20);

        date = new JTextField();
        date.setLocation(480,80);
        date.setSize(200,20);

        cp.add(calendar);
        cp.add(dateLabel);
        cp.add(date);


        changeSupport = new PropertyChangeSupport(this);

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

    public java.util.Calendar getSelectedDate() {

        return selectedDate;
    }

    public void setSelectedDate(java.util.Calendar newDate){

        java.util.Calendar oldDate = (java.util.Calendar)selectedDate.clone();
        selectedDate = newDate;

        date.setText(selectedDate.toString());
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){

        changeSupport.addPropertyChangeListener(listener);
    }


}
