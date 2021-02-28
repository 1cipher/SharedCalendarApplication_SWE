import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginController {

    public void setLogView(Login logView) {
        this.logView = logView;
    }

    Login logView;
    Register regView;
    MainWindow mainView;
    Database model;
    Dialog dialog = null;

    public LoginController(Login lview, Database model, Register rview, MainWindow mview) {
        this.logView = lview;
        this.model = model;
        this.regView = rview;
        this.mainView = mview;

        this.logView.addLoginListener(new LoginListener());

        this.logView.addRegisterListener(new RegisterListener());

        //this.regView.addListener(new RegViewListener());
    }

    class LoginListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String acquiredUser = logView.getUsername();
            String acquiredPassword = logView.getPassword();
            try {
                Boolean check = model.checkUserPresence(acquiredUser,acquiredPassword);
                if(check){

                    mainView.setVisible(true);
                    logView.setVisible(false);
                    regView.setVisible(false);
                    User user = new User(acquiredUser);
                    model.setCurrentUser(user);
                    mainView.loadView(model.getCurrentUserCalendars());
                    dialog = new Dialog.Builder().setDialogTitle("LoginSuccessful!")
                            .setColor(Color.green)
                            .setLabel("You are logged in!")
                            .build();
                    dialog.setVisible(true);



                }
                else{
                    dialog = new Dialog.Builder().setDialogTitle("Access denied")
                            .setLabel("Your username or password is wrong")
                            .setColor(Color.red)
                            .build();
                    dialog.setVisible(true);
                }


            } catch (SQLException ex) {
                dialog = new Dialog.Builder().setDialogTitle("Access denied")
                        .setLabel("Your username or password is wrong")
                        .setColor(Color.red)
                        .build();
                dialog.setVisible(true);
            }


        }
    }

    class RegisterListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            //regView.setVisible(true);
            regView = new Register();                     //TODO: LO SWITCH TRA VISTE DOVREBBE ESSERE GESTITO COSì,SENZA VISTE GLOBALI
            regView.setVisible(true);
            regView.addListener(new RegViewListener());

        }
    }

    class RegViewListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String newUser = regView.getUsername();
            String newPassword = regView.getPassword();

            if (newUser.length()==0 || newPassword.length() == 0) {

                dialog = new Dialog.Builder().setDialogTitle("Register Problem")
                        .setLabel("Username or password fields are empty")
                        .setColor(Color.red)
                        .build();
                dialog.setVisible(true);
            }

            else if (model.isExistingUsername(newUser)) {

                dialog = new Dialog.Builder().setDialogTitle("Register Problem")
                        .setLabel("Username already present")
                        .setColor(Color.red)
                        .build();
                dialog.setVisible(true);
            }

            else{

                model.registerNewUser(newUser,newPassword);
                dialog = new Dialog.Builder().setDialogTitle("Successfull sign up!")
                        .setLabel("You are being registered on our system!")
                        .setColor(Color.green)
                        .build();
                dialog.setVisible(true);
            }


        }
    }




}


