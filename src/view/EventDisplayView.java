package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EventDisplayView extends JFrame {

    private String id;
    private JLabel name;
    private JLabel startDate;
    private JLabel endDate;
    private JLabel location;
    private JLabel descr;
    private JLabel nameDisplayed;
    private JLabel startDateDisplayed;
    private JLabel endDateDisplayed;
    private JLabel locationDisplayed;
    private JLabel descrDisplayed;
    private JButton okButton;
    private JButton deleteButton;
    JButton editButton;
    private Container c;


    private EventDisplayView(){

        setDefaultCloseOperation(HIDE_ON_CLOSE);

        c = getContentPane();
        c.setLayout(null);
        setLocationRelativeTo(null);
        setTitle("Event Displayed!");

        setSize(410,300);

        name = new JLabel("Name:",SwingConstants.CENTER);
        name.setLocation(100,30);
        name.setSize(100,20);

        startDate = new JLabel("Start Date:",SwingConstants.CENTER);
        startDate.setLocation(100,60);
        startDate.setSize(100,20);

        endDate = new JLabel("End Date",SwingConstants.CENTER);
        endDate.setLocation(100,90);
        endDate.setSize(100,20);

        location = new JLabel("Location",SwingConstants.CENTER);
        location.setLocation(100,120);
        location.setSize(100,20);

        descr = new JLabel("Description",SwingConstants.CENTER);
        descr.setLocation(100,150);
        descr.setSize(100,20);

        nameDisplayed = new JLabel("",SwingConstants.CENTER);
        nameDisplayed.setLocation(210,30);
        nameDisplayed.setSize(100,20);

        startDateDisplayed = new JLabel("",SwingConstants.CENTER);
        startDateDisplayed.setLocation(210,60);
        startDateDisplayed.setSize(100,20);

        endDateDisplayed = new JLabel("",SwingConstants.CENTER);
        endDateDisplayed.setLocation(210,90);
        endDateDisplayed.setSize(100,20);

        locationDisplayed = new JLabel("",SwingConstants.CENTER);
        locationDisplayed.setLocation(210,120);
        locationDisplayed.setSize(100,20);

        descrDisplayed = new JLabel("",SwingConstants.CENTER);
        descrDisplayed.setLocation(210,150);
        descrDisplayed.setSize(100,20);

        okButton = new JButton("Ok!");
        okButton.setLocation(10,200);
        okButton.setSize(120,20);

        editButton = new JButton("Edit!");
        editButton.setLocation(140,200);
        editButton.setSize(120,20);

        deleteButton = new JButton("Delete!");
        deleteButton.setLocation(270,200);
        deleteButton.setSize(120,20);

        c.add(name);
        c.add(startDate);
        c.add(endDate);
        c.add(location);
        c.add(descr);
        c.add(nameDisplayed);
        c.add(startDateDisplayed);
        c.add(endDateDisplayed);
        c.add(locationDisplayed);
        c.add(descrDisplayed);
        c.add(okButton);
        c.add(deleteButton);
        c.add(editButton);



    }

    public void close(){
        this.setVisible(false);
        this.dispose();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Builder{

        String nameDisplayed;
        String startDateDisplayed;
        String endDateDisplayed;
        String locationDisplayed;
        String descrDisplayed;

        public Builder setName(String name){

            nameDisplayed = name;

            return this;
        }

        public Builder setStartDate(String date){

            startDateDisplayed = date.substring(0,15);

            return this;
        }

        public Builder setEndDate(String date){

            endDateDisplayed = date.substring(0,15);

            return this;
        }

        public Builder setLocation(String location){

            locationDisplayed = location;

            return this;
        }

        public Builder setDescription(String description){

            descrDisplayed = description;

            return this;
        }

        public EventDisplayView build(){

            EventDisplayView eventDisplayView = new EventDisplayView();
            eventDisplayView.nameDisplayed.setText(this.nameDisplayed);
            eventDisplayView.startDateDisplayed.setText(this.startDateDisplayed);
            eventDisplayView.endDateDisplayed.setText(this.endDateDisplayed);
            eventDisplayView.locationDisplayed.setText(this.locationDisplayed);
            eventDisplayView.descrDisplayed.setText(this.descrDisplayed);

            return eventDisplayView;

        }


    }

    public void addOkButtonListener(ActionListener okButtonForEventDisplayWindowListener){

        this.okButton.addActionListener(okButtonForEventDisplayWindowListener);
    }

    public void addDeleteButtonListener(ActionListener deleteButtonForEventDisplayWindowListener){

        this.deleteButton.addActionListener(deleteButtonForEventDisplayWindowListener);
    }

    public void addEditButtonListener(ActionListener editButtonListener){

        this.editButton.addActionListener(editButtonListener);
    }


}
