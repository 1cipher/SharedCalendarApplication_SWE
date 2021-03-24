package test;

import static org.junit.Assert.*;

import model.*;
import org.junit.*;


public class LoginDataBaseTest {

    private static Gateway gateway;
    private static String sample;
    private static String username1;
    private static String username2;
    private static CalendarCollection calendarCollection;
    private static Calendar calendar;
    private static User user;

    @BeforeClass
    public static void init(){
        Database db = Database.getInstance();
        gateway = new Gateway(db.getConnection());
        sample = "prova";

    }

    @Before
    public void setUp(){

        sample = "prova";
        gateway.registerUser(sample,sample);

    }

    @After
    public void closeUp(){

        gateway.deleteUser(user);
    }


    @Test
    //@DisplayName("Ensure a correct sign up")
    public void registerNewUser(){


        assertTrue(gateway.isRegisteredUser(sample,sample));


    }

    @Test
    //@DisplayName("Ensure that the username exists")
    public void isExistingUsernameTest(){

        assertTrue(gateway.isExistingUsername(sample));
    }

    @Test
    //@DisplayName("Ensure a correct deletion")
    public void checkDelete(){

        gateway.deleteUser(user);
        assertFalse(gateway.isRegisteredUser(sample,sample));

    }







}
