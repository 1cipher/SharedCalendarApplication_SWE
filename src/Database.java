
import com.mindfusion.common.DateTime;

import java.sql.*;

public class Database {
    User currentUser;
    public Connection c = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;

    public Database() {

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
                            "postgres", "accendino99");
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

        try {
            stmt = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO LOGIN(UID,PASSWORD)" +
                "VALUES('"+username+"','"+password+"');" +
                "INSERT INTO CALENDAR(ID,NAME,OWNER)" +
                "VALUES('"+"CID1"+"','"+username+"_default"+"','"+username+"');" +
                "INSERT INTO PARTICIPATION(UID,CALENDARID,TYPE)" +
                "VALUES('"+username+"','"+"CID1"+"',0)";


        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public CalendarCollection getCurrentUserCalendars(){

        CalendarCollection calendars = new CalendarCollection();
        ResultSet rs;

        try {
            stmt = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "SELECT CALENDARID FROM PARTICIPATION WHERE UID='"+currentUser.getUsername()+"';";
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                Calendar calendar = new Calendar(currentUser, rs.getString("CALENDARID"));
                calendars.addCalendarToCollection(calendar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        };

        try {
            stmt = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "SELECT CALENDARID,EVENT,ID,NAME,DATE,LOCATION FROM PARTICIPATION,CALENDAREVENTS " +
                "WHERE UID='"+currentUser.getUsername()+
                "' AND CALENDAR=CALENDARID" +
                "AND EVENT=ID;";
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                Date date = new Date(2021,5,5);
                String calendar_id = rs.getString("CALENDARID");
                Calendar calendar = calendars.getCalendar(calendar_id);
                Event event = new Event(rs.getString("ID"), rs.getString("NAME"), new DateTime(rs.getTimestamp("START")),new DateTime(rs.getTimestamp("END")), rs.getString("LOCATION"));
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

        boolean check = false;
        try {
            check = resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return check;

    }

    public void addEventinEvents(Event e) {

        try {
            stmt = c.createStatement();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        Timestamp ts=new Timestamp(e.getStartDate().getMillisecond());
        Timestamp te=new Timestamp(e.getEndDate().getMillisecond());
        String sql = "INSERT INTO EVENTS(ID,NAME,START,END,LOCATION,COLOR,DESCRIPTION)" +
                "VALUES('"+e.getName()+"_ID','"+e.getName()+"','"+ts+"','"+te+"','"+e.getLocation()+"',0,'"+e.getDescription()+"');";
                //uid troppo lungo, l'ho rimpiazzato con un'altro valore
                //date sarà un problema poi riprenderlo dal database in formato Java
                //il colore non è una stringa ma un intero (ora ho messo semplicemente "0")
        System.out.println(sql);
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException exc) {
            exc.printStackTrace();
        }

    }

    public void addToCalendar(String id,String name, String owner){

        try {
            stmt = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO CALENDAR(ID,NAME,OWNER)" +
                "VALUES('"+id+"','"+name+"','"+owner+"');";

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToCalendarEvents(String calendar, String event){

        try {
            stmt = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO CALENDAREVENTS(CALENDAR,EVENT)" +
                "VALUES('"+calendar+"','"+event+"');";

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

}


