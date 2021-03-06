package model;

public class User {

    public String getUsername() {
        return username;
    }

    String username;
    CalendarCollection collection;

    public User(String username){
        this.username = username;
    }

    public void setCollection(CalendarCollection cc){
        this.collection = cc;
    }
}

