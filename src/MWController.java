import com.mindfusion.scheduling.CalendarView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MWController {

    MainWindow mwView;
    CalendarWindow cwView;
    Database model;

    public MWController(MainWindow mw, CalendarWindow cw, Database db){

        this.mwView = mw;
        this.cwView = cw;
        this.model = db;

        this.mwView.addAddEventListener(new addEventListener());
        this.mwView.addDayListener(new dayListener());
        this.mwView.addWeekListener(new weekListener());
        this.mwView.addMonthListener(new monthListener());
        this.mwView.addSearchListener(new searchListener());


    }

    class addEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class  dayListener implements  ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            mwView.calendar.setCurrentView(CalendarView.Timetable);

        }
    }

    class  weekListener implements  ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            mwView.calendar.setCurrentView(CalendarView.WeekRange);

        }
    }

    class  monthListener implements  ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            mwView.calendar.setCurrentView(CalendarView.MonthRange);

        }
    }

    class searchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

}
