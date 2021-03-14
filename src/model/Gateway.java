package model;

import com.mindfusion.common.DateTime;

import java.sql.*;

public class Gateway {

    PreparedStatement preparedStatement = null;
    Connection c;

    public Gateway(Connection c){
        this.c = c;
    }

    public boolean checkUserPresence(String acquiredUser,String acquiredPassword) {

        String sql = "SELECT UID FROM LOGIN WHERE UID = ? AND PASSWORD = ? ;";

        boolean check = false;
        try {

            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,acquiredUser);
            preparedStatement.setString(2,acquiredPassword);

            ResultSet result = preparedStatement.executeQuery();
            check = result.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return check;
    }

    public void registerNewUser(String username,String password) {

        String cid = java.util.UUID.randomUUID().toString().substring(0, 19);

        String sql = "INSERT INTO LOGIN(UID,PASSWORD) VALUES(?,?);" +
                "INSERT INTO CALENDAR(ID,NAME,OWNER) VALUES(?,?,?);" +
                "INSERT INTO PARTICIPATION(UID,CALENDARID,TYPE) VALUES(?,?,0)";


        try {
            preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,cid);
            preparedStatement.setString(4,username+"_default");
            preparedStatement.setString(5,username);
            preparedStatement.setString(6,username);
            preparedStatement.setString(7,cid);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public CalendarCollection getUserCalendars(User currentUser){

        CalendarCollection calendars = new CalendarCollection();
        ResultSet rs;
        String username = currentUser.getUsername();

        String sql = "SELECT CALENDARID,NAME,TYPE FROM PARTICIPATION,CALENDAR WHERE UID=? AND ID=CALENDARID;";
        try {
            preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,username);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                Calendar calendar = new Calendar(currentUser, rs.getString("CALENDARID"),rs.getString("NAME"),rs.getInt("TYPE"));
                calendars.addCalendarToCollection(calendar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT CALENDARID,EVENT,ID,NAME,START_DATE,END_DATE,LOCATION,DESCRIPTION " +
                "FROM PARTICIPATION,CALENDAREVENTS,EVENTS " +
                "WHERE UID=? "+
                "AND CALENDAR=CALENDARID " +
                "AND EVENT=ID;";
        try {
            preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,username);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                String calendar_id = rs.getString("CALENDARID");
                Calendar calendar = calendars.getCalendar(calendar_id);
                Event event = new Event(rs.getString("ID"), rs.getString("NAME"), new DateTime(rs.getTimestamp("START_DATE")),new DateTime(rs.getTimestamp("END_DATE")), rs.getString("LOCATION"),rs.getString("DESCRIPTION"));
                calendar.addtoCalendar(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return calendars;
        //TODO: si può fare tutto in un colpo?

    }

    public boolean isExistingUsername(String username) {

        String sql = "SELECT * " +
                "FROM LOGIN " +
                "WHERE UID = ?";


        ResultSet resultSet = null;
        try {
            preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
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

    public void addEventinEvents(Event e, String calendar_id) {

        DateTime start = e.getStartDate();
        DateTime end = e.getEndDate();
        Timestamp ts=new Timestamp(start.getYear(),start.getMonth()-1,start.getDay(),start.getHour(),start.getMinute(),0,0);
        Timestamp te=new Timestamp(end.getYear(),end.getMonth()-1,end.getDay(),end.getHour(),end.getMinute(),0,0);
        String sql = "INSERT INTO EVENTS(ID,NAME,START_DATE,END_DATE,LOCATION,COLOR,DESCRIPTION) VALUES(?,?,?,?,?,0,?);";

        try {
            preparedStatement=c.prepareStatement(sql);
            preparedStatement.setString(1,e.getId());
            preparedStatement.setString(2,e.getName());
            preparedStatement.setTimestamp(3,ts);
            preparedStatement.setTimestamp(4,te);
            preparedStatement.setString(5,e.getLocation());
            preparedStatement.setString(6,e.getDescription());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        sql = "INSERT INTO CALENDAREVENTS(CALENDAR,EVENT) VALUES(?,?);";

        try {
            preparedStatement=c.prepareStatement(sql);
            preparedStatement.setString(1,calendar_id);
            preparedStatement.setString(2,e.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void deleteEvent(String id){
        String sql = "DELETE FROM EVENTS WHERE ID=?;";

        try {
            preparedStatement=c.prepareStatement(sql);
            preparedStatement.setString(1,id);

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sql2 = "DELETE FROM CALENDAREVENTS WHERE EVENT=?;";
        try {
            preparedStatement=c.prepareStatement(sql2);
            preparedStatement.setString(1,id);

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void createCalendar(Calendar calendar, User currentUser){

        String username = currentUser.getUsername();
        String sql = "INSERT INTO CALENDAR(ID,NAME,OWNER)" +
                "VALUES(?,?,?);" +
                "INSERT INTO PARTICIPATION(UID,CALENDARID,TYPE)" +
                "VALUES(?,?,0)";

        try {
            preparedStatement=c.prepareStatement(sql);
            preparedStatement.setString(1,calendar.getId());
            preparedStatement.setString(2,calendar.getName());
            preparedStatement.setString(3,username);
            preparedStatement.setString(4,username);
            preparedStatement.setString(5,calendar.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void shareCalendar(Calendar calendar, String username, int permission){
        String sql = "INSERT INTO PARTICIPATION(UID,CALENDARID,TYPE)" +
                "VALUES(?,?,?)";

        try {
            preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,calendar.getId());
            preparedStatement.setInt(3,permission);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void deleteUser(String uid){

        String sql = "DELETE FROM LOGIN WHERE UID = ?";

        try {
            preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,uid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT CE.EVENT" +
                "FROM (PARTICIPATION P JOIN CALENDAR C ON P.CALENDARID = C.ID) JOIN CALENDAREVENTS CE ON CE.CALENDAR = C.ID" +
                "WHERE CE.UID = ?";

        ResultSet resultSet = null;
        try {
            preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,uid);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                deleteEvent(resultSet.getString("EVENT"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        sql = "DELETE " +
                "FROM CALENDAR JOIN PARTICIPATION ON CALENDARID = ID" +
                "WHERE UID = UID";
        try {
            preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1,uid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    public void deleteCalendar(model.Calendar calendar){
        String sql = "DELETE FROM CALENDAR WHERE ID=?;";

        try {
            preparedStatement=c.prepareStatement(sql);
            preparedStatement.setString(1,calendar.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "DELETE FROM CALENDAREVENTS WHERE CALENDAR=?;";
        try {
            preparedStatement=c.prepareStatement(sql);
            preparedStatement.setString(1,calendar.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        sql = "DELETE FROM PARTICIPATION WHERE CALENDARID=?;";
        try {
            preparedStatement=c.prepareStatement(sql);
            preparedStatement.setString(1,calendar.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void unsubscribeCalendar(model.Calendar calendar, User user){
        String sql = "DELETE FROM PARTICIPATION WHERE CALENDARID=? AND UID=?;";
        try {
            preparedStatement=c.prepareStatement(sql);
            preparedStatement.setString(1,calendar.getId());
            preparedStatement.setString(2,user.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
