
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

    public Boolean checkUserPresence(String acquiredUser,String acquiredPassword) throws SQLException { //TODO: LO STATEMENT NON AGGIORNA IL DB

        String sql = "SELECT UID" +
                "FROM LOGIN " +
                "WHERE UID = ? AND PASS = ?";

        pstmt = c.prepareStatement(sql);
        pstmt.setString(1,acquiredUser);
        pstmt.setString(2,acquiredPassword);
        Boolean result = pstmt.execute();

        return result;
    }

    public void registerNewUser(String username,String password) throws SQLException {  //TODO: LO STATEMENT NON AGGIORNA IL DB

        String sql = "INSERT INTO login(uid,password)" +
                "VALUES(?,?)";

        pstmt = c.prepareStatement(sql);
        pstmt.setString(1,username);
        pstmt.setString(2,password);
        pstmt.executeUpdate();
        c.commit();




    }
}


