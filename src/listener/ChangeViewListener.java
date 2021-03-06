package listener;

import com.mindfusion.scheduling.CalendarView;
import com.mindfusion.scheduling.WeekRangeHeaderStyle;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

class ChangeViewListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        String selection = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();

        if (selection.equals("day"))
            mwView.calendar.setCurrentView(CalendarView.Timetable);
        else if (selection.equals("week")) {
            mwView.calendar.setCurrentView(CalendarView.WeekRange);
            mwView.calendar.getWeekRangeSettings().setHeaderStyle(EnumSet.of(WeekRangeHeaderStyle.Title));
        } else if (selection.equals("month"))
            mwView.calendar.setCurrentView(CalendarView.MonthRange);


    }

}
