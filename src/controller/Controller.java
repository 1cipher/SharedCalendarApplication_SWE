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
    private EditEventView editEventView;
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
        setupLoginView();
    }

    public void setupCreateCalendarWindow() {     //setup createCalendarView attaching its listener

        createCalendarView = new CreateCalendarView();
        createCalendarView.setVisible(true);
        createCalendarView.addCreateCalendarListener(e -> createCalendar());

    }

    public void setupShareView() {   //setup shareView attaching its listener

        shareView = new ShareView();
        shareView.setVisible(true);
        shareView.addShareButtonListener(e -> shareCalendar());

    }

    public void setupCreateEventWindow() {       //setup editEventView attaching its listener

        if (RBAC.canCreateEvent(getCurrentCalendar().getPermission())) {
            editEventView = new EditEventView();
            editEventView.setVisible(true);
            editEventView.addCreateEventListener(e -> editEvent(java.util.UUID.randomUUID().toString().substring(0, 19)));
            editEventView.addCalendarPressListener(new CalendarListener(editEventView));
        }
        else {
            dialog = new Dialog.Builder().setLabel("You are not allowed!").setType(Dialog.type.ERROR).build();
            setupDialog();
        }

    }

    public void setupSearchView() {  //setup searchView attaching its listener

            sView = new SearchView();
            sView.setVisible(true);
            sView.addSearchListener(e -> search());
            sView.addSelectedEventListener(e->{

                Appointment sel = (Appointment) sView.getList().getSelectedValue();
                mwView.getCalendar().setDate(sel.getStartTime());
                setupEventView(sel);
                sView.close();
            });

    }

    public void setupEditEventView(Appointment a){   ////setup editEventView attaching its listener

        if (RBAC.canEditEvent(getCurrentCalendar().getPermission())) {
            editEventView = new EditEventView();
            editEventView.setVisible(true);
            editEventView.addCalendarPressListener(new CalendarListener(editEventView));

            editEventView.setStartDate(a.getStartTime());
            editEventView.setEndDate(a.getEndTime());
            editEventView.setName(a.getHeaderText());
            editEventView.setLocation(a.getLocation().getName());
            editEventView.setDescr(a.getDescriptionText());
            editEventView.setStartHour(a.getStartTime());
            editEventView.setEndHour(a.getEndTime());
            editEventView.addCreateEventListener(e->{
                gateway.deleteEvent(a.getId());
                editEvent(a.getId());
            });
            eventView.close();
        }
        else {
            dialog = new Dialog.Builder().setLabel("You are not allowed!").setType(Dialog.type.ERROR).build();
            setupDialog();
        }
    }



    public void setupRegisterView() {  //setup regView attaching its listener

        regView = new RegisterView();
        regView.setVisible(true);
        regView.addListener(e -> register());

    }

    public void setupEventView(Appointment a) {  //setup eventView attaching its listener

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
        eventView.addEditButtonListener(e->setupEditEventView(a));

    }


    public void setupMainWindowView() {  //setup mwView attaching its listener

        mwView = new MainView();
        mwView.setVisible(true);
        mwView.addNewEventListener(e -> setupCreateEventWindow());
        mwView.addMainCalendarListener(new MainCalendarAdapter());
        mwView.addLogoutListener(e -> {
            mwView.close();
            setupLoginView();
        });
        mwView.addRemoveUserListener(e-> {
            dialog = new Dialog.Builder().setLabel("Are you sure?")
                    .setType(Dialog.type.GENERIC).build();
            dialog.setVisible(true);
            dialog.addDialogListener(action->unsubscribeUser());
        });
        mwView.addNewCalendarListener(e -> setupCreateCalendarWindow());
        mwView.addSelectedCalendarListener(e -> loadEvents());
        mwView.addShareCalendarListener(e-> setupShareView());
        mwView.addFindListener(e -> setupSearchView());
        mwView.addRemoveCalendar(e-> setupDeleteCalendarView());


    }

    public void deleteCalendar(model.Calendar calendar){    //takes a calendar and call gateway in order to delete it;if has permission

        if (RBAC.canDeleteCalendar(calendar.getPermission()))
            gateway.deleteCalendar(calendar);
        else
            gateway.unsubscribeCalendar(calendar,currentUser);
        mwView.deleteCalendar(deleteCalendarView.getCalendarList().getSelectedIndex());
        deleteCalendarView.deleteCalendar(deleteCalendarView.getCalendarList().getSelectedIndex());

        deleteCalendarView.getCalendarList().updateUI();



    }

    public void setupLoginView() {  //setup logView attaching its listener

        logView = new Login();
        logView.setVisible(true);
        logView.addLoginListener(e -> login());
        logView.addRegisterListener(e -> setupRegisterView());

    }

    public void setupDialog(){   //setup dialog attaching its listener

        dialog.setVisible(true);
        dialog.addDialogListener(e->dialog.close());

    }

    public void setupDeleteCalendarView(){   //setup deleteCalendarView attaching its listener

        deleteCalendarView = new DeleteCalendarView();
        deleteCalendarView.setVisible(true);
        ArrayList<model.Calendar> calendarsList = currentUser.getCollection().getCalendars();
        for (model.Calendar cal: calendarsList) {
            deleteCalendarView.getDefaultListModel().add(0,cal);
        }
        deleteCalendarView.addDeleteCalendarListener(e->deleteCalendar((model.Calendar) deleteCalendarView.getCalendarList().getSelectedValue()));


    }

    public void register() {     //get the data from regView and register a new user: makes the gateway register on database

        String newUser = regView.getUsername();
        String newPassword = regView.getPassword();

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newUser);



        if (newUser.length() == 0 || newPassword.length() == 0) {

            dialog = new Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Empty field!").build();
        }
        else if(!matcher.matches()){

            dialog = new Dialog.Builder().setLabel("Invalid mail").setType(Dialog.type.ERROR).build();
        }
        else if (gateway.isExistingUsername(newUser)) {

            dialog = new Dialog.Builder().setLabel("Username already existing!").setType(Dialog.type.ERROR).build();

        } else {

            gateway.registerUser(newUser, newPassword);
            dialog = new Dialog.Builder().setType(Dialog.type.SUCCESS).setLabel("You have been registered!").build();
        }
        setupDialog();
        regView.close();
    }

    private void login(){   //login function: gateway check if correct data are inserted

        String acquiredUser = logView.getUsername();
        String acquiredPassword = logView.getPassword();

        boolean check = gateway.isRegisteredUser(acquiredUser, acquiredPassword);
        if (check) {

            setupMainWindowView();

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
            if(!toSend.isEmpty()) {
                mailer.setMailParameters(toSend, currentUser.getUsername());
                mailer.start();
            }

        } else {
            dialog = new Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Wrong credentials!").build();
            setupDialog();
        }
    }

    public void setCalendars(CalendarCollection list) {       //refresh the display of calendar list
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

    public void loadEvents() {    //gateway retrieve calendars and events associated with user and then add them to mwView to display
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

    private void search() {     //search using the correct strategy algorithm
        String toSearch = sView.getSearchText();
        ItemList itemList = mwView.getCalendar().getSchedule().getItems();

        SearchStrategy strategy = sView.getSearchType();
        ItemList items = strategy.search(itemList,toSearch);
        sView.addResults(items);


    }

    private void createCalendar(){    //demand the gateway in order to register a new calendar associated with user
        String cid = java.util.UUID.randomUUID().toString().substring(0, 19);
        model.Calendar newCalendar = null;
        if (!createCalendarView.getName().isEmpty()) {
            newCalendar = new model.Calendar(cid, createCalendarView.getName(), RBAC.getCreatorPermission());
            gateway.createCalendar(newCalendar, currentUser);
            dialog = new Dialog.Builder().setLabel("Calendar Created").setType(Dialog.type.SUCCESS).build();
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

    private void shareCalendar(){      //check if user to share with exists,then if true associate this calendar with selected permission to him
        model.Calendar calendar = getCurrentCalendar();
        String username = shareView.getUsername();
        if (!shareView.getName().isEmpty() && gateway.isExistingUsername(username)){
            gateway.shareCalendar(calendar, username, shareView.getPermission());
            dialog = new Dialog.Builder().setLabel("Calendar Shared!").setType(Dialog.type.SUCCESS).build();
        } else {
            dialog = new Dialog.Builder().setLabel("Calendar could NOT be shared!").setType(Dialog.type.ERROR).build();
        }
        setupDialog();
        shareView.close();
    }

    private void editEvent(String id){   //makes possible to edit and create an event,retrieving information from GUI and then making the gateway
                                        //to register on database

        if (!editEventView.getName().isEmpty() && !id.isEmpty() && !editEventView.startDateisEmpty() && !editEventView.endDateisEmpty()) {

            model.Calendar calendar = getCurrentCalendar();

            String name = editEventView.getName();
            String location = editEventView.getLocationName();
            String descr = editEventView.getDescriptionText();
            DateTime startDate = editEventView.getStartDate();
            DateTime endDate = editEventView.getEndDate();

            DateTime startHour = editEventView.getStartHour();
            DateTime endHour = editEventView.getEndHour();

            startDate = startDate.addTicks(startHour.getTicks());
            endDate = endDate.addTicks(endHour.getTicks());

            if (startDate.isLessThan(endDate)) {

                dialog = new Dialog.Builder().setType(Dialog.type.SUCCESS).setLabel("Event saved!").build();
                model.Event event = new model.Event(id, name, startDate, endDate, location, descr);
                gateway.addEvent(event, calendar.getId());
                currentUser.setCollection(gateway.getUserCalendars(currentUser));
                mwView.getCalendar().getSchedule().getAllItems().clear();
                loadEvents();
                editEventView.close();

            } else {
                dialog = new Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Inconsistent dates!").build();
            }
        }
        else {
            dialog = new Dialog.Builder().setType(Dialog.type.ERROR).setLabel("Fill missing data!").build();
        }

        setupDialog();
    }


    public void deleteEvent(){  //gets the event to delete and then leaves the responsibility to gateway
        if (RBAC.canDeleteEvent(getCurrentCalendar().getPermission())) {
            Appointment appointment = (Appointment) mwView.getCalendar().getSchedule().getItems().get(eventView.getId());
            gateway.deleteEvent(appointment.getId());
            mwView.getCalendar().getSchedule().getItems().remove(appointment);
            eventView.close();
        }
        else {
            dialog = new Dialog.Builder().setType(Dialog.type.ERROR).setLabel("You are not allowed!").build();
            setupDialog();
        }
    }

    class MainCalendarAdapter extends CalendarAdapter {    //handles the clicks on calendar

        @Override
        public void itemClick(ItemMouseEvent e) {

            if (eventView != null) {
                eventView.close();
            }
            Appointment a = (Appointment) e.getItem();
            setupEventView(a);

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

    public void unsubscribeUser(){  //delete user permanently

        dialog.close();
        mwView.close();
        gateway.deleteUser(currentUser);
        setupLoginView();

    }

    public MainView getMwView() {
        return mwView;
    }

    public Login getLogView() {
        return logView;
    }

    public EditEventView getEditEventView() {
        return editEventView;
    }

    public Dialog getDialog() {
        return dialog;
    }


}



