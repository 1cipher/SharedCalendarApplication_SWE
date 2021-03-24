package model;

import java.util.ArrayList;

public class CalendarCollection {

    private ArrayList<Calendar> collection;


    public ArrayList<Calendar> getCalendars() {
        return collection;
    }

    public ArrayList<Event> getAllEvents(){
        ArrayList<Event> events = new ArrayList<>();
        for (Calendar calendar:
             collection) {
            events.addAll(calendar.getEvents());
        }
        return events;
    }
//TODO: DA VALUTARE NL CASO IN CUI SI MOSTRANO TUTTI I CALENDARI IN UN BOTTO

    public Calendar getCalendar(String id){
        for (Calendar calendar:collection
             ) {
            if (calendar.getId().equals(id))
                return calendar;
        }
        return null;
    }

    public CalendarCollection() {
        this.collection = new ArrayList<Calendar>();

    }

    public void addCalendar(Calendar c){

        collection.add(c);
    }
}
