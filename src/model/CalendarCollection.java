package model;

import java.util.ArrayList;

public class CalendarCollection {

    private ArrayList<Calendar> collection;


    public ArrayList<Calendar> getCalendars() {
        return collection;
    }

    public ArrayList<Event> getAllEvents(){   //get all events from calendar in collection
        ArrayList<Event> events = new ArrayList<>();
        for (Calendar calendar:
             collection) {
            events.addAll(calendar.getEvents());
        }
        return events;
    }

    public Calendar getCalendar(String id){   //retrieve a specific calendar within the collection
        for (Calendar calendar:collection
             ) {
            if (calendar.getId().equals(id))
                return calendar;
        }
        return null;
    }

    public CalendarCollection() {
        this.collection = new ArrayList();

    }

    public void addCalendar(Calendar c){

        collection.add(c);
    }
}
