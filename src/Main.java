
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

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