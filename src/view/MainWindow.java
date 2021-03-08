package view;

import com.mindfusion.scheduling.*;
import model.CalendarCollection;
import utils.CustomRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.EnumSet;


public class MainWindow extends JFrame {

    private JFormattedTextField textField = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
    private com.mindfusion.scheduling.Calendar calendar;
    private JButton search;
    private JButton addEvent;
    private JButton logout;
    private JTextField searchBox;
    private JComboBox<String> viewMenu;
    private JButton createCalendar;
    private JComboBox<model.Calendar> selectedCalendarMenu;
    private JComboBox<String> styleSelector;



    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 1000);
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

        styleSelector = new JComboBox<>();
        styleSelector.setName("Style");
        styleSelector.addItem(ThemeType.Light.toString());
        styleSelector.addItem(ThemeType.Lila.toString());
        styleSelector.addItem(ThemeType.Vista.toString());
        styleSelector.addItem(ThemeType.Silver.toString());
        styleSelector.setLocation(1010,10);
        styleSelector.setSize(100,20);

        logout = new JButton("Logout");
        logout.setLocation(900, 10);
        logout.setSize(100, 20);


        calendar = new com.mindfusion.scheduling.Calendar();
        calendar.beginInit();
        calendar.setCurrentView(CalendarView.Timetable);
        calendar.setTheme(ThemeType.Light);
        calendar.setLocation(0, 40);
        calendar.setSize(1000, 700);
        calendar.setAllowInplaceEdit(false);
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

        createCalendar = new JButton("Create Calendar");
        createCalendar.setLocation(780,10);
        createCalendar.setSize(110,20);

        selectedCalendarMenu = new JComboBox<>();
        selectedCalendarMenu.setLocation(670, 10);
        selectedCalendarMenu.setSize(100, 20);
        selectedCalendarMenu.setRenderer(new CustomRenderer());

        cp.add(calendar);
        cp.add(addEvent);
        cp.add(searchBox);
        cp.add(search);
        cp.add(viewMenu);
        cp.add(logout);
        cp.add(createCalendar);
        cp.add(selectedCalendarMenu);
        cp.add(styleSelector);

    }

    public String getSearchText(){
        return searchBox.getText();
    }

    public JComboBox<String> getViewMenu() {
        return viewMenu;
    }

    public Calendar getCalendar(){
        return calendar;
    }

    public model.Calendar getCurrentCalendar(){
        return (model.Calendar) selectedCalendarMenu.getSelectedItem();
    }

    public JComboBox<String> getStyleSelector() {return styleSelector;}

    public void changeStyle(){

        String selection = (String) styleSelector.getSelectedItem();

        calendar.setTheme(ThemeType.valueOf(selection));
    }

    public void changeView(){
        String selection = (String) viewMenu.getSelectedItem();

        if (selection.equals("day"))
            calendar.setCurrentView(CalendarView.Timetable);
        else if (selection.equals("week")) {
            calendar.setCurrentView(CalendarView.WeekRange);
            calendar.getWeekRangeSettings().setHeaderStyle(EnumSet.of(WeekRangeHeaderStyle.Title));
        }
        else if (selection.equals("month"))
            calendar.setCurrentView(CalendarView.MonthRange);


    }


    public void setCalendars(CalendarCollection list) {
        ArrayList<model.Calendar> calendarsList = list.getCalendars();
        for (model.Calendar cal:
                calendarsList) {
            selectedCalendarMenu.addItem(cal);
        }
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

    public void addCreateCalendarButtonListener(ActionListener createCalendarButtonListener){

        this.createCalendar.addActionListener(createCalendarButtonListener);
    }

    public void addCalendarInMainWindowPressedListener(MouseListener calendarInMainWindowPressedListener){

        this.calendar.addMouseListener(calendarInMainWindowPressedListener);
    }

    public void addSelectedCalendarListener(ActionListener selectedCalendarListener){

        this.selectedCalendarMenu.addActionListener(selectedCalendarListener);
    }

    public void addStyleSelectorListener(ActionListener styleSelectorListener){

        this.styleSelector.addActionListener(styleSelectorListener);
    }

    public void close(){
        setVisible(false);
        dispose();
    }

}
