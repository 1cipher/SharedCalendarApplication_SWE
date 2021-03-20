package view;

import com.mindfusion.scheduling.*;
import utils.CalendarCustomRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;


public class MainWindow extends JFrame {

    private com.mindfusion.scheduling.Calendar calendar;
    private JList viewMenu;
    private JList calendarList;
    private JLabel visualization;
    private JLabel calendarsLabel;
    private JMenuBar bar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu styleMenu;
    private JMenuItem newCalendar;
    private JMenuItem newEvent;
    private JMenuItem find;
    private JMenuItem removeCalendar;
    private JMenuItem shareCalendar;
    private JMenuItem logout;
    private JScrollPane viewScroll;
    private JScrollPane calendarScroll;
    private DefaultListModel calendars;

    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1215, 760);
        //setExtendedState(MAXIMIZED_BOTH);
        setTitle("Calendar");
        setLocationRelativeTo(null);
        setResizable(false);

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        visualization = new JLabel("View Format:");
        visualization.setLocation(0,0);
        visualization.setSize(100,50);

        String viewList[] = {"Day","Month","Year"};
        viewMenu = new JList(viewList);
        viewMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        viewMenu.setLayoutOrientation(JList.VERTICAL);
        viewMenu.setVisibleRowCount(3);
        viewMenu.setSelectedIndex(0);
        viewMenu.addListSelectionListener(e->changeView());
        viewScroll = new JScrollPane(viewMenu);
        viewScroll.setSize(200,80);
        viewScroll.setLocation(0,50);

        calendarsLabel = new JLabel("Calendars:");
        calendarsLabel.setLocation(0,130);
        calendarsLabel.setSize(100,50);

        calendars = new DefaultListModel();
        calendarList = new JList(calendars);
        calendarList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        calendarList.setLayoutOrientation(JList.VERTICAL);
        calendarList.setVisibleRowCount(-1);
        calendarList.setCellRenderer(new CalendarCustomRenderer());
        calendarScroll = new JScrollPane(calendarList);
        calendarScroll.setSize(200,700-180);
        calendarScroll.setLocation(0,180);

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
        newEvent.setPreferredSize(new Dimension(100,20));
        find = new JMenuItem("Find");
        find.setPreferredSize(new Dimension(100,20));
        removeCalendar = new JMenuItem("Remove Calendar");
        removeCalendar.setPreferredSize(new Dimension(120,20));
        newCalendar = new JMenuItem("Calendar");
        newCalendar.setPreferredSize(new Dimension(120,20));
        submenu.add(newEvent);
        submenu.add(newCalendar);
        fileMenu.add(submenu);
        shareCalendar = new JMenuItem("Share");
        shareCalendar.setPreferredSize(new Dimension(100,20));
        logout = new JMenuItem("Logout");
        logout.setPreferredSize(new Dimension(100,20));
        JCheckBoxMenuItem light = new JCheckBoxMenuItem("Dark");
        light.setPreferredSize(new Dimension(100,20));
        light.addActionListener(e -> {
            if (light.getState())
                calendar.setTheme(ThemeType.Vista);
            else
                calendar.setTheme(ThemeType.Light);
        });
        styleMenu.add(light);
        editMenu.add(find);
        editMenu.add(removeCalendar);
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
        cp.add(visualization);
        cp.add(calendarsLabel);
        this.setJMenuBar(bar);

    }

    public JList<String> getViewMenu() {
        return viewMenu;
    }

    public Calendar getCalendar(){
        return calendar;
    }

    public JList getCalendarList() {
        return calendarList;
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

    public DefaultListModel getCalendars() {
        return calendars;
    }

    public void refreshCalendarsDisplayed(){
        calendarList.updateUI();
        calendarList.setSelectedIndex(0);
    }



    public void addNewEventListener(ActionListener newEventListener){

        this.newEvent.addActionListener(newEventListener);
    }

    public void addFindListener(ActionListener listener){

        this.find.addActionListener(listener);
    }

    public void addRemoveCalendar(ActionListener listener){

        this.removeCalendar.addActionListener(listener);
    }

    public void deleteCalendar(int selected){

        calendars.removeElementAt(selected);
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
