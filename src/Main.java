
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
                     db.createConnection();

                     MWController mwc = new MWController(mw,cw,log,reg,db);

                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });
    }
}