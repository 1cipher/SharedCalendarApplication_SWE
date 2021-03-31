package view;

import javax.swing.*;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.ThemeType;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class EditEventView extends JFrame {

    private com.mindfusion.scheduling.Calendar cal;
    private DateTextField startDate;
    private DateTextField endDate;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel nameLabel;
    private JTextField name;
    private JLabel locationLabel;
    private JTextField location;
    private JLabel descrLabel;
    private JTextArea descr;
    private JButton createEvent;
    private HourComboBox startHour;
    private HourComboBox endHour;
    
    public EditEventView(){
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        setSize(850,300);
        setTitle("Edit Event");
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(null);

        cal = new com.mindfusion.scheduling.Calendar();
        cal.setLocation(0,0);
        cal.setSize(400,200);
        cal.setTheme(ThemeType.Light);

        startDateLabel = new JLabel("Start Date: ");
        startDateLabel.setLocation(410,40);
        startDateLabel.setSize(100,20);

        startDate = new DateTextField();
        startDate.getTextField().setLocation(500,40);
        startDate.getTextField().setSize(100,20);
        startDate.getTextField().setEditable(false);

        startHour = new HourComboBox();
        startHour.getStringJComboBox().setLocation(610,40);
        startHour.getStringJComboBox().setSize(100,20);

        endHour = new HourComboBox();
        endHour.getStringJComboBox().setLocation(610,70);
        endHour.getStringJComboBox().setSize(100,20);

        endDateLabel = new JLabel("End Date: ");
        endDateLabel.setLocation(410,70);
        endDateLabel.setSize(100,20);

        endDate = new DateTextField();
        endDate.getTextField().setLocation(500,70);
        endDate.getTextField().setSize(100,20);
        endDate.getTextField().setEditable(false);

        nameLabel = new JLabel("Name: ");
        nameLabel.setLocation(410,10);
        nameLabel.setSize(100,20);

        name = new JTextField();
        name.setLocation(500,10);
        name.setSize(300,20);
        name.setText("Merenda nonna");

        locationLabel = new JLabel("Location: ");
        locationLabel.setLocation(410,110);
        locationLabel.setSize(100,20);

        location = new JTextField();
        location.setLocation(500,110);
        location.setSize(300,20);
        location.setText("Casa nonna");

        descrLabel = new JLabel("Description: ");
        descrLabel.setLocation(410,140);
        descrLabel.setSize(100,20);

        descr = new JTextArea();
        descr.setLocation(500,140);
        descr.setSize(300,50);
        descr.setText("Sarà deliziosa!");

        createEvent = new JButton("Save");
        createEvent.setLocation(650,200);
        createEvent.setSize(150,20);

        cp.add(cal);
        cp.add(startDateLabel);
        cp.add(startDate.getTextField());
        cp.add(name);
        cp.add(nameLabel);
        cp.add(locationLabel);
        cp.add(location);
        cp.add(descr);
        cp.add(descrLabel);
        cp.add(createEvent);
        cp.add(endDate.getTextField());
        cp.add(endDateLabel);
        cp.add(startHour.getStringJComboBox());
        cp.add(endHour.getStringJComboBox());

    }

    public com.mindfusion.scheduling.Calendar getCal() {
        return cal;
    }

    @Override
    public String getName() {

        return name.getText();
    }

    public String getDescriptionText(){
        return descr.getText();
    }

    public DateTime getStartDate(){
        return startDate.getDate();
    }

    public DateTime getEndDate(){
        return endDate.getDate();
    }

    public String getLocationName(){
        return location.getText();
    }

    public DateTime getStartHour() {
        return startHour.getTime();
    }

    public DateTime getEndHour() {
        return endHour.getTime();
    }

    public void setDescr(String descr) {
        this.descr.setText(descr);
    }

    public void setStartDate(DateTime date) {
        this.startDate.setDate(date);
    }

    public void setEndDate(DateTime date) {
        this.endDate.setDate(date);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setLocation(String location) {
        this.location.setText(location);
    }

    public void setStartHour(DateTime time) {
        this.startHour.setTime(time);
    }

    public void setEndHour(DateTime time) {
        this.endHour.setTime(time);
    }

    public void addCreateEventListener(ActionListener createEventListener){

        this.createEvent.addActionListener(createEventListener);
    }

    public void addCalendarPressListener(MouseListener calendarPressedListener){

        this.cal.addMouseListener(calendarPressedListener);
    }
    public void close(){
        this.setVisible(false);
        this.dispose();
    }

    public boolean startDateisEmpty() {
        return startDate.getTextField().getText().isEmpty();
    }

    public boolean endDateisEmpty() {
        return endDate.getTextField().getText().isEmpty();
    }

    public DateTextField getStartDateBox(){

        return startDate;
    }

    public DateTextField getEndDateBox(){

        return endDate;
    }


}