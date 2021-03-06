package listener;

import view.CreateCalendarWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CreateCalendarButtonListener implements ActionListener {

    CreateCalendarWindow createCalendarWindow;

    @Override
    public void actionPerformed(ActionEvent e) {

        createCalendarWindow = new CreateCalendarWindow();
        createCalendarWindow.setVisible(true);
        createCalendarWindow.addCreateCalendarListener(new CreateCalendarListener());

    }
}
