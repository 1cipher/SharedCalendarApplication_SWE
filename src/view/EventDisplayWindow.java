package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EventDisplayWindow extends JFrame {

    JLabel name;
    JLabel startDate;
    JLabel endDate;
    JLabel location;
    JLabel descr;
    JLabel nameDisplayed;
    JLabel startDateDisplayed;
    JLabel endDateDisplayed;
    JLabel locationDisplayed;
    JLabel descrDisplayed;
    JButton okButton;
    JButton deleteButton;
    Container c;

    private EventDisplayWindow(){

        setDefaultCloseOperation(HIDE_ON_CLOSE);

        c = getContentPane();
        c.setLayout(null);

        setSize(400,300);

        name = new JLabel("Name:",SwingConstants.CENTER);
        name.setLocation(30,30);
        name.setSize(100,20);

        startDate = new JLabel("Start Date:",SwingConstants.CENTER);
        startDate.setLocation(30,60);
        startDate.setSize(100,20);

        endDate = new JLabel("End Date",SwingConstants.CENTER);
        endDate.setLocation(30,90);
        endDate.setSize(100,20);

        location = new JLabel("Location",SwingConstants.CENTER);
        location.setLocation(30,120);
        location.setSize(100,20);

        descr = new JLabel("Description",SwingConstants.CENTER);
        descr.setLocation(30,150);
        descr.setSize(100,20);

        nameDisplayed = new JLabel("",SwingConstants.CENTER);
        nameDisplayed.setLocation(150,30);
        nameDisplayed.setSize(100,20);

        startDateDisplayed = new JLabel("",SwingConstants.CENTER);
        startDateDisplayed.setLocation(150,60);
        startDateDisplayed.setSize(100,20);

        endDateDisplayed = new JLabel("",SwingConstants.CENTER);
        endDateDisplayed.setLocation(150,90);
        endDateDisplayed.setSize(100,20);

        locationDisplayed = new JLabel("",SwingConstants.CENTER);
        locationDisplayed.setLocation(150,120);
        locationDisplayed.setSize(100,20);

        descrDisplayed = new JLabel("",SwingConstants.CENTER);
        descrDisplayed.setLocation(150,150);
        descrDisplayed.setSize(100,20);

        okButton = new JButton("Ok!");
        okButton.setLocation(90,200);
        okButton.setSize(100,20);

        deleteButton = new JButton("Delete!");
        deleteButton.setLocation(210,200);
        deleteButton.setSize(100,20);

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

            startDateDisplayed = date;

            return this;
        }

        public Builder setEndDate(String date){

            endDateDisplayed = date;

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

        public EventDisplayWindow build(){

            EventDisplayWindow eventDisplayWindow = new EventDisplayWindow();
            eventDisplayWindow.nameDisplayed.setText(this.nameDisplayed);
            eventDisplayWindow.startDateDisplayed.setText(this.startDateDisplayed);
            eventDisplayWindow.endDateDisplayed.setText(this.endDateDisplayed);
            eventDisplayWindow.locationDisplayed.setText(this.locationDisplayed);
            eventDisplayWindow.descrDisplayed.setText(this.descrDisplayed);

            return eventDisplayWindow;

        }


    }

    public void addOkButtonForEventDisplayWindowListener(ActionListener okButtonForEventDisplayWindowListener){

        this.okButton.addActionListener(okButtonForEventDisplayWindowListener);
    }

    public void addDeleteButtonForEventDisplayWindowListener(ActionListener deleteButtonForEventDisplayWindowListener){

        this.deleteButton.addActionListener(deleteButtonForEventDisplayWindowListener);
    }


}
