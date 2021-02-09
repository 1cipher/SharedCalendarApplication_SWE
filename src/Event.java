
public class Event {

    enum Color {RED,BLUE,BLACK}
    private int id;
    private String Name;
    private String Date;
    private String Location;

    public Event(int id, String name, String date, String location) {
        this.id = id;
        Name = name;
        Date = date;
        Location = location;

    }

    public Event() {

    }
}
