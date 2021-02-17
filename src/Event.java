
public class Event {

    enum Color {RED,BLUE,BLACK}
    private int id;
    private String Name;
    private String Date;
    private String Location;
    private String Color;
    private String Description;

    public Event(int id, String name, String date, String location) {
        this.id = id;
        Name = name;
        Date = date;
        Location = location;

    }

    public Event() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getDate() {
        return Date;
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
