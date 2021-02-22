
import javax.swing.*;

public class Main {
     static Login log;
     static Register reg;
     static MainWindow mw;
     public static void main(String args[]) {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 try {
                     log = new Login();
                     reg = new Register();
                     mw = new MainWindow();

                     log.setVisible(true);

                     Database db = new Database();
                     db.createConnection();

                     LoginController lc = new LoginController(log,db,reg,mw);
                     MWController mwc = new MWController(mw,db);

                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });
    }
}