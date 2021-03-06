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

    public MWController(Login lview, Gateway db) {

        this.model = db;
        this.logView = lview;
        attachLoginWindow();

    }

    public void attachCreateCalendarWindow() {

        this.createCalendarWindow.addCreateCalendarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCalendar();
                createCalendarWindow.setVisible(false);
                createCalendarWindow.dispose();
            }
        });
    }

    public void attachMainWindow() {

        this.mwView.addChangeViewListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mwView.changeView();
            }
        });
        this.mwView.addAddEventListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {

                cwView = new CalendarWindow();
                cwView.setVisible(true);
                cwView.setCalendars(model.getUserCalendars(currentUser));
                attachCalendarWindow();

            }
        });
        this.mwView.addSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        this.mwView.addMainCalendarListener(new mainCalendarAdapter());
        this.mwView.addLogoutListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mwView.setVisible(false);
                mwView.dispose();
                logView = new Login();
                logView.setVisible(true);
                attachLoginWindow();
            }
        });
        this.mwView.addCreateCalendarButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCalendarWindow = new CreateCalendarWindow();
                createCalendarWindow.setVisible(true);
                createCalendarWindow.addCreateCalendarListener(new CreateCalendarListener());
            }
        });
        this.mwView.addSelectedCalendarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mwView.getCalendar().getSchedule().getAllItems().clear();
                CalendarCollection cc = new CalendarCollection();
                cc.addCalendarToCollection(mwView.getCurrentCalendar());
                loadView(cc);
            }
        });

    }

    public void attachCalendarWindow() {

        this.cwView.addCreateEventListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {

                createEvent();
                CalendarCollection cc = new CalendarCollection();
                cc.addCalendarToCollection(mwView.getCurrentCalendar());
                mwView.getCalendar().getSchedule().getAllItems().clear();
                loadView(cc);
                cwView.setVisible(false);
                cwView.dispose();
                //TODO: sei un po' cattivo

            }
        });
        this.cwView.addCalendarPressListener(new CalendarinCalendarWindowPressedListener());

    }

    public void attachLoginWindow() {

        this.logView.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        this.logView.addRegisterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //regView.setVisible(true);
                regView = new Register();                     //TODO: LO SWITCH TRA VISTE DOVREBBE ESSERE GESTITO COSì,SENZA VISTE GLOBALI
                regView.setVisible(true);
                attachRegisterWindow();
            }
        });
    }

    public void attachRegisterWindow() {

        this.regView.addListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
                dialog.setVisible(true);
                dialog.addDialogListener(new DialogListener());
            }
        });
    }

    public void attachEventDisplayWindow() {

        this.eventView.addDeleteButtonForEventDisplayWindowListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Appointment appointment = (Appointment) mwView.getCalendar().getSchedule().getItems().get(eventView.getTitle());
                model.deleteEvent(appointment.getId());
                mwView.getCalendar().getSchedule().getItems().remove(appointment);
                mwView.getCalendar().repaint();
                eventView.setVisible(false);
                eventView.dispose();
            }
        });
        this.eventView.addOkButtonForEventDisplayWindowListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventView.setVisible(false);
                eventView.dispose();
            }
        });
    }

    public void register() {

        String newUser = regView.getUsername();
        String newPassword = regView.getPassword();

        if (newUser.length() == 0 || newPassword.length() == 0) {

            dialog = new view.Dialog.Builder().setDialogTitle("view.Register Problem")
                    .setLabel("Username or password fields are empty")
                    .setColor(Color.red)
                    .build();
        } else if (model.isExistingUsername(newUser)) {

            dialog = new view.Dialog.Builder().setDialogTitle("view.Register Problem")
                    .setLabel("Username already present")
                    .setColor(Color.red)
                    .build();

        } else {

            model.registerNewUser(newUser, newPassword);
            dialog = new view.Dialog.Builder().setDialogTitle("Successfull sign up!")
                    .setLabel("You have been registered on our system!")
                    .setColor(Color.green)
                    .build();
        }
    }

    public void loadView(CalendarCollection calendars) {
        ArrayList<model.Event> events = calendars.getEvents();
        for (model.Event event :
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
            mwView.getCalendar().getSchedule().getItems().add(appointment);
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
            Calendar calendar = mwView.getCalendar();
            if (calendar.getCurrentView() == CalendarView.WeekRange) {
                calendar.setCurrentView(CalendarView.Timetable);
                calendar.setDate(e.getDate());
                mwView.getViewMenu().setSelectedIndex(0);
            }
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
            dialog = new view.Dialog.Builder().setDialogTitle("view.Register Problem")
                    .setLabel("No event found!")
                    .setColor(Color.red)
                    .build();
            dialog.setVisible(true);
            dialog.addDialogListener(new DialogListener());
        }
    }

    private void createCalendar(){
        String cid = java.util.UUID.randomUUID().toString().substring(0, 19);
        if (!createCalendarWindow.getName().isEmpty()) {
            model.Calendar newCalendar = new model.Calendar(currentUser, cid, createCalendarWindow.getName());
            model.CreateCalendar(newCalendar, currentUser);
            model.getUserCalendars(currentUser).addCalendarToCollection(newCalendar);
            dialog = new view.Dialog.Builder().setDialogTitle("model.Calendar").setLabel("model.Calendar Created").setColor(Color.green).build();
            dialog.setVisible(true);
        } else {
            dialog = new Dialog.Builder().setDialogTitle("model.Calendar").setLabel("model.Calendar can't be created").setColor(Color.red).build();
            dialog.setVisible(true);
        }
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

        java.util.Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(date1);
        java.util.Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(date2);

        DateTime startDate = new DateTime(calendar1.get(java.util.Calendar.YEAR) - 1900, calendar1.get(java.util.Calendar.MONTH) + 1, calendar1.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(startHour.substring(0, 2)), Integer.parseInt(startHour.substring(3, 5)), 0);//TODO: PROBLEMI QUANDO NON SI SELEZIONA LA DATA
        DateTime endDate = new DateTime(calendar2.get(java.util.Calendar.YEAR) - 1900, calendar2.get(java.util.Calendar.MONTH) + 1, calendar2.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(endHour.substring(0, 2)), Integer.parseInt(endHour.substring(3, 5)), 0); //TODO:COLLEZIONARE GLI ORARI DAI RISPETTIVI TEXTFIELD
        if (!name.isEmpty() && !uid.isEmpty() && !startDate.toString().isEmpty() && !endDate.toString().isEmpty()) {
            dialog = new view.Dialog.Builder().setColor(Color.green).setLabel("model.Event Created!").setDialogTitle("model.Event").build();
            dialog.setVisible(true);
            dialog.addDialogListener(new DialogListener());
        } else {
            dialog = new view.Dialog.Builder().setColor(Color.red).setLabel("Check null values").setDialogTitle("model.Event").build();
            dialog.setVisible(true);
            dialog.addDialogListener(new DialogListener());
        }

        model.Event event = new model.Event(uid, name, startDate, endDate, location, descr);
        /*Item appointment = new Appointment();
        appointment.setStartTime(new DateTime(calendar1.get(java.util.Calendar.YEAR), calendar1.get(java.util.Calendar.MONTH) + 1, calendar1.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(startHour.substring(0, 2)), Integer.parseInt(startHour.substring(3, 5)), 00));
        appointment.setEndTime(new DateTime(calendar2.get(java.util.Calendar.YEAR), calendar2.get(java.util.Calendar.MONTH) + 1, calendar2.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(endHour.substring(0, 2)), Integer.parseInt(endHour.substring(3, 5)), 00));
        appointment.setHeaderText(name);
        appointment.setId(uid);
        appointment.setDescriptionText(descr);
        Location loc = new Location();
        loc.setName(location);
        appointment.setLocation(loc);*/
        model.addEventinEvents(event, cwView.getCurrentCalendar().getId());
        currentUser.getCollection().getCalendar(cwView.getCurrentCalendar().getId()).addtoCalendar(event);
    }


    private void login(){
        String acquiredUser = logView.getUsername();
        String acquiredPassword = logView.getPassword();
        try {
            boolean check = model.checkUserPresence(acquiredUser, acquiredPassword);
            if (check) {

                mwView = new MainWindow();
                mwView.setVisible(true);
                attachMainWindow();
                if (regView != null) {

                    regView.setVisible(false);
                    regView.dispose();
                }
                logView.setVisible(false);
                logView.dispose();

                User user = new User(acquiredUser);
                currentUser = user;
                CalendarCollection cc = model.getUserCalendars(currentUser);
                loadView(cc);
                mwView.setCalendars(cc);
                dialog = new view.Dialog.Builder().setDialogTitle("LoginSuccessful!")
                        .setColor(Color.green)
                        .setLabel("You are logged in!")
                        .build();

            } else {
                dialog = new view.Dialog.Builder().setDialogTitle("Access denied")
                        .setLabel("Your username or password is wrong")
                        .setColor(Color.red)
                        .build();
            }
            dialog.setVisible(true);
            dialog.addDialogListener(new DialogListener());


        } catch (SQLException ex) {
            dialog = new view.Dialog.Builder().setDialogTitle("Access denied")
                    .setLabel("Your username or password is wrong")
                    .setColor(Color.red)
                    .build();
            dialog.setVisible(true);
            dialog.addDialogListener(new DialogListener());
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

    class DialogListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.setVisible(false);
            dialog.dispose();

        }
    }

    class CreateCalendarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String cid = java.util.UUID.randomUUID().toString().substring(0, 19);
            if (!createCalendarWindow.getName().isEmpty()) {
                model.Calendar newCalendar = new model.Calendar(currentUser, cid, createCalendarWindow.getName());
                model.CreateCalendar(newCalendar,currentUser);
                model.getUserCalendars(currentUser).addCalendarToCollection(newCalendar);
                dialog = new Dialog.Builder().setDialogTitle("Calendar").setLabel("Calendar Created").setColor(Color.green).build();
                dialog.setVisible(true);
            } else {
                dialog = new Dialog.Builder().setDialogTitle("Calendar").setLabel("Calendar can't be created").setColor(Color.red).build();
                dialog.setVisible(true);
            }

            createCalendarWindow.setVisible(false);
            createCalendarWindow.dispose();
        }
    }
}


