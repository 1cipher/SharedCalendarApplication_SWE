package test;

import controller.Controller;
import model.Database;
import model.Gateway;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.awt.*;
import java.awt.event.InputEvent;
import static org.junit.Assert.*;

public class LoginTest {

    private static Controller controller;
    private static Gateway gateway;
    private static Robot robot;

    @BeforeClass
    public static void  init() throws AWTException {

        Database db = Database.getInstance();
        gateway = new Gateway(db.getConnection());
        robot = new Robot();

    }

    @Before
    public void setUp(){

        controller = new Controller(gateway);
    }

    @AfterClass
    public static void closeAll(){

        controller.getMwView().close();
    }

    @Test

    public void testUnsuccesfulLogin(){

        robot.mouseMove(controller.getLogView().getX()+41,controller.getLogView().getY()+100);
        controller.getLogView().getTextPassword().setText("wrong");
        controller.getLogView().getTextUser().setText("wrong");
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        assertNull(controller.getMwView());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test

    public void testSuccessfullLogin(){

        robot.mouseMove(controller.getLogView().getX()+40,controller.getLogView().getY()+100);
        controller.getLogView().getTextPassword().setText("a");
        controller.getLogView().getTextUser().setText("a");

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(controller.getMwView());




    }

}
