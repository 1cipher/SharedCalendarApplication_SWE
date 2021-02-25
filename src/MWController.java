import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.CalendarView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MWController {

    MainWindow mwView;
    CalendarWindow cwView;
    Database model;

    public MWController(MainWindow mw, CalendarWindow cw, Database db) {

        this.mwView = mw;
        this.cwView = cw;
        this.model = db;

        this.mwView.addChangeViewListener(new changeViewListener());
        this.mwView.addAddEventListener(new addEventListener());
        this.mwView.addSearchListener(new searchListener());
        this.cwView.addCreateEventListener(new createEventListener());
        this.cwView.addCalendarPressListener(new calendarPressedListener());


    }

    class addEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            cwView.setVisible(true);

        }
    }

    class searchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class changeViewListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String selection = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();

            if (selection.equals("day"))
                mwView.calendar.setCurrentView(CalendarView.Timetable);
            else if (selection.equals("week"))
                mwView.calendar.setCurrentView(CalendarView.WeekRange);
            else if (selection.equals("month"))
                mwView.calendar.setCurrentView(CalendarView.MonthRange);


        }
    }

    class createEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String uid = java.util.UUID.randomUUID().toString();
            String name = cwView.name.getText();
            String location = cwView.location.getText();
            String descr = cwView.descr.getText();
            java.sql.Date date = java.sql.Date.valueOf(cwView.date.getText());
            String colour = (String) cwView.color.getSelectedItem();

            model.addEventinEvents(uid,name,location,descr,colour,date);




        }
    }

    class calendarPressedListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2) {

                cwView.calendar.getSelection().reset();

                DateTime pointedDate = cwView.calendar.getDateAt(e.getX(), e.getY());

                Calendar cal = Calendar.getInstance();
                cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
                Date utilDate = cal.getTime();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                cwView.setSelectedDate(sqlDate);

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
    }

    class logoutListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            //TODO: PATTERN PER CHIUDERE LA MAINWINDOW E RIAPRIRE LA LOGIN
        }
    }
}
