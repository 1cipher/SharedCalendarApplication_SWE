package view;

import com.mindfusion.scheduling.*;
import model.CalendarCollection;
import utils.CustomRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class MainWindow extends JFrame {

    private com.mindfusion.scheduling.Calendar calendar;
    private JList viewMenu;
    private JList calendarList;
    private JMenuBar bar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu styleMenu;
    private JMenuItem newCalendar;
    private JMenuItem newEvent;
    private JMenuItem find;
    private JMenuItem shareCalendar;
    private JMenuItem logout;
    private JScrollPane viewScroll;
    private JScrollPane calendarScroll;
    private DefaultListModel calendars;

    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Calendar");
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        String viewList[] = {"Day","Month","Year"};
        viewMenu = new JList(viewList);
        viewMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        viewMenu.setLayoutOrientation(JList.VERTICAL);
        viewMenu.setVisibleRowCount(3);
        viewMenu.setSelectedIndex(0);
        viewScroll = new JScrollPane(viewMenu);
        viewScroll.setSize(200,80);
        viewScroll.setLocation(0,0);

        calendars = new DefaultListModel();
        calendarList = new JList(calendars);
        calendarList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        calendarList.setLayoutOrientation(JList.VERTICAL);
        calendarList.setVisibleRowCount(-1);
        calendarList.setCellRenderer(new utils.CustomRenderer());
        calendarScroll = new JScrollPane(calendarList);
        calendarScroll.setSize(200,300);
        calendarScroll.setLocation(0,80);

        calendar = new com.mindfusion.scheduling.Calendar();
        calendar.beginInit();
        calendar.setCurrentView(CalendarView.Timetable);
        calendar.setTheme(ThemeType.Light);
        calendar.setLocation(200, 0);
        calendar.setSize(1000, 700);
        calendar.setAllowInplaceEdit(false);
        calendar.setVisible(true);
        calendar.endInit();

        bar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        styleMenu = new JMenu("Style");
        JMenu submenu = new JMenu("New");
        newEvent = new JMenuItem("Event");
        find = new JMenuItem("Find");
        newCalendar = new JMenuItem("Calendar");
        submenu.add(newEvent);
        submenu.add(newCalendar);
        fileMenu.add(submenu);
        shareCalendar = new JMenuItem("Share");
        logout = new JMenuItem("Logout");
        JCheckBoxMenuItem light = new JCheckBoxMenuItem("Dark");
        light.addActionListener(e -> {
            if (light.getState())
                calendar.setTheme(ThemeType.Vista);
            else
                calendar.setTheme(ThemeType.Light);
        });
        styleMenu.add(light);
        editMenu.add(find);
        fileMenu.add(shareCalendar);
        fileMenu.addSeparator();
        fileMenu.add(logout);
        bar.add(fileMenu);
        bar.add(editMenu);
        bar.add(styleMenu);

        cp.add(calendar);
        cp.add(bar);
        cp.add(viewScroll);
        cp.add(calendarScroll);
        this.setJMenuBar(bar);

    }

    public JList<String> getViewMenu() {
        return viewMenu;
    }

    public Calendar getCalendar(){
        return calendar;
    }

    public model.Calendar getCurrentCalendar(){
        return (model.Calendar) calendarList.getSelectedValue();
    }


    public void changeView(){
        String selection = (String) viewMenu.getSelectedValue();

        assert selection != null;
        switch (selection) {
            case "Day":
                calendar.setCurrentView(CalendarView.Timetable);
                break;
            case "Month":
                calendar.setCurrentView(CalendarView.SingleMonth);
                break;
            case "Year":
                calendar.setCurrentView(CalendarView.MonthRange);
                break;
            default:
                break;
        }


    }

    public void addCalendar(model.Calendar cal){
        calendars.add(0,cal);
        calendarList.setSelectedIndex(0);
    }

    public void setCalendars(CalendarCollection list) {
        ArrayList<model.Calendar> calendarsList = list.getCalendars();
        for (model.Calendar cal:
                calendarsList) {
            addCalendar(cal);
        }
    }

    public void addNewEventListener(ActionListener newEventListener){

        this.newEvent.addActionListener(newEventListener);
    }

    public void addFindListener(ActionListener listener){

        this.find.addActionListener(listener);
    }

   public void addChangeViewListener(ListSelectionListener changeViewListener){
        //TODO: non serve passare dal controller
        this.viewMenu.addListSelectionListener(changeViewListener);
    }

    public void addLogoutListener(ActionListener logoutListener){

        this.logout.addActionListener(logoutListener);
    }

    public void addMainCalendarListener(CalendarAdapter mainCalendarAdapter ){

        calendar.addCalendarListener(mainCalendarAdapter);
    }

    public void addNewCalendarListener(ActionListener addNewCalendarListener){

        this.newCalendar.addActionListener(addNewCalendarListener);
    }

    public void addShareCalendarListener(ActionListener listener){

        this.shareCalendar.addActionListener(listener);
    }

    public void addSelectedCalendarListener(ListSelectionListener listener){

        this.calendarList.addListSelectionListener(listener);
    }


    public void close(){
        setVisible(false);
        dispose();
    }

}
