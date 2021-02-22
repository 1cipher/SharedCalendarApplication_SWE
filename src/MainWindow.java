import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.CalendarView;
import com.mindfusion.scheduling.ThemeType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;

public class MainWindow extends JFrame implements PropertyChangeListener {

    JFormattedTextField textField = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
    Calendar calendar;
    JButton changeVisualizationMode;
    JButton search;
    JButton addEvent;
    JButton logout;
    JTextField searchBox;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem day;
    JMenuItem week;
    JMenuItem month;




    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(368, 362);
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Calendar");

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        menuBar = new JMenuBar();
        menu = new JMenu("Change View");
        day = new JMenuItem("Day");
        week = new JMenuItem("Week");
        month = new JMenuItem("Month");
        menu.addSeparator();
        menu.add(day);
        menu.add(week);
        menu.add(month);
        menuBar.setLocation(550,10);
        menuBar.setSize(100,20);
        menuBar.add(menu);



        Calendar calendar = new Calendar();
        calendar.beginInit();
        calendar.setCurrentView(CalendarView.Timetable);
        calendar.setTheme(ThemeType.Light);
        calendar.setLocation(0,40);
        calendar.setSize(1000,1000);
        calendar.setVisible(true);
        calendar.endInit();

        addEvent = new JButton();
        addEvent.setText("Create Event");
        addEvent.setLocation(0,10);
        addEvent.setSize(200,20);


        searchBox = new JTextField();
        searchBox.setLocation(210,10);
        searchBox.setSize(300,20);

        search = new JButton(new ImageIcon("C:\\search.png"));
        search.setLocation(520,10);
        search.setSize(20,20);



        cp.add(calendar);
        cp.add(addEvent);
        cp.add(searchBox);
        cp.add(search);
        cp.add(menuBar);



    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if (event.getPropertyName().equals("selectedDate")) {

            java.util.Calendar cal = (java.util.Calendar) event.getNewValue();
            Date selDate = cal.getTime();

            textField.setValue(selDate);
        }
    }

    public void addSearchListener(ActionListener searchListener){

        this.search.addActionListener(searchListener);
    }

    public void addChangeViewListener(ActionListener changeViewListener){

        this.changeVisualizationMode.addActionListener(changeViewListener);
    }

    public void addAddEventListener(ActionListener addEventListener){

        this.addEvent.addActionListener(addEventListener);
    }

    public void addDayListener(ActionListener dayListener){

        this.day.addActionListener(dayListener);
    }

    public void addWeekListener(ActionListener weekListener){

        this.week.addActionListener(weekListener);
    }

    public void addMonthListener(ActionListener monthListener){

        this.day.addActionListener(monthListener);
    }
}
