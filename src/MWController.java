import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.*;
import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.model.*;
import listener.LoginListener;
import model.CalendarCollection;
import model.Database;
import model.Event;
import model.User;
import view.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MWController {

    private MainWindow mwView;
    private CalendarWindow cwView;
    private Database model;
    private view.Dialog dialog;
    private Login logView;
    private Register regView;
    private EventDisplayWindow eventView;
    private CreateCalendarWindow createCalendarWindow;
    private User currentUser;

    public MWController(Login lview, Database db) {

        this.model = db;
        this.logView = lview;
        this.logView.addLoginListener(new LoginListener());
        this.logView.addRegisterListener(new RegisterListener());

    }

    public void attachCreateCalendarWindow() {

        this.createCalendarWindow.addCreateCalendarListener(new createCalendarListener());
    }

    public void attachMainWindow() {

        this.mwView.addChangeViewListener(new changeViewListener());
        this.mwView.addAddEventListener(new addEventListener());
        this.mwView.addSearchListener(new searchListener());
        this.mwView.addMainCalendarListener(new mainCalendarAdapter());
        this.mwView.addLogoutListener(new logoutListener());
        this.mwView.addCreateCalendarButtonListener(new createCalendarButtonListener());
        this.mwView.addSelectedCalendarListener(new selectedCalendarListener());

    }

    public void attachCalendarWindow() {

        this.cwView.addCreateEventListener(new createEventListener());
        this.cwView.addCalendarPressListener(new calendarinCalendarWindowPressedListener());

    }

    public void attachLoginWindow() {

        this.logView.addLoginListener(new LoginListener());
        this.logView.addRegisterListener(new RegisterListener());
    }

    public void attachRegisterWindow() {

        this.regView.addListener(new RegViewListener());
    }

    public void attachEventDisplayWindow() {

        this.eventView.addDeleteButtonForEventDisplayWindowListener(new deleteButtonForEventDisplayWindowListener());
        this.eventView.addOkButtonForEventDisplayWindowListener(new okButtonForEventDisplayWindowListener());
    }

    class searchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String nameToSearch = mwView.searchBox.getText();
            ItemList itemList = mwView.calendar.getSchedule().getItems();
            System.out.println(mwView.calendar.getSchedule().getItems().size());
            boolean check = false;
            int i = 0;
            while (!check && i < itemList.size()) {
                String comp = itemList.get(i).getHeaderText();
                if (nameToSearch.compareTo(comp) == 0)
                    check = true;
                else
                    i++;
            }
            if (check) {
                DateTime dt = itemList.get(i).getStartTime();
                mwView.calendar.setDate(dt);
            } else {
                dialog = new view.Dialog.Builder().setDialogTitle("view.Register Problem")
                        .setLabel("Username already present")
                        .setColor(Color.red)
                        .build();
                dialog.setVisible(true);
                dialog.addDialogListener(new dialogListener());
            }


        }
    }

    public void loadView(CalendarCollection calendars) {
        ArrayList<model.Event> events = calendars.getEvents();
        for (Event event :
                events) {
            Item appointment = new Appointment();
            DateTime start = event.getStartDate();
            DateTime end = event.getEndDate();
            appointment.setStartTime(new DateTime(start.getYear(), start.getMonth(), start.getDay(), start.getHour(), start.getMinute(), 0));
            appointment.setEndTime(new DateTime(end.getYear(), end.getMonth(), end.getDay(), end.getHour(), end.getMinute(), 0));
            appointment.setHeaderText(event.getName());
            appointment.setDescriptionText(event.getDescription());
            appointment.setId(event.getId());
            Location loc = new Location();
            loc.setName(event.getLocation());
            appointment.setLocation(loc);
            mwView.calendar.getSchedule().getItems().add(appointment);
        }
    }

    class mainCalendarAdapter extends CalendarAdapter {

        public void itemClick(ItemMouseEvent e) {

            if (eventView != null) {
                eventView.setVisible(false);
                eventView.dispose();
            }
            Appointment a = (Appointment) e.getItem();
            eventView = new EventDisplayWindow.Builder()
                    .setName(a.getHeaderText())
                    .setStartDate(a.getStartTime().toString())
                    .setEndDate(a.getEndTime().toString())
                    .setLocation(a.getLocation().getName())
                    .setDescription(a.getDescriptionText())
                    .build();
            eventView.setVisible(true);
            eventView.setTitle(a.getId());
            attachEventDisplayWindow();

        }

        public void dateClick(ResourceDateEvent e) {
            if (mwView.calendar.getCurrentView() == CalendarView.WeekRange) {
                mwView.calendar.setCurrentView(CalendarView.Timetable);
                mwView.calendar.setDate(e.getDate());
                mwView.viewMenu.setSelectedIndex(0);
            }
        }
    }
    public void populateCalendars(CalendarCollection list){

        ArrayList<Calendar> calendars_list = list.getCalendars();
        for (Calendar calendar:
                calendars_list) {
            selectedCalendarMenu.addItem(calendar);
        }
    }

    public Calendar getCurrentCalendar(){

        return (Calendar) selectedCalendarMenu.getSelectedItem();

    }

}


