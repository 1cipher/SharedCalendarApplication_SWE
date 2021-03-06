
import model.Database;
import model.Gateway;
import view.CalendarWindow;
import view.Login;
import view.MainWindow;
import view.Register;

import javax.swing.*;

public class Main {

     public static void main(String args[]) {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 try {
                     Login log = new Login();
                     Register reg = new Register();
                     MainWindow mw = new MainWindow();
                     CalendarWindow cw = new CalendarWindow();

                     log.setVisible(true);

                     Database db = new Database();
                     Gateway g = new Gateway(db.createConnection());

                     MWController mwc = new MWController(log,db);

                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });
    }
}