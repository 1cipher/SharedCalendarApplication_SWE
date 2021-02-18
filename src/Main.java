
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
     static Login log;
     static Register reg;
     static MainWindow mw;
     public static void main(String args[]) throws SQLException, IOException {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 try {
                     log = new Login();
                     reg = new Register();
                     mw = new MainWindow();

                     log.setVisible(true);

                     Database db = new Database();

                     LoginController lc = new LoginController(log,db,reg,mw);
                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });
    }
}