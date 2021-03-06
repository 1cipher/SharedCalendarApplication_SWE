package listener;

import view.Register;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class RegisterListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        //regView.setVisible(true);
        regView = new Register();                     //TODO: LO SWITCH TRA VISTE DOVREBBE ESSERE GESTITO COSì,SENZA VISTE GLOBALI
        regView.setVisible(true);
        attachRegisterWindow();

    }
}