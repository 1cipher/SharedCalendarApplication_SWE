package model;

import com.mindfusion.common.DateTime;

public class Event {

    private String id;
    private String name;
    private DateTime start;
    private DateTime end;
    private String location;
    private String description;

    public Event(String id, String name, DateTime start, DateTime end, String location,String description) {

        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.location = location;
        this.description = description;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getStartDate() {
        return start;
    }

    public DateTime getEndDate(){
        return end;
    }

}
