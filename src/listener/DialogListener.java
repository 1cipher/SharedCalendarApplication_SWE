package listener;

import view.Dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class DialogListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(false);
        dialog.dispose();

    }
}
