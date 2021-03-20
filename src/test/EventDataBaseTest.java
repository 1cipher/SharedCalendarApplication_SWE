package test;

import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import utils.ACL;

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
        username1 = "Alessio";
        calendar = new Calendar("test","CID", ACL.getCreatorPermission());
        user = new User(username1);
        event = new Event("a","concerto",null,null,"casa","wow");
    }
    @After
    public void removeCalendar(){

        gateway.deleteEvent(event.getId());

        gateway.deleteCalendar(calendar);

    }

    @Before
    public void createCalendar(){

        gateway.createCalendar(calendar,user);

        gateway.addEventinEvents(event,calendar.getId());
        calendarCollection = gateway.getUserCalendars(user);

        calendarCollection.getCalendar("test").getEvents().isEmpty();
    }


    @Test
    @DisplayName("Ensure the correct calendar creation")
    public void checkCreateCalendar(){

        calendarCollection = gateway.getUserCalendars(user);

        assertNotNull(calendarCollection.getCalendar("test"));


    }

    @Test
    @DisplayName("Check the event upload on database")
    public void eventCreation(){

        gateway.addEventinEvents(event,calendar.getId());
        calendarCollection = gateway.getUserCalendars(user);

        calendarCollection.getCalendar("test").getEvents().isEmpty();

        //assertTrue(calendarCollection.getCalendar("test").getEvents().isEmpty());

        gateway.deleteEvent(event.getId());



    }



    @Test
    @DisplayName("Ensure the correct deletion of an event")
    public void deleteEvent(){

        gateway.addEventinEvents(event,calendar.getId());
        gateway.deleteEvent(event.getId());

        assertFalse(user.getCollection().getEvents().isEmpty());

    }
}
