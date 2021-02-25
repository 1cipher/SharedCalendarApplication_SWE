import java.sql.SQLException;
import java.util.ArrayList;

public class Calendar {
    User user;
    ArrayList<Event> newBuffer;
    String id;
    boolean permission;

    public Calendar(User user, String cid) {
        this.user = user;
        this.newBuffer = new ArrayList<Event>();
    }

    public void addtoCalendar(Event e) throws SQLException {
        newBuffer.add(e);
    }

    public void removefromCalendar(Event e){

        newBuffer.remove(newBuffer.indexOf(e));

    }

    public String getId() {
        return id;
    }
}
