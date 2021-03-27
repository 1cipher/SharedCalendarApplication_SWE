package test;

import model.*;
import org.junit.*;
import utils.RBAC;

import static org.junit.Assert.*;

public class ShareDataBaseTest {

    private static Gateway gateway;
    private static String username1;
    private static Calendar calendar;
    private static User user;
    private static User userToShareWith;

    @BeforeClass
    public static void init(){
        Database db = Database.getInstance();
        gateway = new Gateway(db.getConnection());
        username1 = "Marco";
        user = new User(username1);
        calendar = new Calendar("test","CID", RBAC.getCreatorPermission());

        userToShareWith = new User("Alessio");

        gateway.createCalendar(calendar,user);


    }
    @AfterClass
    public static void reset(){

        gateway.deleteCalendar(calendar);
    }

    @After
    public void setUp(){

        gateway.unsubscribeCalendar(calendar,userToShareWith);
    }

    @Test

    public void shareAsCreator(){


        gateway.shareCalendar(calendar,userToShareWith.getUsername(), RBAC.getCreatorPermission());
        CalendarCollection calendarCollection1 = gateway.getUserCalendars(userToShareWith);
        assertEquals(calendarCollection1.getCalendar(calendar.getId()).getPermission(),0);


    }

    @Test
    public void shareAsOwner(){

        gateway.shareCalendar(calendar,userToShareWith.getUsername(), RBAC.getOwnerPermission());
        CalendarCollection calendarCollection1 = gateway.getUserCalendars(userToShareWith);
        assertEquals(calendarCollection1.getCalendar(calendar.getId()).getPermission(),1);
    }

    @Test
    public void shareAsUser(){

        gateway.shareCalendar(calendar,userToShareWith.getUsername(), RBAC.getUserPermission());
        CalendarCollection calendarCollection1 = gateway.getUserCalendars(userToShareWith);
        assertEquals(calendarCollection1.getCalendar(calendar.getId()).getPermission(),2);
    }

}
