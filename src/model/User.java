package model;

public class User {

    private String username;
    private CalendarCollection collection;

    public User(String username){
        this.username = username;
    }

    public void setCollection(CalendarCollection cc){
        this.collection = cc;
    }

    public String getUsername() {
        return username;
    }

    public CalendarCollection getCollection(){
        return collection;
    }

}

