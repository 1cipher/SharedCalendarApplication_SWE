
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection c = null;

    public Database() {
    }

    private void createConnection() throws Exception{
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/CalendarApplication",
                        "postgres", "admin");

}
    private void dropConnection() throws  Exception{
        c.close();
    }
}
