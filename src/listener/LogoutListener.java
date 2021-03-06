package listener;

import view.Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LogoutListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        mwView.setVisible(false);
        mwView.dispose();
        logView = new Login();
        logView.setVisible(true);
        attachLoginWindow();

    }
}