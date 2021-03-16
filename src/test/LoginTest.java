package test;

import controller.MWController;
import model.Database;
import model.Gateway;
import org.junit.BeforeClass;
import org.junit.Test;
import java.awt.*;
import java.awt.event.InputEvent;

public class LoginTest {

    private MWController controller;
    private Gateway gateway;
    private Robot robot;

    @BeforeClass
    public void init() throws AWTException {

        Database db = Database.getInstance();
        gateway = new Gateway(db.createConnection());
        controller = new MWController(gateway);
        robot = new Robot();

    }

    @Test

    public void testLogin(){

        robot.mouseMove(21,61);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

    }
}
