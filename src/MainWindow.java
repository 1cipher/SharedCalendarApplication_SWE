import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.*;
import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.model.Appointment;
import com.mindfusion.scheduling.model.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;


public class MainWindow extends JFrame {

    JFormattedTextField textField = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
    Calendar calendar;
    JButton search;
    JButton addEvent;
    JButton logout;
    JTextField searchBox;
    JComboBox<String> viewMenu;
    JComboBox<String> calendarMenu;
    JCheckBox deleteSelector;



    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(368, 362);
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Calendar");

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        viewMenu = new JComboBox<>();
        viewMenu.addItem("day");
        viewMenu.addItem("week");
        viewMenu.addItem("month");
        viewMenu.setLocation(550, 10);
        viewMenu.setSize(100, 20);

        logout = new JButton("Logout");
        logout.setLocation(800, 10);
        logout.setSize(100, 20);


        calendar = new Calendar();
        calendar.beginInit();
        calendar.setCurrentView(CalendarView.Timetable);
        calendar.setTheme(ThemeType.Light);
        calendar.setLocation(0, 40);
        calendar.setSize(1000, 1000);
        calendar.setVisible(true);
        calendar.endInit();

        addEvent = new JButton();
        addEvent.setText("Create Event");
        addEvent.setLocation(0, 10);
        addEvent.setSize(200, 20);


        searchBox = new JTextField();
        searchBox.setLocation(210, 10);
        searchBox.setSize(300, 20);

        search = new JButton(new ImageIcon("C:\\search.png"));
        search.setLocation(520, 10);
        search.setSize(20, 20);

        calendar.setEnableDragCreate(true);

        deleteSelector = new JCheckBox();
        deleteSelector.setText("Delete");
        deleteSelector.setLocation(670, 10);
        deleteSelector.setSize(150, 20);

        cp.add(calendar);
        cp.add(addEvent);
        cp.add(searchBox);
        cp.add(search);
        cp.add(viewMenu);
        cp.add(logout);
        cp.add(deleteSelector);

    }

    public void loadView(CalendarCollection calendars){
        ArrayList<Event> events = calendars.getEvents();
        for (Event event:
             events) {
            Item appointment = new Appointment();
            DateTime start = event.getStartDate();
            DateTime end = event.getEndDate();
            appointment.setStartTime(new DateTime(start.getYear(), start.getMonth(), start.getDay(), start.getHour(), start.getMinute(), 0));
            appointment.setEndTime(new DateTime(end.getYear(), end.getMonth(),end.getDay(),end.getHour(),end.getMinute(),0));
            this.calendar.getSchedule().getItems().add(appointment);
        }
        //TODO: c'è uguale in MWcontroller
    }

    public void addSearchListener(ActionListener searchListener){

        this.search.addActionListener(searchListener);
    }


    public void addAddEventListener(ActionListener addEventListener){

        this.addEvent.addActionListener(addEventListener);
    }

   public void addChangeViewListener(ActionListener changeViewListener){

        this.viewMenu.addActionListener(changeViewListener);
    }

    public void addLogoutListener(ActionListener logoutListener){

        this.logout.addActionListener(logoutListener);
    }

    public void addMainCalendarListener(CalendarAdapter mainCalendarAdapter ){

        calendar.addCalendarListener(mainCalendarAdapter);
    }


}
