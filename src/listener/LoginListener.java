package listener;

import view.MainWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

class LoginListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        String acquiredUser = logView.getUsername();
        String acquiredPassword = logView.getPassword();
        try {
            boolean check = model.checkUserPresence(acquiredUser, acquiredPassword);
            if (check) {

                mwView = new MainWindow();
                mwView.setVisible(true);
                attachMainWindow();
                if (regView != null) {

                    regView.setVisible(false);
                    regView.dispose();
                }
                logView.setVisible(false);
                logView.dispose();

                User user = new User(acquiredUser);
                model.setCurrentUser(user);
                loadView(model.getCurrentUserCalendars());
                mwView.populateCalendars(model.getCurrentUserCalendars());
                dialog = new view.Dialog.Builder().setDialogTitle("LoginSuccessful!")
                        .setColor(Color.green)
                        .setLabel("You are logged in!")
                        .build();

            } else {
                dialog = new view.Dialog.Builder().setDialogTitle("Access denied")
                        .setLabel("Your username or password is wrong")
                        .setColor(Color.red)
                        .build();
            }
            dialog.setVisible(true);
            dialog.addDialogListener(new dialogListener());


        } catch (SQLException ex) {
            dialog = new view.Dialog.Builder().setDialogTitle("Access denied")
                    .setLabel("Your username or password is wrong")
                    .setColor(Color.red)
                    .build();
            dialog.setVisible(true);
            dialog.addDialogListener(new dialogListener());
        }


    }
}
