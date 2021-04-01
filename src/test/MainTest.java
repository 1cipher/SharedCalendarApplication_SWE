package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class MainTest {

    public static void main(String[] args){

        //LOGINDATABASETEST
        Result result = JUnitCore.runClasses(LoginDataBaseTest.class);

        for(Failure failure:result.getFailures()) {

            System.out.println(failure.toString());
        }
        System.out.println("Result of LoginDataBaseTest == "+result.wasSuccessful());


        //EVENTDATABASETEST
        result = JUnitCore.runClasses(EventDataBaseTest.class);

        for(Failure failure:result.getFailures()) {

            System.out.println(failure.toString());
        }
        System.out.println("Result of EventDatabaseTest == "+result.wasSuccessful());


        //SHAREDDATABASETEST
        result = JUnitCore.runClasses(ShareDataBaseTest.class);

        for(Failure failure:result.getFailures()) {

            System.out.println(failure.toString());
        }
        System.out.println("Result of SharedDataBaseTest == "+result.wasSuccessful());


        //LOGINTEST
        result = JUnitCore.runClasses(LoginTest.class);

        for(Failure failure:result.getFailures()) {

            System.out.println(failure.toString());
        }
        System.out.println("Result of LoginTest == "+result.wasSuccessful());


    }
}
