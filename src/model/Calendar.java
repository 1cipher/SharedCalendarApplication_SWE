package model;

import java.util.ArrayList;

public class Calendar {
    User user;
    ArrayList<Event> newBuffer;
    String id;
    String name;
    public int permission;

    public Calendar(User user, String cid, String name, int permission) {
        this.user = user;
        this.newBuffer = new ArrayList<>();
        this.id = cid;
        this.name = name;
        this.permission = permission;
    }

    public String getName(){
        return name;
    }

    public void addtoCalendar(Event e)  {
        newBuffer.add(e);
    }

    public void removefromCalendar(Event e){

        newBuffer.remove(newBuffer.indexOf(e));

    }

    public String getId() {
        return id;
    }

    public ArrayList<Event> getEvents(){
        return newBuffer;
    }
}
