import java.util.Date;

public class Event {

    enum Color {RED,BLUE,BLACK}

    public String getId() {
        return id;
    }

    private String id;
    private String Name;

    public Date getDate() {
        return date;
    }

    private Date date;
    private String Location;
    private String Color;
    private String Description;

    public Event(String id, String name, Date date, String location) {
        this.id = id;
        Name = name;
        this.date = date;
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
}
