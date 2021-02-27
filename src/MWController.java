import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.*;
import com.mindfusion.scheduling.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;

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
        this.mwView.addMainCalendarListener(new mainCalendarAdapter());
        this.mwView.addLogoutListener(new logoutListener());

    }

    class addEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            cwView.setVisible(true);
            cwView.populateCalendars(model.getCurrentUserCalendars());

        }
    }

    class searchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String nameToSearch = mwView.searchBox.getText();
            ItemList itemList = mwView.calendar.getSchedule().getItems();
            System.out.println(mwView.calendar.getSchedule().getItems().size());
            boolean check = false;
            int i = 0;
            while(!check && i<itemList.size()){
                String comp = itemList.get(i).getHeaderText();
                if(nameToSearch.compareTo(comp)==0)
                    check = true;
                else
                    i++;
            }
            if (check){
                DateTime dt = itemList.get(i).getStartTime();
                mwView.calendar.setDate(dt);
            }
            else{
                mwView.searchBox.setText("Not Found");
            }


        }
    }

    class changeViewListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String selection = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();

            if (selection.equals("day"))
                mwView.calendar.setCurrentView(CalendarView.Timetable);
            else if (selection.equals("week")){
                mwView.calendar.setCurrentView(CalendarView.WeekRange);
                mwView.calendar.getWeekRangeSettings().setHeaderStyle(EnumSet.of(WeekRangeHeaderStyle.Title));
            }
            else if (selection.equals("month"))
                mwView.calendar.setCurrentView(CalendarView.MonthRange);


        }

    }

    class createEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String uid = java.util.UUID.randomUUID().toString().substring(0,19);
            String name = cwView.name.getText();
            String location = cwView.location.getText();
            String descr = cwView.descr.getText();
            String startdate = cwView.startDate.getText();
            String enddate = cwView.endDate.getText();

            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat hourFormatter = new SimpleDateFormat("HH:mm");
            String startHour = (String) cwView.startHour.getSelectedItem();
            String endHour = (String) cwView.endHour.getSelectedItem();
            Date date1 = new Date();
            Date date2 = new Date();
            try {
                date1 = dateFormatter.parse(startdate);
                date2 = dateFormatter.parse(enddate);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            //TODO: AGGIUSTARE FORMAT DELL'ORA

            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTime(date1);
            Calendar calendar2 = new GregorianCalendar();
            calendar2.setTime(date2);

            DateTime startDate = new DateTime(calendar1.get(Calendar.YEAR)-1900,calendar1.get(Calendar.MONTH)+1,calendar1.get(Calendar.DAY_OF_MONTH)-1,10,10,0);//TODO: PROBLEMI QUANDO NON SI SELEZIONA LA DATA
            DateTime endDate = new DateTime(calendar2.get(Calendar.YEAR)-1900,calendar2.get(Calendar.MONTH)+1,calendar2.get(Calendar.DAY_OF_MONTH)-1,11,11,0); //TODO:COLLEZIONARE GLI ORARI DAI RISPETTIVI TEXTFIELD
            if(!name.isEmpty() && !uid.isEmpty() && !startDate.toString().isEmpty() && !endDate.toString().isEmpty()) {
                cwView.isEventCreated.setVisible(true);
                cwView.isEventCreated.setText("Event created");
                cwView.isEventCreated.setForeground(Color.green);
            }
            else{
                cwView.isEventCreated.setVisible(true);
                cwView.isEventCreated.setForeground(Color.red);
                cwView.isEventCreated.setText("Missing datas");
            }

            Event event = new Event(uid,name,startDate,endDate,location);
            model.addEventinEvents(event,cwView.getCurrentCalendar());
            Item appointment = new Appointment();
            appointment.setStartTime(new DateTime(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH)+1,calendar1.get(Calendar.DAY_OF_MONTH),11,00,00));
            appointment.setEndTime(new DateTime(calendar2.get(Calendar.YEAR),calendar2.get(Calendar.MONTH)+1,calendar2.get(Calendar.DAY_OF_MONTH),12,0,00));
            mwView.calendar.getSchedule().getItems().add(appointment);


        }
    }

    class calendarPressedListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2) {

                cwView.cal.getSelection().reset();

                DateTime pointedDate = cwView.cal.getDateAt(e.getX(), e.getY());

                Calendar cal = Calendar.getInstance();
                cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
                Date utilDate = cal.getTime();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                cwView.setSelectedStartDate(sqlDate);


            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

            cwView.cal.getSelection().reset();

            DateTime pointedDate = cwView.cal.getDateAt(e.getX(), e.getY());

            Calendar cal = Calendar.getInstance();
            cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
            Date utilDate = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            cwView.setSelectedStartDate(sqlDate);

        }

        @Override
        public void mouseReleased(MouseEvent e) {

            cwView.cal.getSelection().reset();

            DateTime pointedDate = cwView.cal.getDateAt(e.getX(), e.getY());

            Calendar cal = Calendar.getInstance();
            cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
            Date utilDate = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            if(cwView.startDate.getText().compareTo(sqlDate.toString())>0) {
                java.sql.Date d = java.sql.Date.valueOf(cwView.startDate.getText());
                cwView.setSelectedEndDate(d);
                cwView.setSelectedStartDate(sqlDate);
            }
            else
                cwView.setSelectedEndDate(sqlDate);

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    class mainCalendarAdapter extends CalendarAdapter{

        public void itemClick(ItemMouseEvent e){

            if(mwView.deleteSelector.isSelected()){
                mwView.calendar.getSelection().reset();
                mwView.calendar.getSchedule().getItems().remove(e.getItem());
            }
        }
    }

    class logoutListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            mwView.setVisible(false);
            Main.log.setVisible(true);

        }
    }


}

