package controller;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.*;
import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.model.*;
import model.*;
import utils.RBAC;
import view.*;
import view.Dialog;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private MainView mwView;
    private EditEventView cwView;
    private SearchView sView;
    private  DeleteCalendarView deleteCalendarView;
    private Gateway gateway;
    private view.Dialog dialog;
    private Login logView;
    private RegisterView regView;
    private EventDisplayView eventView;
    private CreateCalendarView createCalendarView;
    private ShareView shareView;
    private User currentUser;
    private Mailer mailer;

    public Controller(Gateway g) {
        gateway = g;
        mailer = new Mailer();
        setupLoginWindow();
    }

    public void setupCreateCalendarWindow() {

        createCalendarView = new CreateCalendarView();
        createCalendarView.setVisible(true);
        createCalendarView.addCreateCalendarListener(e -> createCalendar());

    }

    public void setupShareView() {

        shareView = new ShareView();
        shareView.setVisible(true);
        shareView.addShareButtonListener(e -> shareCalendar());

    }

    public void setupCreateEventWindow() {

        if (RBAC.canCreateEvent(getCurrentCalendar().getPermission())) {
            cwView = new EditEventView();
            cwView.setVisible(true);
            cwView.addCreateEventListener(e -> editEvent(java.util.UUID.randomUUID().toString().substring(0, 19)));
            cwView.addCalendarPressListener(new CalendarListener(cwView));
        }
        else {
            dialog = new view.Dialog.Builder().setLabel("You are not allowed!").setType(Dialog.type.ERROR).build();
            setupDialog();
        }

    }

    public void setupSearchWindow() {

            sView = new SearchView();
            sView.setVisible(true);
            sView.addSearchListener(e -> search());
            sView.addSelectedEventListener(e->{

                Appointment sel = (Appointment) sView.getList().getSelectedValue();
                mwView.getCalendar().setDate(sel.getStartTime());
                setupEventWindow(sel);
                sView.close();
            });

    }

    public void setupEditEventWindow(Appointment a){

        if (RBAC.canEditEvent(getCurrentCalendar().getPermission())) {
            cwView = new EditEventView();
            cwView.setVisible(true);
            cwView.addCalendarPressListener(new CalendarListener(cwView));

            cwView.setStartDate(a.getStartTime());
            cwView.setEndDate(a.getEndTime());
            cwView.setName(a.getHeaderText());
            cwView.setLocation(a.getLocation().getName());
            cwView.setDescr(a.getDescriptionText());
            cwView.setStartHour(a.getStartTime());
            cwView.setEndHour(a.getEndTime());
            cwView.addCreateEventListener(e->{
                gateway.deleteEvent(a.getId());
                editEvent(a.getId());
            });
            eventView.close();
        }
        else {
            dialog = new view.Dialog.Builder().setLabel("You are not allowed!").setType(Dialog.type.ERROR).build();
            setupDialog();
        }
    }



    public void setupRegisterWindow() {

        regView = new RegisterView();
        regView.setVisible(true);
        regView.addListener(e -> register());

    }

    public void setupEventWindow(Appointment a) {

        eventView = new EventDisplayView.Builder()
                .setName(a.getHeaderText())
                .setStartDate(a.getStartTime().toString())
                .setEndDate(a.getEndTime().toString())
                .setLocation(a.getLocation().getName())
                .setDescription(a.getDescriptionText())
                .build();
        eventView.setVisible(true);
        eventView.setId(a.getId());
        eventView.addDeleteButtonListener(e -> deleteEvent());
        eventView.addOkButtonListener(e -> eventView.close());
        eventView.addEditButtonListener(e->setupEditEventWindow(a));

    }


    public void setupMainWindow() {

        mwView = new MainView();
        mwView.setVisible(true);
        mwView.addNewEventListener(e -> setupCreateEventWindow());
        mwView.addMainCalendarListener(new MainCalendarAdapter());
        mwView.addLogoutListener(e -> {
            mwView.close();
            setupLoginWindow();
        });
        mwView.addRemoveUserListener(e-> {
            dialog = new Dialog.Builder().setLabel("<html>Are you sure?</html>")
                    .setType(Dialog.type.GENERIC).build();
            dialog.setVisible(true);
            dialog.addDialogListener(action->unsubscribeUser());
        });
        mwView.addNewCalendarListener(e -> setupCreateCalendarWindow());
        mwView.addSelectedCalendarListener(e -> loadEvents());
        mwView.addShareCalendarListener(e-> setupShareView());
        mwView.addFindListener(e -> setupSearchWindow());
        mwView.addRemoveCalendar(e->setupDeleteCalendarWindow());


    }

    public void deleteCalendar(model.Calendar calendar){

        if (RBAC.canDeleteCalendar(calendar.getPermission()))
            gateway.deleteCalendar(calendar);
        else
            gateway.unsubscribeCalendar(calendar,currentUser);
        mwView.deleteCalendar(deleteCalendarView.getCalendarList().getSelectedIndex());
        deleteCalendarView.deleteCalendar(deleteCalendarView.getCalendarList().getSelectedIndex());

        deleteCalendarView.getCalendarList().updateUI();



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

    public void setupDeleteCalendarWindow(){

        deleteCalendarView = new DeleteCalendarView();
        deleteCalendarView.setVisible(true);
        ArrayList<model.Calendar> calendarsList = currentUser.getCollection().getCalendars();
        for (model.Calendar cal: calendarsList) {
            deleteCalendarView.getDefaultListModel().add(0,cal);
        }
        deleteCalendarView.addDeleteCalendarListener(e->deleteCalendar((model.Calendar) deleteCalendarView.getCalendarList().getSelectedValue()));


    }

    public void register() {

        String newUser = regView.getUsername();
        String newPassword = regView.getPassword();

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newUser);



        if (newUser.length() == 0 || newPassword.length() == 0) {

            dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Empty field!").build();
        }
        else if(!matcher.matches()){

            dialog = new view.Dialog.Builder().setLabel("Invalid mail").setType(Dialog.type.ERROR).build();
        }
        else if (gateway.isExistingUsername(newUser)) {

            dialog = new view.Dialog.Builder().setLabel("Username already existing!").setType(Dialog.type.ERROR).build();

        } else {

            gateway.registerUser(newUser, newPassword);
            dialog = new view.Dialog.Builder().setType(Dialog.type.SUCCESS).setLabel("You have been registered!").build();
        }
        setupDialog();
        regView.close();
    }

    private void login(){

        String acquiredUser = logView.getUsername();
        String acquiredPassword = logView.getPassword();

        boolean check = gateway.isRegisteredUser(acquiredUser, acquiredPassword);
        if (check) {

            setupMainWindow();

            if (regView != null) {
                regView.close();
            }
            logView.close();

            currentUser = new User(acquiredUser);

            CalendarCollection cc = gateway.getUserCalendars(currentUser);
            currentUser.setCollection(cc);

            setCalendars(cc);
            DateTime start = DateTime.today();
            DateTime end = DateTime.today();
            end = end.addHours(23);
            end = end.addMinutes(59);
            ItemList itemList = mwView.getCalendar().getSchedule().getAllItems(start,end);
            String toSend = "";
            for (Item item:
                 itemList) {
                toSend = toSend+"\n"+item.getHeaderText()+" @ "+item.getStartTime().getHour()+" - "+item.getEndTime().getHour();
            }
            mailer.setMailParameters(toSend,currentUser.getUsername());
            mailer.start();

        } else {
            dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Wrong credentials!").build();
            setupDialog();
        }
    }

    public void setCalendars(CalendarCollection list) {
        ArrayList<model.Calendar> calendarsList = list.getCalendars();
        for (model.Calendar cal:
                calendarsList) {
            mwView.getCalendars().add(0,cal);
            mwView.refreshCalendarsDisplayed();
        }
    }


    public model.Calendar getCurrentCalendar(){
        return (model.Calendar)mwView.getCalendarList().getSelectedValue();
    }

    public void loadEvents() {
        if (getCurrentCalendar()!=null) {
            String calID = getCurrentCalendar().getId();
            model.Calendar calendar = currentUser.getCollection().getCalendar(calID);
            mwView.getCalendar().getSchedule().getAllItems().clear();

            Style style = new Style();
            style.setLineColor(Color.black);

            if (calendar!=null) {
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
                    appointment.setStyle(style);
                    if (event.getStartDate().getDay() < event.getEndDate().getDay())
                        appointment.setAllDayEvent(true);

                    mwView.getCalendar().getSchedule().getItems().add(appointment);
                }
            }
        }
    }

    private void search() {
        String toSearch = sView.getSearchText();
        ItemList itemList = mwView.getCalendar().getSchedule().getItems();

        SearchStrategy strategy = sView.getSearchType();
        ItemList items = strategy.search(itemList,toSearch);
        sView.addResults(items);


    }

    private void createCalendar(){
        String cid = java.util.UUID.randomUUID().toString().substring(0, 19);
        model.Calendar newCalendar = null;
        if (!createCalendarView.getName().isEmpty()) {
            newCalendar = new model.Calendar(cid, createCalendarView.getName(), RBAC.getCreatorPermission());
            gateway.createCalendar(newCalendar, currentUser);
            dialog = new view.Dialog.Builder().setLabel("Calendar Created").setType(Dialog.type.SUCCESS).build();
        } else {
            dialog = new Dialog.Builder().setLabel("Calendar can't be created").setType(Dialog.type.ERROR).build();
        }
        setupDialog();
        createCalendarView.close();
        if (newCalendar!=null) {
            mwView.getCalendars().add(0,newCalendar);
            mwView.refreshCalendarsDisplayed();
            CalendarCollection cal = currentUser.getCollection();
            cal.addCalendar(newCalendar);
            loadEvents();
        }
    }

    private void shareCalendar(){
        model.Calendar calendar = getCurrentCalendar();
        String username = shareView.getUsername();
        if (!shareView.getName().isEmpty() && gateway.isExistingUsername(username)){
            gateway.shareCalendar(calendar, username, shareView.getPermission());
            dialog = new view.Dialog.Builder().setLabel("Calendar Shared!").setType(Dialog.type.SUCCESS).build();
        } else {
            dialog = new Dialog.Builder().setLabel("Calendar could NOT be shared!").setType(Dialog.type.ERROR).build();
        }
        setupDialog();
        shareView.close();
    }

    private void editEvent(String id){

        if (!cwView.getName().isEmpty() && !id.isEmpty() && !cwView.startDateisEmpty() && !cwView.endDateisEmpty()) {

            model.Calendar calendar = getCurrentCalendar();

            String name = cwView.getName();
            String location = cwView.getLocationName();
            String descr = cwView.getDescriptionText();
            DateTime startDate = cwView.getStartDate();
            DateTime endDate = cwView.getEndDate();

            DateTime startHour = cwView.getStartHour();
            DateTime endHour = cwView.getEndHour();

            startDate = startDate.addTicks(startHour.getTicks());
            endDate = endDate.addTicks(endHour.getTicks());

            if (startDate.isLessThan(endDate)) {

                dialog = new view.Dialog.Builder().setType(Dialog.type.SUCCESS).setLabel("Event saved!").build();
                model.Event event = new model.Event(id, name, startDate, endDate, location, descr);
                gateway.addEvent(event, calendar.getId());
                currentUser.setCollection(gateway.getUserCalendars(currentUser));
                mwView.getCalendar().getSchedule().getAllItems().clear();
                loadEvents();
                cwView.close();

            } else {
                dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Inconsistent dates!").build();
            }
        }
        else {
            dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Fill missing data!").build();
        }

        setupDialog();
    }


    public void deleteEvent(){
        if (RBAC.canDeleteEvent(getCurrentCalendar().getPermission())) {
            Appointment appointment = (Appointment) mwView.getCalendar().getSchedule().getItems().get(eventView.getId());
            gateway.deleteEvent(appointment.getId());
            mwView.getCalendar().getSchedule().getItems().remove(appointment);
            eventView.close();
        }
        else {
            dialog = new view.Dialog.Builder().setType(Dialog.type.ERROR).setLabel("You are not allowed!").build();
            setupDialog();
        }
    }

    class MainCalendarAdapter extends CalendarAdapter {

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
            if (calendar.getCurrentView() == CalendarView.SingleMonth || calendar.getCurrentView() == CalendarView.MonthRange) {
                calendar.setCurrentView(CalendarView.Timetable);
                calendar.setDate(e.getDate());
                mwView.getViewMenu().setSelectedIndex(0);
            }
        }
    }

    public void unsubscribeUser(){

        dialog.close();
        mwView.close();
        gateway.deleteUser(currentUser);
        setupLoginWindow();

    }

    public MainView getMwView() {
        return mwView;
    }

    public SearchView getsView() {
        return sView;
    }

    public Login getLogView() {
        return logView;
    }

    public RegisterView getRegView() {
        return regView;
    }

    public ShareView getShareView() {
        return shareView;
    }

    public EditEventView getCwView() {
        return cwView;
    }

    public Dialog getDialog() {
        return dialog;
    }


}



