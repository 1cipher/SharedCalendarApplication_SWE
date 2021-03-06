package listener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class RegViewListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        String newUser = regView.getUsername();
        String newPassword = regView.getPassword();

        if (newUser.length() == 0 || newPassword.length() == 0) {

            dialog = new view.Dialog.Builder().setDialogTitle("view.Register Problem")
                    .setLabel("Username or password fields are empty")
                    .setColor(Color.red)
                    .build();
            dialog.setVisible(true);
            dialog.addDialogListener(new dialogListener());
        } else if (model.isExistingUsername(newUser)) {

            dialog = new view.Dialog.Builder().setDialogTitle("view.Register Problem")
                    .setLabel("Username already present")
                    .setColor(Color.red)
                    .build();
            dialog.setVisible(true);
            dialog.addDialogListener(new dialogListener());
        } else {

            model.registerNewUser(newUser, newPassword);
            dialog = new view.Dialog.Builder().setDialogTitle("Successfull sign up!")
                    .setLabel("You have been registered on our system!")
                    .setColor(Color.green)
                    .build();
            dialog.setVisible(true);
            dialog.addDialogListener(new dialogListener());
        }


    }
}
