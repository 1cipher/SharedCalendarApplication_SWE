
import controller.MWController;
import model.Database;
import model.Gateway;
import view.Login;

import javax.swing.*;

public class Main {

     public static void main(String args[]) {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 try {
                     Login log = new Login();

                     log.setVisible(true);

                     Database db = new Database();
                     Gateway g = new Gateway(db.createConnection());

                     MWController mwc = new MWController(log,g);

                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });
    }
}