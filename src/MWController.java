import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.*;
import com.mindfusion.scheduling.model.*;

import javax.swing.*;
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

    MainWindow mwView;
    CalendarWindow cwView;
    Database model;
    Dialog dialog;
    Login logView;
    Register regView;
    EventDisplayWindow eventView;
    CreateCalendarWindow createCalendarWindow;


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

    class addEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            cwView = new CalendarWindow();
            cwView.setVisible(true);
            cwView.populateCalendars(model.getCurrentUserCalendars());
            attachCalendarWindow();

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
                dialog = new Dialog.Builder().setDialogTitle("Register Problem")
                        .setLabel("Username already present")
                        .setColor(Color.red)
                        .build();
                dialog.setVisible(true);
                dialog.addDialogListener(new dialogListener());
            }


        }
    }

    class changeViewListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String selection = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();

            if (selection.equals("day"))
                mwView.calendar.setCurrentView(CalendarView.Timetable);
            else if (selection.equals("week")) {
                mwView.calendar.setCurrentView(CalendarView.WeekRange);
                mwView.calendar.getWeekRangeSettings().setHeaderStyle(EnumSet.of(WeekRangeHeaderStyle.Title));
            } else if (selection.equals("month"))
                mwView.calendar.setCurrentView(CalendarView.MonthRange);


        }

    }

    class createEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String uid = java.util.UUID.randomUUID().toString().substring(0, 19);
            String name = cwView.name.getText();
            String location = cwView.location.getText();
            String descr = cwView.descr.getText();
            String startdate = cwView.startDate.getText();
            String enddate = cwView.endDate.getText();

            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
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

            java.util.Calendar calendar1 = new GregorianCalendar();
            calendar1.setTime(date1);
            java.util.Calendar calendar2 = new GregorianCalendar();
            calendar2.setTime(date2);

            DateTime startDate = new DateTime(calendar1.get(java.util.Calendar.YEAR) - 1900, calendar1.get(java.util.Calendar.MONTH) + 1, calendar1.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(startHour.substring(0, 2)), Integer.parseInt(startHour.substring(3, 5)), 0);//TODO: PROBLEMI QUANDO NON SI SELEZIONA LA DATA
            DateTime endDate = new DateTime(calendar2.get(java.util.Calendar.YEAR) - 1900, calendar2.get(java.util.Calendar.MONTH) + 1, calendar2.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(endHour.substring(0, 2)), Integer.parseInt(endHour.substring(3, 5)), 0); //TODO:COLLEZIONARE GLI ORARI DAI RISPETTIVI TEXTFIELD
            if (!name.isEmpty() && !uid.isEmpty() && !startDate.toString().isEmpty() && !endDate.toString().isEmpty()) {
                dialog = new Dialog.Builder().setColor(Color.green).setLabel("Event Created!").setDialogTitle("Event").build();
                dialog.setVisible(true);
                dialog.addDialogListener(new dialogListener());
            } else {
                dialog = new Dialog.Builder().setColor(Color.red).setLabel("Check null values").setDialogTitle("Event").build();
                dialog.setVisible(true);
                dialog.addDialogListener(new dialogListener());
            }

            Event event = new Event(uid, name, startDate, endDate, location, descr);
            model.addEventinEvents(event, cwView.getCurrentCalendar().getId());
            Item appointment = new Appointment();
            appointment.setStartTime(new DateTime(calendar1.get(java.util.Calendar.YEAR), calendar1.get(java.util.Calendar.MONTH) + 1, calendar1.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(startHour.substring(0, 2)), Integer.parseInt(startHour.substring(3, 5)), 00));
            appointment.setEndTime(new DateTime(calendar2.get(java.util.Calendar.YEAR), calendar2.get(java.util.Calendar.MONTH) + 1, calendar2.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(endHour.substring(0, 2)), Integer.parseInt(endHour.substring(3, 5)), 00));
            appointment.setHeaderText(name);
            appointment.setId(uid);
            appointment.setDescriptionText(descr);
            Location loc = new Location();
            loc.setName(location);
            appointment.setLocation(loc);
            CalendarCollection cc = new CalendarCollection();
            cc.addCalendarToCollection(mwView.getCurrentCalendar());
            mwView.calendar.getSchedule().getAllItems().clear();
            loadView(cc);
            cwView.setVisible(false);
            cwView.dispose();


        }
    }

    class calendarinCalendarWindowPressedListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2) {

                cwView.cal.getSelection().reset();

                DateTime pointedDate = cwView.cal.getDateAt(e.getX(), e.getY());

                java.util.Calendar cal = java.util.Calendar.getInstance();
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

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
            Date utilDate = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            cwView.setSelectedStartDate(sqlDate);

        }

        @Override
        public void mouseReleased(MouseEvent e) {

            cwView.cal.getSelection().reset();

            DateTime pointedDate = cwView.cal.getDateAt(e.getX(), e.getY());

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
            Date utilDate = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            if (cwView.startDate.getText().compareTo(sqlDate.toString()) > 0) {
                java.sql.Date d = java.sql.Date.valueOf(cwView.startDate.getText());
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

    class logoutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            mwView.setVisible(false);
            mwView.dispose();
            logView = new Login();
            logView.setVisible(true);
            attachLoginWindow();

        }
    }

    class dialogListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            dialog.setVisible(false);
            dialog.dispose();

        }
    }

    class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

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
                    model.setCurrentUser(user);
                    loadView(model.getCurrentUserCalendars());
                    mwView.populateCalendars(model.getCurrentUserCalendars());
                    dialog = new Dialog.Builder().setDialogTitle("LoginSuccessful!")
                            .setColor(Color.green)
                            .setLabel("You are logged in!")
                            .build();

                } else {
                    dialog = new Dialog.Builder().setDialogTitle("Access denied")
                            .setLabel("Your username or password is wrong")
                            .setColor(Color.red)
                            .build();
                }
                dialog.setVisible(true);
                dialog.addDialogListener(new dialogListener());


            } catch (SQLException ex) {
                dialog = new Dialog.Builder().setDialogTitle("Access denied")
                        .setLabel("Your username or password is wrong")
                        .setColor(Color.red)
                        .build();
                dialog.setVisible(true);
                dialog.addDialogListener(new dialogListener());
            }


        }
    }

    public void loadView(CalendarCollection calendars) {
        ArrayList<Event> events = calendars.getEvents();
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

    class RegisterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //regView.setVisible(true);
            regView = new Register();                     //TODO: LO SWITCH TRA VISTE DOVREBBE ESSERE GESTITO COSì,SENZA VISTE GLOBALI
            regView.setVisible(true);
            attachRegisterWindow();

        }
    }

    class RegViewListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String newUser = regView.getUsername();
            String newPassword = regView.getPassword();

            if (newUser.length() == 0 || newPassword.length() == 0) {

                dialog = new Dialog.Builder().setDialogTitle("Register Problem")
                        .setLabel("Username or password fields are empty")
                        .setColor(Color.red)
                        .build();
                dialog.setVisible(true);
                dialog.addDialogListener(new dialogListener());
            } else if (model.isExistingUsername(newUser)) {

                dialog = new Dialog.Builder().setDialogTitle("Register Problem")
                        .setLabel("Username already present")
                        .setColor(Color.red)
                        .build();
                dialog.setVisible(true);
                dialog.addDialogListener(new dialogListener());
            } else {

                model.registerNewUser(newUser, newPassword);
                dialog = new Dialog.Builder().setDialogTitle("Successfull sign up!")
                        .setLabel("You have been registered on our system!")
                        .setColor(Color.green)
                        .build();
                dialog.setVisible(true);
                dialog.addDialogListener(new dialogListener());
            }


        }
    }

    class createCalendarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String cid = java.util.UUID.randomUUID().toString().substring(0, 19);
            if (!createCalendarWindow.getName().isEmpty()) {
                Calendar newCalendar = new Calendar(model.getCurrentUser(), cid, createCalendarWindow.getName());
                model.CreateCalendar(newCalendar);
                model.getCurrentUserCalendars().addCalendarToCollection(newCalendar);
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

    class createCalendarButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            createCalendarWindow = new CreateCalendarWindow();
            createCalendarWindow.setVisible(true);
            createCalendarWindow.addCreateCalendarListener(new createCalendarListener());

        }
    }

    class okButtonForEventDisplayWindowListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            eventView.setVisible(false);
            eventView.dispose();

        }
    }

    class deleteButtonForEventDisplayWindowListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {


            Appointment appointment = (Appointment) mwView.calendar.getSchedule().getItems().get(eventView.getTitle());
            model.deleteEvent(appointment.getId());
            mwView.calendar.getSchedule().getItems().remove(appointment);
            mwView.calendar.repaint();
            eventView.setVisible(false);
            eventView.dispose();

            //TODO:     INTERESSANTE TROVARE UNA SOLUZIONE ALTERNATIVA PER ACQUISIRE ID

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
        class selectedCalendarListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {

                mwView.calendar.getSchedule().getAllItems().clear();
                CalendarCollection cc = new CalendarCollection();
                cc.addCalendarToCollection(mwView.getCurrentCalendar());
                loadView(cc);

            }
        }
    }


