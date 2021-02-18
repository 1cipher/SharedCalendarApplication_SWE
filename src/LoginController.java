import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;

public class LoginController {

    Login logView;
    Register regView;
    MainWindow mainView;
    Database model;

    public LoginController(Login lview, Database model, Register rview, MainWindow mview) {
        this.logView = lview;
        this.model = model;
        this.regView = rview;
        this.mainView = mview;

        this.logView.addLoginListener(new LoginListener());

        this.logView.addRegisterListener(new RegisterListener());

        this.regView.addListener(new RegViewListener());
    }

    class LoginListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String acquiredUser = logView.getUsername();
            String acquiredPassword = logView.getPassword();
            try {
                Boolean check = model.checkUserPresence(acquiredUser,acquiredPassword);
                if(check){

                    mainView = new MainWindow();
                    mainView.setVisible(true);
                    logView.setVisible(false);
                    regView.setVisible(false);
                }
                else
                    logView.showDeniedAccess();
            } catch (SQLException ex) {

                logView.showDeniedAccess();
            }


        }
    }

    class RegisterListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            regView = new Register();
            regView.setVisible(true);



        }
    }

    class RegViewListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String newUser = regView.getUsername();
            String newPassword = regView.getPassword();

            try {
                model.registerNewUser(newUser,newPassword);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }


        }
    }

}


