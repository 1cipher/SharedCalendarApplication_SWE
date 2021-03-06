package model;

import utils.Private;

import java.sql.*;

public class Database {

    public Database() {

    }

    public Connection createConnection() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection c = null;
        try {
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/CalendarApplication",
                            "postgres", Private.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return c;

    }

}




