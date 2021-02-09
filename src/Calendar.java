import java.util.ArrayList;

public class Calendar {
    User user;
    ArrayList<Event> newBuffer;
    Database db;
    boolean permission;

    public Calendar(User user, Database db) {
        this.user = user;
        this.newBuffer = new ArrayList<Event>();
        this.db = db;
    }

    public void addtoCalendar(Event e){

        newBuffer.add(e);

    }

    public void removefromCalendar(Event e){

        newBuffer.remove(newBuffer.indexOf(e));

    }
}
