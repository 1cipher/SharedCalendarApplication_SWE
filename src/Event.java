import com.mindfusion.common.DateTime;

import java.util.Date;

public class Event {



    enum Color {RED,BLUE,BLACK}

    public String getId() {
        return id;
    }

    private String id;
    private String Name;


    private DateTime start;
    private DateTime end;
    private String Location;
    private String Color;
    private String Description;

    public Event(String id, String name, DateTime start, DateTime end, String location) {
        this.id = id;
        Name = name;
        this.start = start;
        this.end = end;
        Location = location;

    }

    public Event() {

    }

    public String getName() {
        return Name;
    }

    public String getLocation() {
        return Location;
    }

    public String getColor() {
        return Color;
    }

    public String getDescription() {
        return Description;
    }

    public DateTime getStartDate() {
        return start;
    }

    public DateTime getEndDate(){
        return end;
    }

}
