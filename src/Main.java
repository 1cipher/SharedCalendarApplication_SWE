
import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
     static Login log;
     public static void main(String args[]) throws SQLException, IOException {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 try {
                     log = new Login();
                     log.setVisible(true);
                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });
    }
}