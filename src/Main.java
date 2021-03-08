
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

                     Database db = new Database();
                     Gateway g = new Gateway(db.createConnection());

                     MWController mwc = MWController.getInstance();
                     MWController.setDatabase(g);

                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });
    }
}