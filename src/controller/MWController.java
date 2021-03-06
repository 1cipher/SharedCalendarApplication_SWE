package controller;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.*;
import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.model.*;
import model.*;
import view.*;
import view.Dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MWController {

    private MainWindow mwView;
    private CalendarWindow cwView;
    private Gateway model;
    private view.Dialog dialog;
    private Login logView;
    private Register regView;
    private EventDisplayWindow eventView;
    private CreateCalendarWindow createCalendarWindow;
    private User currentUser;

    public MWController(Gateway db) {

        this.model = db;
        setupLoginWindow();

    }

    public void setupCreateCalendarWindow() {

        createCalendarWindow = new CreateCalendarWindow();
        createCalendarWindow.setVisible(true);
        this.createCalendarWindow.addCreateCalendarListener(e -> createCalendar());

    }

    public void setupCalendarWindow() {

        cwView = new CalendarWindow(model.getUserCalendars(currentUser));
        cwView.setVisible(true);
        this.cwView.addCreateEventListener(e -> createEvent());
        this.cwView.addCalendarPressListener(new CalendarinCalendarWindowPressedListener());

    }

    public void setupRegisterWindow() {

        regView = new Register();
        regView.setVisible(true);
        this.regView.addListener(e -> register());
        //TODO: ci sono problemi con la finestra di registrazione
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
        this.eventView.addDeleteButtonListener(e -> deleteEvent());
        this.eventView.addOkButtonListener(e -> eventView.close());

    }


    public void setupMainWindow() {

        mwView = new MainWindow();
        mwView.setVisible(true);
        this.mwView.addChangeViewListener(e -> mwView.changeView());
        this.mwView.addAddEventListener(e -> setupCalendarWindow());
        this.mwView.addSearchListener(e -> search());
        this.mwView.addMainCalendarListener(new mainCalendarAdapter());
        this.mwView.addLogoutListener(e -> {
            mwView.close();
            setupLoginWindow();
        });
        this.mwView.addCreateCalendarButtonListener(e -> setupCreateCalendarWindow());
        this.mwView.addSelectedCalendarListener(e -> loadView());

    }

    public void setupLoginWindow() {

        logView = new Login();
        logView.setVisible(true);
        this.logView.addLoginListener(e -> login());
        this.logView.addRegisterListener(e -> setupRegisterWindow());

    }

    public void setupDialog(){

        dialog.setVisible(true);
        dialog.addDialogListener(e->dialog.close());

    }

    public void register() {

        String newUser = regView.getUsername();
        String newPassword = regView.getPassword();

        if (newUser.length() == 0 || newPassword.length() == 0) {

            dialog = new view.Dialog.Builder().setType(Dialog.type.error).setLabel("Empty field!").build();
        } else if (model.isExistingUsername(newUser)) {

            dialog = new view.Dialog.Builder().setLabel("Username already existing!").setType(Dialog.type.error).build();

        } else {

            model.registerNewUser(newUser, newPassword);
            dialog = new view.Dialog.Builder().setType(Dialog.type.success).setLabel("You have been registered!").build();
        }
        setupDialog();
    }

    private void login(){
        String acquiredUser = logView.getUsername();
        String acquiredPassword = logView.getPassword();
        try {
            boolean check = model.checkUserPresence(acquiredUser, acquiredPassword);
            if (check) {

                setupMainWindow();

                if (regView != null) {
                    regView.close();
                }
                logView.close();

                currentUser = new User(acquiredUser);

                CalendarCollection cc = model.getUserCalendars(currentUser);
                currentUser.setCollection(cc);

                mwView.setCalendars(cc);
                dialog = new view.Dialog.Builder().setType(Dialog.type.success).setLabel("You are logged in!").build();

            } else {
                dialog = new view.Dialog.Builder().setType(Dialog.type.error).setLabel("Wrong credentials!").build();
            }
        } catch (SQLException ex) { //TODO: no sql here
            dialog = new view.Dialog.Builder().setType(Dialog.type.error).setLabel("Exception!").build();
        }
        setupDialog();

    }


    public void loadView() {
        model.Calendar calendar = mwView.getCurrentCalendar();
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
        String nameToSearch = mwView.getSearchText();
        ItemList itemList = mwView.getCalendar().getSchedule().getItems();
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
            mwView.getCalendar().setDate(dt);
        } else {
            dialog = new view.Dialog.Builder().setLabel("No event found!").setType(Dialog.type.error).build();
            setupDialog();
        }
    }

    private void createCalendar(){
        String cid = java.util.UUID.randomUUID().toString().substring(0, 19);
        if (!createCalendarWindow.getName().isEmpty()) {
            model.Calendar newCalendar = new model.Calendar(currentUser, cid, createCalendarWindow.getName());
            model.CreateCalendar(newCalendar, currentUser);
            model.getUserCalendars(currentUser).addCalendarToCollection(newCalendar);
            dialog = new view.Dialog.Builder().setLabel("model.Calendar Created").setType(Dialog.type.success).build();
        } else {
            dialog = new Dialog.Builder().setLabel("model.Calendar can't be created").setType(Dialog.type.error).build();
        }
        dialog.setVisible(true);
        createCalendarWindow.setVisible(false);
        createCalendarWindow.dispose();
        //TODO aggiungere calendario a tutte le jcombobox e far visualizzare quello per primo
    }

    private void createEvent(){

        String uid = java.util.UUID.randomUUID().toString().substring(0, 19);
        String name = cwView.getName();
        String location = cwView.getLocationName();
        String descr = cwView.getDescriptionText();
        String startdate = cwView.getStartDate();
        String enddate = cwView.getEndDate();

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String startHour = cwView.getStartHour();
        String endHour = cwView.getEndHour();
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = dateFormatter.parse(startdate);
            date2 = dateFormatter.parse(enddate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        java.util.Calendar greg = new GregorianCalendar();

        greg.setTime(date1);
        DateTime startDate = new DateTime(greg.get(java.util.Calendar.YEAR) - 1900, greg.get(java.util.Calendar.MONTH) + 1, greg.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(startHour.substring(0, 2)), Integer.parseInt(startHour.substring(3, 5)), 0);//TODO: PROBLEMI QUANDO NON SI SELEZIONA LA DATA
        greg.setTime(date2);
        DateTime endDate = new DateTime(greg.get(java.util.Calendar.YEAR) - 1900, greg.get(java.util.Calendar.MONTH) + 1, greg.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(endHour.substring(0, 2)), Integer.parseInt(endHour.substring(3, 5)), 0); //TODO:COLLEZIONARE GLI ORARI DAI RISPETTIVI TEXTFIELD
        if (!name.isEmpty() && !uid.isEmpty() && !startDate.toString().isEmpty() && !endDate.toString().isEmpty()) {
            dialog = new view.Dialog.Builder().setType(Dialog.type.success).setLabel("model.Event Created!").build();

        } else {
            dialog = new view.Dialog.Builder().setType(Dialog.type.error).setLabel("Check null values").build();
        }

        setupDialog();

        model.Event event = new model.Event(uid, name, startDate, endDate, location, descr);
        model.addEventinEvents(event, cwView.getCurrentCalendar().getId());
        currentUser.setCollection(model.getUserCalendars(currentUser));
        mwView.getCalendar().getSchedule().getAllItems().clear();
        cwView.close();
    }

    public void deleteEvent(){
        Appointment appointment = (Appointment) mwView.getCalendar().getSchedule().getItems().get(eventView.getTitle());
        model.deleteEvent(appointment.getId());
        mwView.getCalendar().getSchedule().getItems().remove(appointment);
        eventView.close();
    }

    class mainCalendarAdapter extends CalendarAdapter {

        public void itemClick(ItemMouseEvent e) {

            if (eventView != null) {
                eventView.close();
            }
            Appointment a = (Appointment) e.getItem();
            setupEventWindow(a);

        }

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

            if (cwView.getStartDate().compareTo(sqlDate.toString()) > 0) {
                java.sql.Date d = java.sql.Date.valueOf(cwView.getStartDate());
                cwView.setSelectedEndDate(d);
                cwView.setSelectedStartDate(sqlDate);
            } else
                cwView.setSelectedEndDate(sqlDate);

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}


