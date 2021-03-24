package model;

import java.util.ArrayList;

public class Calendar {

    private ArrayList<Event> newBuffer;
    private String id;
    private String name;
    private int permission;

    public Calendar(String cid, String name, int permission) {

        this.permission=permission;
        this.newBuffer = new ArrayList<>();
        this.id = cid;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addEvent(Event e)  {
        newBuffer.add(e);
    }

    public String getId() {
        return id;
    }

    public ArrayList<Event> getEvents(){
        return newBuffer;
    }

    public int getPermission() {
        return permission;
    }
}


