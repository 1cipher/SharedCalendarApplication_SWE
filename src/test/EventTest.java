package test;

import controller.Controller;
import model.Database;
import model.Gateway;

import java.awt.*;
import java.awt.event.InputEvent;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EventTest {

    private Controller controller;
    private Gateway gateway;
    private Robot robot;

    @Before
    public void init() throws AWTException {

        Database db = Database.getInstance();
        gateway = new Gateway(db.getConnection());
        controller = new Controller(gateway);
        robot = new Robot();
        robot.mouseMove(controller.getLogView().getX()+41,controller.getLogView().getY()+101);
        controller.getLogView().getTextPassword().setText("wrong username");
        controller.getLogView().getTextUser().setText("wrong password");
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

    }

    @Test
    //@DisplayName("Check if the dialog is created correctly")
    public void createWrongEvent(){

        controller.setupCreateEventWindow();
        robot.mouseMove(controller.getCwView().getX()+650,controller.getCwView().getY()+200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

        assertEquals(controller.getDialog().getLabel(),"Check null values");

    }

    @Test
    public void createEvent(){


        controller.getCwView().getStartDateBox().setEditable(true);
        controller.getCwView().getEndDateBox().setEditable(true);

        controller.getCwView().getStartDateBox().setText("2021-03-19");
        controller.getCwView().getEndDateBox().setText("2021-03-19");

        controller.getCwView().setName("test");
        controller.getCwView().setDescr("test");
        controller.getCwView().setLocation("test");
        //TODO:PRENDI DATE E ORE MEGLIO

        robot.mouseMove(controller.getCwView().getX()+650,controller.getCwView().getY()+200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

        assertEquals(controller.getDialog().getLabel(),"Event saved!");

        


    }
}
