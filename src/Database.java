
import com.mindfusion.common.DateTime;


import java.sql.*;

public class Database {
    User currentUser;
    public Connection c = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;

    public Database() {

    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void createConnection() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/CalendarApplication",
                            "postgres", Private.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    private void dropConnection() throws  Exception{

        c.close();
    }

    public boolean checkUserPresence(String acquiredUser,String acquiredPassword) throws SQLException {

        String sql = "SELECT UID " +
                "FROM LOGIN " +
                "WHERE UID = '"+acquiredUser+"' AND PASSWORD ='"+acquiredPassword+"'";

        stmt = c.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        boolean check = result.next();

        return check;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    public void registerNewUser(String username,String password) {

        String sql = "INSERT INTO LOGIN(UID,PASSWORD)" +
                "VALUES('"+username+"','"+password+"');" +
                "INSERT INTO CALENDAR(ID,NAME,OWNER)" +
                "VALUES('"+"CID1"+"','"+username+"_default"+"','"+username+"');" +
                "INSERT INTO PARTICIPATION(UID,CALENDARID,TYPE)" +
                "VALUES('"+username+"','"+"CID1"+"',0)";
        update(sql);

    }

    public CalendarCollection getCurrentUserCalendars(){

        CalendarCollection calendars = new CalendarCollection();
        ResultSet rs;

        String sql = "SELECT CALENDARID,NAME FROM PARTICIPATION,CALENDAR WHERE UID='"+currentUser.getUsername()+"' AND ID=CALENDARID;";
        rs = fetch(sql);
        try {
            while (rs.next()){
                Calendar calendar = new Calendar(currentUser, rs.getString("CALENDARID"),rs.getString("NAME"));
                calendars.addCalendarToCollection(calendar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        };


        sql = "SELECT CALENDARID,EVENT,ID,NAME,START_DATE,END_DATE,LOCATION FROM PARTICIPATION,CALENDAREVENTS,EVENTS " +
                "WHERE UID='"+currentUser.getUsername()+
                "' AND CALENDAR=CALENDARID " +
                "AND EVENT=ID;";
        rs = fetch(sql);
        try {
            while (rs.next()){
                String calendar_id = rs.getString("CALENDARID");
                Calendar calendar = calendars.getCalendar(calendar_id);
                Event event = new Event(rs.getString("ID"), rs.getString("NAME"), new DateTime(rs.getTimestamp("START_DATE")),new DateTime(rs.getTimestamp("END_DATE")), rs.getString("LOCATION"));
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
                "WHERE UID = '" + username + "'";

        ResultSet resultSet = fetch(sql);

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
        String sql = "INSERT INTO EVENTS(ID,NAME,START_DATE,END_DATE,LOCATION,COLOR,DESCRIPTION)" +
                "VALUES('"+e.getId()+"','"+e.getName()+"','"+ts+"','"+te+"','"+e.getLocation()+"',0,'"+e.getDescription()+"');";
        update(sql);

        sql = "INSERT INTO CALENDAREVENTS(CALENDAR,EVENT)" +
                "VALUES('"+calendar_id+"','"+e.getId()+"');";
        update(sql);


    }

    public void deleteEvent(Event e){
        String sql = "DELETE FROM EVENTS WHERE ID='"+e.getId()+"';";
        String sql2 = "DELETE FROM CALENDAREVENTS WHERE EVENT='"+e.getId()+"';";
        update(sql);
        update(sql2);

    }

    public void CreateCalendar(Calendar calendar){
        String sql = "INSERT INTO CALENDAR(ID,NAME,OWNER)" +
                "VALUES('"+calendar.getId()+"','"+calendar.getName()+"','"+currentUser.getUsername()+"');" +
                "INSERT INTO PARTICIPATION(UID,CALENDARID,TYPE)" +
                "VALUES('"+currentUser.getUsername()+"','"+calendar.getId()+"',0)";
        update(sql);
    }

    private void update(String sql){
        //TODO: primo try si può cancellare tutto?
        try {
            stmt = c.createStatement();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        try{
            stmt.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    private ResultSet fetch(String sql) {
        try {
            stmt = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}




