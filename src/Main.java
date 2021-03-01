
import javax.swing.*;

public class Main {
     static Login log;
     static Register reg;
     static MainWindow mw;
     static CalendarWindow cw;
     public static void main(String args[]) {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 try {
                     log = new Login();
                     reg = new Register();
                     mw = new MainWindow();
                     cw = new CalendarWindow();

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