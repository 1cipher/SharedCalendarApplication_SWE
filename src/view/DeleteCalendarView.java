package view;
import utils.CalendarCustomRenderer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DeleteCalendarView extends JFrame {

    private JList calendarList;
    private JButton deleteButton;
    private JLabel deleteLabel;
    private DefaultListModel defaultListModel;
    private JScrollPane scrollPane;

    public DeleteCalendarView() {

        setTitle("Delete Calendar");
        setSize(400, 300);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(null);

        deleteLabel = new JLabel("Select the calendar you want to delete:",SwingConstants.CENTER);
        deleteLabel.setLocation(30,30);
        deleteLabel.setSize(340,40);

        deleteButton = new JButton("Delete!");
        deleteButton.setLocation(160, 200);
        deleteButton.setSize(80, 20);

        defaultListModel = new DefaultListModel();
        calendarList = new JList(defaultListModel);
        calendarList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        calendarList.setLayoutOrientation(JList.VERTICAL);
        calendarList.setVisibleRowCount(-1);
        calendarList.setCellRenderer(new CalendarCustomRenderer());
        scrollPane = new JScrollPane(calendarList);
        scrollPane.setSize(300,100);
        scrollPane.setLocation(50,80);


        c.add(deleteButton);
        c.add(deleteLabel);
        c.add(scrollPane);
    }

    public void close(){
        this.setVisible(false);
        this.dispose();
    }

    public void addDeleteCalendarListener(ActionListener deleteCalendarListener){

        this.deleteButton.addActionListener(deleteCalendarListener);
    }

    public JList getCalendarList() {
        return calendarList;
    }

    public DefaultListModel getDefaultListModel() {
        return defaultListModel;
    }

    public void deleteCalendar(int selected){

        defaultListModel.removeElementAt(selected);
    }
}
