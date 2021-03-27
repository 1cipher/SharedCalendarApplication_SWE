package test;

import com.mindfusion.common.DateTime;
import model.*;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.RBAC;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class EventDataBaseTest {

    private static Gateway gateway;
    private static String username1;
    private static CalendarCollection calendarCollection;
    private static Calendar calendar;
    private static User user;
    private static Event event;

    @BeforeClass
    public static void init(){
        Database db = Database.getInstance();
        gateway = new Gateway(db.getConnection());
        username1 = "michele";
        calendar = new Calendar("test","CID", RBAC.getCreatorPermission());
        user = new User(username1);
        event = new Event("a","concerto",new DateTime(2000,2,2,2,2,2,0),new DateTime(2000,2,2,2,3,3,0),"casa","wow");

    }


    @Test
    //@DisplayName("Ensure the correct calendar creation")
    public void checkCreateCalendar(){

        gateway.createCalendar(calendar,user);

        calendarCollection = gateway.getUserCalendars(user);

        assertNotNull(calendarCollection.getCalendar("test"));

        gateway.deleteCalendar(calendar);


    }

    @Test
    //@DisplayName("Check the event upload on database")
    public void eventCreation(){

        gateway.createCalendar(calendar,user);
        calendarCollection = gateway.getUserCalendars(user);
        int sizeBefore = calendarCollection.getAllEvents().size();
        gateway.addEvent(event,calendar.getId());
        calendarCollection = gateway.getUserCalendars(user);
        int sizeAfter = calendarCollection.getAllEvents().size();

        //calendarCollection.getCalendar("test").getEvents().isEmpty();

        assertTrue(sizeAfter==sizeBefore+1);

        gateway.deleteEvent(event.getId());


        gateway.deleteCalendar(calendar);


    }



    @Test
    //@DisplayName("Ensure the correct deletion of an event")
    public void deleteEvent(){

        gateway.createCalendar(calendar,user);
        calendarCollection = gateway.getUserCalendars(user);
        ArrayList<Event> list = calendarCollection.getAllEvents();
        int sizeBefore = list.size();
        gateway.addEvent(event,calendar.getId());
        gateway.deleteEvent(event.getId());

        calendarCollection = gateway.getUserCalendars(user);
        list = calendarCollection.getAllEvents();
        int sizeAfter = list.size();
        assertTrue(sizeAfter==sizeBefore);

        gateway.deleteCalendar(calendar);

    }
}
