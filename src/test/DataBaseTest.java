package test;

import static org.junit.Assert.*;

import model.*;
import org.junit.Before;
import org.junit.Test;
import utils.ACL;


public class DataBaseTest {

    Gateway gateway;

    @Before
    public void init(){
        Database db = Database.getInstance();
        gateway = new Gateway(db.createConnection());
    }


    @Test
    public void user(){

        String sample = "prova";
        gateway.registerNewUser(sample,sample);
        assertTrue(gateway.checkUserPresence(sample,sample));

        assertTrue(gateway.isExistingUsername(sample));

        gateway.deleteUser(sample);
        assertFalse(gateway.checkUserPresence(sample,sample));
    }

   @Test
    public void event(){

        String username = "alessio";
        User user = new User(username);
        Event event = new Event("a","concerto",null,null,"casa","wow");
        Calendar calendar = new Calendar(user,"aaa","CID", ACL.getCreatorPermission());
        gateway.createCalendar(calendar,user);

        CalendarCollection calendarCollection = gateway.getUserCalendars(user);

        assertNotNull(calendarCollection.getCalendar("aaa"));

        gateway.addEventinEvents(event,calendar.getId());

        assertFalse(calendarCollection.getCalendar("aaa").getEvents().isEmpty());

        gateway.deleteUser(username);

        String username2 = "marco";
        User marco = new User(username2);
        gateway.shareCalendar(calendar,username2,0);
        CalendarCollection calendarCollection1 = gateway.getUserCalendars(marco);
        assertFalse(calendarCollection1.getCalendar(calendar.getId()).getEvents().isEmpty());


   }

   @Test

    public void share(){

       String username = "alessio";
       User user = new User(username);
       Event event = new Event("a","concerto",null,null,"casa","wow");
       Calendar calendar = new Calendar(user,"aaa","CID",ACL.getCreatorPermission());
       gateway.createCalendar(calendar,user);

       String username2 = "marco";
       User marco = new User(username2);
       gateway.shareCalendar(calendar,username2,0);
       CalendarCollection calendarCollection1 = gateway.getUserCalendars(marco);
       assertFalse(calendarCollection1.getCalendar(calendar.getId()).getEvents().isEmpty());

       gateway.deleteUser(username);
       gateway.deleteUser(username2);
       assertFalse(gateway.isExistingUsername(username));
       assertFalse(gateway.isExistingUsername(username2));

   }

}
