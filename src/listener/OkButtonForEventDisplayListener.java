package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OkButtonForEventDisplayListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

        eventView.setVisible(false);
        eventView.dispose();

    }
}
