package view;

import com.mindfusion.scheduling.*;
import controller.*;
import model.CalendarCollection;
import utils.CustomRenderer;
import utils.SearchRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.EnumSet;


public class MainWindow extends JFrame {

    private JFormattedTextField textField = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
    private com.mindfusion.scheduling.Calendar calendar;
    private JLabel searchLabel;
    private JButton search;
    private JTextField searchBox;
    private JComboBox<String> viewMenu;
    private JComboBox<model.Calendar> selectedCalendarMenu;
    private JComboBox<SearchStrategy> searchType;
    private JMenuBar bar;
    private JMenu fileMenu;
    private JMenu styleMenu;
    private JMenuItem newCalendar;
    private JMenuItem newEvent;
    private JMenuItem shareCalendar;
    private JMenuItem logout;


    public SearchStrategy getSearchType() {

        return (SearchStrategy) searchType.getSelectedItem();
    }

    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Calendar");
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        viewMenu = new JComboBox<>();
        viewMenu.addItem("day");
        viewMenu.addItem("week");
        viewMenu.addItem("month");
        viewMenu.setLocation(550, 10);
        viewMenu.setSize(100, 20);

        calendar = new com.mindfusion.scheduling.Calendar();
        calendar.beginInit();
        calendar.setCurrentView(CalendarView.Timetable);
        calendar.setTheme(ThemeType.Light);
        calendar.setLocation(0, 40);
        calendar.setSize(1000, 700);
        calendar.setAllowInplaceEdit(false);
        calendar.setVisible(true);
        calendar.endInit();

        searchLabel = new JLabel("Search with filters:");
        searchLabel.setLocation(30,10);
        searchLabel.setSize(150,20);

        searchBox = new JTextField();
        searchBox.setLocation(150, 10);
        searchBox.setSize(200, 20);

        searchType = new JComboBox<>();
        searchType.addItem(new NameSearch());
        searchType.addItem(new LocationSearch());
        searchType.addItem(new NameSearchWithOldOnes());
        searchType.addItem(new LocationStrategyWithOldOnes());
        searchType.setLocation(360,10);
        searchType.setSize(150,20);
        searchType.setRenderer(new SearchRenderer());
        search = new JButton();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/utils/search.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert img != null;
        Image newimg = img.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH );

        search.setIcon(new ImageIcon(newimg));
        search.setLocation(520, 10);
        search.setSize(20, 20);

        selectedCalendarMenu = new JComboBox<>();
        selectedCalendarMenu.setLocation(670, 10);
        selectedCalendarMenu.setSize(100, 20);
        selectedCalendarMenu.setRenderer(new CustomRenderer());

        bar = new JMenuBar();
        fileMenu = new JMenu("File");
        styleMenu = new JMenu("Style");
        JMenu submenu = new JMenu("New");
        newEvent = new JMenuItem("Event");
        newCalendar = new JMenuItem("Calendar");
        submenu.add(newEvent);
        submenu.add(newCalendar);
        fileMenu.add(submenu);
        shareCalendar = new JMenuItem("Share");
        logout = new JMenuItem("Logout");
        JMenuItem light = new JMenuItem("Light");
        light.addActionListener(e -> calendar.setTheme(ThemeType.Light));
        styleMenu.add(light);
        JMenuItem lila = new JMenuItem("Lila");
        lila.addActionListener(e -> calendar.setTheme(ThemeType.Lila));
        styleMenu.add(lila);
        JMenuItem vista = new JMenuItem("Vista");
        vista.addActionListener(e -> calendar.setTheme(ThemeType.Vista));
        styleMenu.add(vista);
        JMenuItem silver = new JMenuItem("Silver");
        silver.addActionListener(e -> calendar.setTheme(ThemeType.Silver));
        styleMenu.add(silver);
        fileMenu.add(shareCalendar);
        fileMenu.addSeparator();
        fileMenu.add(logout);
        bar.add(fileMenu);
        bar.add(styleMenu);

        cp.add(calendar);
        cp.add(searchLabel);
        cp.add(searchBox);
        cp.add(search);
        cp.add(viewMenu);
        cp.add(selectedCalendarMenu);
        cp.add(searchType);
        cp.add(bar);
        this.setJMenuBar(bar);

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


    public void changeView(){
        String selection = (String) viewMenu.getSelectedItem();

        assert selection != null;
        switch (selection) {
            case "day":
                calendar.setCurrentView(CalendarView.Timetable);
                break;
            case "week":
                calendar.setCurrentView(CalendarView.WeekRange);
                calendar.getWeekRangeSettings().setHeaderStyle(EnumSet.of(WeekRangeHeaderStyle.Title));
                break;
            case "month":
                calendar.setCurrentView(CalendarView.MonthRange);
                break;
            default:
                break;
        }


    }

    public void addCalendar(model.Calendar cal){
        selectedCalendarMenu.addItem(cal);
        selectedCalendarMenu.setSelectedItem(cal);  
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

    public void addNewEventListener(ActionListener newEventListener){

        this.newEvent.addActionListener(newEventListener);
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

    public void addNewCalendarListener(ActionListener addNewCalendarListener){

        this.newCalendar.addActionListener(addNewCalendarListener);
    }

    public void addShareCalendarListener(ActionListener listener){

        this.shareCalendar.addActionListener(listener);
    }

    public void addSelectedCalendarListener(ActionListener selectedCalendarListener){

        this.selectedCalendarMenu.addActionListener(selectedCalendarListener);
    }


    public void close(){
        setVisible(false);
        dispose();
    }

}
