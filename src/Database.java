
import java.sql.*;

public class Database {
    public Connection c = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;

    public Database() {

    }

    public void createConnection() throws Exception{
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/CalendarApplication",
                        "postgres", "admin");



}
    private void dropConnection() throws  Exception{

        c.close();
    }

    public void addToDB(Calendar calendar, Event e) throws SQLException {

        stmt = c.createStatement();

        String sql = "INSERT INTO CALENDAREVENTS(CALENDAR,EVENT)"+
                "VALUES("+calendar.getId()+","+e.getId()+");";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO EVENTS(ID,NAME,DATE,LOCATION,COLOR,DESCRIZIONE)"+
                "VALUES("+e.getId()+","+e.getName()+","+e.getDate()+","+e.getLocation()+","+e.getColor()+","+e.getDescription()+");";
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

    public void registerNewUser(String username,String password) throws SQLException {

        stmt = c.createStatement();
        String sql = "INSERT INTO LOGIN(UID,PASSWORD)" +
                "VALUES('"+username+"','"+password+"');";

        stmt.executeUpdate(sql);

    }

    public boolean isUsernamePresent(String username) throws SQLException {

        String sql = "SELECT * " +
                "FROM LOGIN " +
                "WHERE UID = '"+username+"'";

        stmt = c.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);

        return resultSet.next();
    }
}


