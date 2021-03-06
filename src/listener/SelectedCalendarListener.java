package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectedCalendarListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        mwView.calendar.getSchedule().getAllItems().clear();
        CalendarCollection cc = new CalendarCollection();
        cc.addCalendarToCollection(mwView.getCurrentCalendar());
        loadView(cc);

    }
}
