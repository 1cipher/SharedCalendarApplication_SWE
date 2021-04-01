package model;

import com.mindfusion.common.DateTime;

import java.sql.*;

public class Gateway {

    private PreparedStatement ps = null;
    private Connection c;

    public Gateway(Connection c){
        this.c = c;
    }

    public boolean isRegisteredUser(String username, String password) {   //check if username and password check within the database

        String sql = "SELECT UID FROM LOGIN WHERE UID = ? AND PASSWORD = ? ;";

        boolean check = false;
        try {

            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

            ResultSet result = preparedStatement.executeQuery();
            check = result.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return check;
    }

    public void registerUser(String username, String password) {    //add new user to login table with his default calendar(into calendar and participation)

        String cid = java.util.UUID.randomUUID().toString().substring(0, 19);

        String sql = "INSERT INTO LOGIN(UID,PASSWORD) VALUES(?,?);" +
                "INSERT INTO CALENDAR(ID,NAME,OWNER) VALUES(?,?,?);" +
                "INSERT INTO PARTICIPATION(UID,CALENDARID,TYPE) VALUES(?,?,0)";


        try {
            ps = c.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ps.setString(3,cid);
            ps.setString(4,username+"_default");
            ps.setString(5,username);
            ps.setString(6,username);
            ps.setString(7,cid);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public CalendarCollection getUserCalendars(User user){  //retrieve from database the calendars associated with user

        CalendarCollection calendars = new CalendarCollection();
        ResultSet rs;
        String username = user.getUsername();

        String sql = "SELECT CALENDARID,NAME,TYPE FROM PARTICIPATION,CALENDAR WHERE UID=? AND ID=CALENDARID;";
        try {
            ps = c.prepareStatement(sql);
            ps.setString(1,username);
            rs = ps.executeQuery();
            while (rs.next()){
                Calendar calendar = new Calendar( rs.getString("CALENDARID"),rs.getString("NAME"),rs.getInt("TYPE"));
                calendars.addCalendar(calendar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql1 = "SELECT CALENDARID,EVENT,ID,NAME,START_DATE,END_DATE,LOCATION,DESCRIPTION " +
                "FROM PARTICIPATION,CALENDAREVENTS,EVENTS " +
                "WHERE UID=? "+
                "AND CALENDAR=CALENDARID " +
                "AND EVENT=ID;";
        try {
            ps = c.prepareStatement(sql1);
            ps.setString(1,username);
            rs = ps.executeQuery();
            while (rs.next()){
                String calendar_id = rs.getString("CALENDARID");
                Calendar calendar = calendars.getCalendar(calendar_id);
                Event event = new Event(rs.getString("ID"), rs.getString("NAME"), new DateTime(rs.getTimestamp("START_DATE")),new DateTime(rs.getTimestamp("END_DATE")), rs.getString("LOCATION"),rs.getString("DESCRIPTION"));
                calendar.addEvent(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return calendars;


    }

    public boolean isExistingUsername(String username) {  //check if the username exists,used in share calendar case

        String sql = "SELECT * " +
                "FROM LOGIN " +
                "WHERE UID = ?";


        ResultSet resultSet = null;
        try {
            ps = c.prepareStatement(sql);
            ps.setString(1,username);
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean check = false;
        try {
            check = resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return check;

    }

    public void addEvent(Event e, String calendarID) {   //add an event to database: associate the event with the interested calendar of the user

        DateTime start = e.getStartDate();
        DateTime end = e.getEndDate();
        Timestamp ts=new Timestamp(start.getYear(),start.getMonth()-1,start.getDay(),start.getHour(),start.getMinute(),0,0);
        Timestamp te=new Timestamp(end.getYear(),end.getMonth()-1,end.getDay(),end.getHour(),end.getMinute(),0,0);
        String sql = "INSERT INTO EVENTS(ID,NAME,START_DATE,END_DATE,LOCATION,COLOR,DESCRIPTION) VALUES(?,?,?,?,?,0,?);";

        try {
            ps =c.prepareStatement(sql);
            ps.setString(1,e.getId());
            ps.setString(2,e.getName());
            ps.setTimestamp(3,ts);
            ps.setTimestamp(4,te);
            ps.setString(5,e.getLocation());
            ps.setString(6,e.getDescription());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        sql = "INSERT INTO CALENDAREVENTS(CALENDAR,EVENT) VALUES(?,?);";

        try {
            ps =c.prepareStatement(sql);
            ps.setString(1,calendarID);
            ps.setString(2,e.getId());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void deleteEvent(String id){   //delete an event from calendarevents and events tables
        String sql = "DELETE FROM EVENTS WHERE ID=?;";

        try {
            ps =c.prepareStatement(sql);
            ps.setString(1,id);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sql2 = "DELETE FROM CALENDAREVENTS WHERE EVENT=?;";
        try {
            ps =c.prepareStatement(sql2);
            ps.setString(1,id);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void createCalendar(Calendar calendar, User user){   //creates a new calendar associated with user in calendar and participation tables

        String username = user.getUsername();
        String sql = "INSERT INTO CALENDAR(ID,NAME,OWNER) " +
                "VALUES(?,?,?); " +
                "INSERT INTO PARTICIPATION(UID,CALENDARID,TYPE) " +
                "VALUES(?,?,0)";

        try {
            ps =c.prepareStatement(sql);
            ps.setString(1,calendar.getId());
            ps.setString(2,calendar.getName());
            ps.setString(3,username);
            ps.setString(4,username);
            ps.setString(5,calendar.getId());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void shareCalendar(Calendar calendar, String username, int permission){   //add share calendar info to participation table with related permission
        String sql = "INSERT INTO PARTICIPATION(UID,CALENDARID,TYPE)" +
                "VALUES(?,?,?)";

        try {
            ps = c.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,calendar.getId());
            ps.setInt(3,permission);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void deleteUser(User user){  //permanent delete of an user from database:all events and calendars are deleted within the deletion of user

        if(isExistingUsername(user.getUsername())) {

            String sql = "DELETE FROM LOGIN " +
                    "WHERE UID = ?";

            try {
                ps = c.prepareStatement(sql);
                ps.setString(1, user.getUsername());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sql1 = "SELECT CALENDAREVENTS.EVENT " +
                    "FROM (PARTICIPATION JOIN CALENDAR ON PARTICIPATION.CALENDARID = CALENDAR.ID) JOIN CALENDAREVENTS ON CALENDAREVENTS.CALENDAR = CALENDAR.ID " +
                    "WHERE PARTICIPATION.UID = ?";

            ResultSet resultSet;
            try {
                ps = c.prepareStatement(sql1);
                ps.setString(1, user.getUsername());
                resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    deleteEvent(resultSet.getString("EVENT"));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            CalendarCollection calendarCollection = getUserCalendars(user);
            for (Calendar calendar:
                 calendarCollection.getCalendars()) {

                deleteCalendar(calendar);

            }
        }



    }

    public void deleteCalendar(Calendar calendar){   //delete calendar from calendarevents and participation tables
        String sql = "DELETE FROM CALENDAR WHERE ID=?;";

        try {
            ps =c.prepareStatement(sql);
            ps.setString(1,calendar.getId());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "DELETE FROM CALENDAREVENTS WHERE CALENDAR=?;";
        try {
            ps =c.prepareStatement(sql);
            ps.setString(1,calendar.getId());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        sql = "DELETE FROM PARTICIPATION WHERE CALENDARID=?;";
        try {
            ps =c.prepareStatement(sql);
            ps.setString(1,calendar.getId());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void unsubscribeCalendar(Calendar calendar, User user){    //delete calendar only from participation,useful within calendar sharing
        String sql = "DELETE FROM PARTICIPATION WHERE CALENDARID=? AND UID=?;";
        try {
            ps =c.prepareStatement(sql);
            ps.setString(1,calendar.getId());
            ps.setString(2,user.getUsername());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
