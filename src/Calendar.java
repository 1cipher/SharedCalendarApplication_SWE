import java.sql.SQLException;
import java.util.ArrayList;

public class Calendar {
    User user;
    ArrayList<Event> newBuffer;
    Database db;
    int id;
    boolean permission;

    public Calendar(User user, Database db) {
        this.user = user;
        this.newBuffer = new ArrayList<Event>();
        this.db = db;
    }

    public void addtoCalendar(Event e) throws SQLException {
        newBuffer.add(e);
        db.addToDB(this, e);


    }

    public void removefromCalendar(Event e){

        newBuffer.remove(newBuffer.indexOf(e));

    }

    public int getId() {
        return id;
    }
}
