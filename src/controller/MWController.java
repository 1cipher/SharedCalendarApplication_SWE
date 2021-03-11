package controller;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.*;
import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.model.*;
import model.*;
import view.*;
import view.Dialog;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MWController {

    private MainWindow mwView;
    private CreateEventWindow cwView;
    private Gateway m;
    private view.Dialog dialog;
    private Login logView;
    private Register regView;
    private EventDisplayWindow eventView;
    private CreateCalendarWindow createCalendarWindow;
    private ShareView shareView;
    private User currentUser;
    private MWController instance = null;

    public MWController(Gateway g) {
        m = g;
        setupLoginWindow();
    }

    public void setupCreateCalendarWindow() {

        createCalendarWindow = new CreateCalendarWindow();
        createCalendarWindow.setVisible(true);
        createCalendarWindow.addCreateCalendarListener(e -> createCalendar());

    }

    public void setupShareView() {

        shareView = new ShareView();
        shareView.setVisible(true);
        shareView.addShareButtonListener(e -> shareCalendar());

    }

    public void setupCalendarWindow() {

        if (mwView.getCurrentCalendar().permission==0) {
            cwView = new CreateEventWindow(m.getUserCalendars(currentUser));
            cwView.setVisible(true);
            cwView.addCreateEventListener(e -> createEvent());
            cwView.addCalendarPressListener(new CalendarinCalendarWindowPressedListener());
        }
        else {
            dialog = new view.Dialog.Builder().setLabel("You are not allowed!").setType(Dialog.type.ERROR).build();
            setupDialog();
        }

    }

    public void setupEditEventWindow(Appointment a){

        if (mwView.getCurrentCalendar().permission==0) {
            cwView = new CreateEventWindow(m.getUserCalendars(currentUser));
            cwView.setVisible(true);
            cwView.addCalendarPressListener(new CalendarinCalendarWindowPressedListener());

            cwView.setStartDate(a.getStartTime().getYear()+"-"+String.format("%02d",a.getStartTime().getMonth())+"-"+String.format("%02d",a.getStartTime().getDay()));
            cwView.setEndDate(a.getEndTime().getYear()+"-"+String.format("%02d",a.getEndTime().getMonth())+"-"+String.format("%02d",a.getEndTime().getDay()));
            cwView.setName(a.getHeaderText());
            cwView.setLocation(a.getLocation().getName());
            cwView.setDescr(a.getDescriptionText());
            cwView.setStartHour(a.getStartTime().toString("HH:mm"));
            cwView.setEndHour(a.getEndTime().toString("HH:mm"));
            cwView.addCreateEventListener(e->editEvent(a.getId()));
            eventView.close();
        }
        else {
            dialog = new view.Dialog.Builder().setLabel("You are not allowed!").setType(Dialog.type.ERROR).build();
            setupDialog();
        }
    }



    public void setupRegisterWindow() {

        regView = new Register();
        regView.setVisible(true);
        regView.addListener(e -> register());

    }

    public void setupEventWindow(Appointment a) {

        eventView = new EventDisplayWindow.Builder()
                .setName(a.getHeaderText())
                .setStartDate(a.getStartTime().toString())
                .setEndDate(a.getEndTime().toString())
                .setLocation(a.getLocation().getName())
                .setDescription(a.getDescriptionText())
                .build();
        eventView.setVisible(true);
        eventView.setTitle(a.getId());
        eventView.addDeleteButtonListener(e -> deleteEvent());
        eventView.addOkButtonListener(e -> eventView.close());
        eventView.addEditButtonListener(e->setupEditEventWindow(a));

    }


    public void setupMainWindow() {

        mwView = new MainWindow();
        mwView.setVisible(true);
        mwView.addChangeViewListener(e -> mwView.changeView());
        mwView.addStyleSelectorListener(e-> mwView.changeStyle());
        mwView.addNewEventListener(e -> setupCalendarWindow());
        mwView.addSearchListener(e -> search());
        mwView.addMainCalendarListener(new mainCalendarAdapter());
        mwView.addLogoutListener(e -> {
            mwView.close();
            setupLoginWindow();
        });
        mwView.addNewCalendarListener(e -> setupCreateCalendarWindow());
        mwView.addSelectedCalendarListener(e -> loadView());
        mwView.addShareCalendarListener(e-> setupShareView());

    }

    public void setupLoginWindow() {

        logView = new Login();
        logView.setVisible(true);
        logView.addLoginListener(e -> login());
        logView.addRegisterListener(e -> setupRegisterWindow());

    }

    public void setupDialog(){

        dialog.setVisible(true);
        dialog.addDialogListener(e->dialog.close());

    }

    public void register() {

        String newUser = regView.getUsername();
        String newPassword = regView.getPassword();

        if (newUser.length() == 0 || newPassword.length() == 0) {

            dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Empty field!").build();
        } else if (m.isExistingUsername(newUser)) {

            dialog = new view.Dialog.Builder().setLabel("Username already existing!").setType(Dialog.type.ERROR).build();

        } else {

            m.registerNewUser(newUser, newPassword);
            dialog = new view.Dialog.Builder().setType(Dialog.type.SUCCESS).setLabel("You have been registered!").build();
        }
        setupDialog();
    }

    private void login(){
        String acquiredUser = logView.getUsername();
        String acquiredPassword = logView.getPassword();

        boolean check = m.checkUserPresence(acquiredUser, acquiredPassword);
        if (check) {

            setupMainWindow();

            if (regView != null) {
                regView.close();
            }
            logView.close();

            currentUser = new User(acquiredUser);

            CalendarCollection cc = m.getUserCalendars(currentUser);
            currentUser.setCollection(cc);

            mwView.setCalendars(cc);
            dialog = new view.Dialog.Builder().setType(Dialog.type.SUCCESS).setLabel("You are logged in!").build();

        } else {
            dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Wrong credentials!").build();
        }
        setupDialog();

    }


    public void loadView() {
        String calID = mwView.getCurrentCalendar().getId();
        model.Calendar calendar = currentUser.getCollection().getCalendar(calID);
        mwView.getCalendar().getSchedule().getAllItems().clear();

        ArrayList<model.Event> events = calendar.getEvents();
        for (model.Event event :
                events) {

            Item appointment = new Appointment();
            appointment.setStartTime(event.getStartDate());
            appointment.setEndTime(event.getEndDate());
            appointment.setHeaderText(event.getName());
            appointment.setDescriptionText(event.getDescription());
            appointment.setId(event.getId());
            Location loc = new Location();
            loc.setName(event.getLocation());
            appointment.setLocation(loc);
            mwView.getCalendar().getSchedule().getItems().add(appointment);
        }
    }

    private void search() {
        String toSearch = mwView.getSearchText();
        ItemList itemList = mwView.getCalendar().getSchedule().getItems();

        SearchStrategy strategy = mwView.getSearchType();
        Item item = strategy.search(itemList,toSearch);

        if (item != null) {
            DateTime dt = item.getStartTime();
            mwView.getCalendar().setDate(dt);
            setupEventWindow((Appointment) item);
        } else {
            dialog = new view.Dialog.Builder().setLabel("No event found!").setType(Dialog.type.ERROR).build();
            setupDialog();
        }
    }

    private void createCalendar(){
        String cid = java.util.UUID.randomUUID().toString().substring(0, 19);
        model.Calendar newCalendar = null;
        if (!createCalendarWindow.getName().isEmpty()) {
            newCalendar = new model.Calendar(currentUser, cid, createCalendarWindow.getName(), 0);
            m.CreateCalendar(newCalendar, currentUser);
            dialog = new view.Dialog.Builder().setLabel("model.Calendar Created").setType(Dialog.type.SUCCESS).build();
        } else {
            dialog = new Dialog.Builder().setLabel("model.Calendar can't be created").setType(Dialog.type.ERROR).build();
        }
        setupDialog();
        createCalendarWindow.close();
        if (newCalendar!=null) {
            mwView.addCalendar(newCalendar);
            CalendarCollection cal = currentUser.getCollection();
            cal.addCalendarToCollection(newCalendar);
            loadView();
        }
    }

    private void shareCalendar(){
        model.Calendar calendar = mwView.getCurrentCalendar();
        String username = shareView.getUsername();
        if (!shareView.getName().isEmpty() && m.isExistingUsername(username)){
            m.shareCalendar(calendar, username, shareView.getPermission());
            dialog = new view.Dialog.Builder().setLabel("Calendar Shared!").setType(Dialog.type.SUCCESS).build();
        } else {
            dialog = new Dialog.Builder().setLabel("Calendar could NOT be shared!").setType(Dialog.type.ERROR).build();
        }
        setupDialog();
        shareView.close();
    }

    private void editEvent(String id){    //TODO:ANDREBBE UNIFICATO AL CREATE EVENT PERCHE ALLA FINE FANNO LA STESSA COSA

        model.Calendar calendar;
        calendar = mwView.getCurrentCalendar();
        m.deleteEvent(id);

        String name = cwView.getName();
        String location = cwView.getLocationName();
        String descr = cwView.getDescriptionText();
        String startdate = cwView.getStartDate();
        String enddate = cwView.getEndDate();

        String startHour = cwView.getStartHour();
        String endHour = cwView.getEndHour();

        DateTime startDate = new DateTime(Integer.parseInt(startdate.substring(0, 4)) - 1900,
                Integer.parseInt(startdate.substring(5, 7)),
                Integer.parseInt(startdate.substring(8, 10)),
                Integer.parseInt(startHour.substring(0, 2)),
                Integer.parseInt(startHour.substring(3, 5)), 0);
        DateTime endDate = new DateTime(Integer.parseInt(enddate.substring(0, 4)) - 1900,
                Integer.parseInt(enddate.substring(5, 7)),
                Integer.parseInt(enddate.substring(8, 10)),
                Integer.parseInt(endHour.substring(0, 2)),
                Integer.parseInt(endHour.substring(3, 5)), 0);

        if (startDate.isLessThan(endDate)) {

            if (!name.isEmpty() && !id.isEmpty() && !startDate.toString().isEmpty() && !endDate.toString().isEmpty()) {
                dialog = new view.Dialog.Builder().setType(Dialog.type.SUCCESS).setLabel("Event edited!").build();
                model.Event event = new model.Event(id, name, startDate, endDate, location, descr);
                m.addEventinEvents(event, calendar.getId());
                currentUser.setCollection(m.getUserCalendars(currentUser));
                mwView.getCalendar().getSchedule().getAllItems().clear();
                loadView();
            }

            else {
                dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Check null values").build();
            }

        }
        else {
            dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Inconsistent dates!").build();
        }

        setupDialog();
        cwView.close();
    }

    private void createEvent(){

        model.Calendar calendar = mwView.getCurrentCalendar();
        String uid = java.util.UUID.randomUUID().toString().substring(0, 19);
        String name = cwView.getName();
        String location = cwView.getLocationName();
        String descr = cwView.getDescriptionText();
        String startdate = cwView.getStartDate();
        String enddate = cwView.getEndDate();

        String startHour = cwView.getStartHour();
        String endHour = cwView.getEndHour();

        DateTime startDate = new DateTime(Integer.parseInt(startdate.substring(0, 4)) - 1900,
                Integer.parseInt(startdate.substring(5, 7)),
                Integer.parseInt(startdate.substring(8, 10)),
                Integer.parseInt(startHour.substring(0, 2)),
                Integer.parseInt(startHour.substring(3, 5)), 0);//TODO: PROBLEMI QUANDO NON SI SELEZIONA LA DATA
        DateTime endDate = new DateTime(Integer.parseInt(enddate.substring(0, 4)) - 1900,
                Integer.parseInt(enddate.substring(5, 7)),
                Integer.parseInt(enddate.substring(8, 10)),
                Integer.parseInt(endHour.substring(0, 2)),
                Integer.parseInt(endHour.substring(3, 5)), 0);

        if (startDate.isLessThan(endDate)) {

            if (!name.isEmpty() && !uid.isEmpty() && !startDate.toString().isEmpty() && !endDate.toString().isEmpty()) {
                dialog = new view.Dialog.Builder().setType(Dialog.type.SUCCESS).setLabel("model.Event Created!").build();
                model.Event event = new model.Event(uid, name, startDate, endDate, location, descr);
                m.addEventinEvents(event, calendar.getId());
                currentUser.setCollection(m.getUserCalendars(currentUser));
                mwView.getCalendar().getSchedule().getAllItems().clear();
                loadView();
            }
            else {
                dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Check null values").build();
            }


        } else {
            dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Inconsistent dates!").build();
        }

        setupDialog();
        cwView.close();
    }

    public void deleteEvent(){
        if (mwView.getCurrentCalendar().permission==0) {
            Appointment appointment = (Appointment) mwView.getCalendar().getSchedule().getItems().get(eventView.getTitle());
            m.deleteEvent(appointment.getId());
            mwView.getCalendar().getSchedule().getItems().remove(appointment);
            eventView.close();
        }
        else {
            dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("You are not allowed!").build();
            setupDialog();
        }
    }

    class mainCalendarAdapter extends CalendarAdapter {

        @Override
        public void itemClick(ItemMouseEvent e) {

            if (eventView != null) {
                eventView.close();
            }
            Appointment a = (Appointment) e.getItem();
            setupEventWindow(a);

        }

        @Override
        public void dateClick(ResourceDateEvent e) {
            Calendar calendar = mwView.getCalendar();
            if (calendar.getCurrentView() == CalendarView.WeekRange) {
                calendar.setCurrentView(CalendarView.Timetable);
                calendar.setDate(e.getDate());
                mwView.getViewMenu().setSelectedIndex(0);
            }
        }
    }

    class CalendarinCalendarWindowPressedListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2) {

                cwView.getCal().getSelection().reset();

                DateTime pointedDate = cwView.getCal().getDateAt(e.getX(), e.getY());

                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
                Date utilDate = cal.getTime();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                cwView.setSelectedStartDate(sqlDate);

            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

            cwView.getCal().getSelection().reset();

            DateTime pointedDate = cwView.getCal().getDateAt(e.getX(), e.getY());

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
            Date utilDate = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            cwView.setSelectedStartDate(sqlDate);

        }

        @Override
        public void mouseReleased(MouseEvent e) {

            cwView.getCal().getSelection().reset();

            DateTime pointedDate = cwView.getCal().getDateAt(e.getX(), e.getY());

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
            Date utilDate = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            String defaultDate = "0001-01-01";
            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd");
            String d1 = sdformat.format(sqlDate);

            if(d1.compareTo(defaultDate)==0) {

                java.sql.Date d = java.sql.Date.valueOf(cwView.getStartDate());
                cwView.setSelectedEndDate(d);
                cwView.setSelectedStartDate(d);
            }

            else if (cwView.getStartDate().compareTo(sqlDate.toString()) > 0) {
                java.sql.Date d = java.sql.Date.valueOf(cwView.getStartDate());
                cwView.setSelectedEndDate(d);
                cwView.setSelectedStartDate(d);
            }

            else
                cwView.setSelectedEndDate(sqlDate);

        }

        @Override
        public void mouseEntered(MouseEvent e) {

            //no need to handle this,it only matters when mouse is inside the calendar
        }

        @Override
        public void mouseExited(MouseEvent e) {

            //no need to handle this,it only matters when mouse is inside the calendar

        }
    }

}



