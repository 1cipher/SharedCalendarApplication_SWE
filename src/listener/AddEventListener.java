package listener;

import view.CalendarWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AddEventListener implements ActionListener {

    CalendarWindow cwView;

    @Override
    public void actionPerformed(ActionEvent e) {

        cwView = new CalendarWindow();
        cwView.setVisible(true);
        cwView.populateCalendars(model.getCurrentUserCalendars());
        attachCalendarWindow();

    }
}