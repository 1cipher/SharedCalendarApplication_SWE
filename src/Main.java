
import java.io.IOException;
import java.sql.SQLException;

public class Main {
     public static void main(String args[]) throws SQLException, IOException {
         String password="cao";
         while (!password.equals("ciao")){
             //password sbagliata :(
             System.out.println("inserisci password");
             password = "ciao";
         }

        User user = new User();

        Database db = new Database();
        Calendar calendar = new Calendar(user,db);
        CalendarCollection cc = new CalendarCollection();
        cc.addCalendarToCollection(calendar);
        user.setCollection(cc);
        int i = 0;
        while (i<3){
            i++;
            Event e = new Event();
            calendar.addtoCalendar(e);

        }
        Event e1 = new Event();
        calendar.addtoCalendar(e1);
        calendar.removefromCalendar(e1);
        
    }
}