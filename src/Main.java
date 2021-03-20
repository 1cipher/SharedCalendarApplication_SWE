
import controller.Controller;
import model.Database;
import model.Gateway;

import javax.swing.*;

public class Main {

     public static void main(String args[]) {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 try {

                     Database db = Database.getInstance();
                     Gateway g = new Gateway(db.getConnection());


                     Controller mwc = new Controller(g);

                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });
    }
}