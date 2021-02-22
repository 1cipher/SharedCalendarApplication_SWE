import com.mindfusion.scheduling.CalendarView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MWController {

    MainWindow mwView;
    Database model;

    public MWController(MainWindow mw, Database db){

        this.mwView = mw;
        this.model = db;

        this.mwView.addChangeViewListener(new changeViewListener());
        this.mwView.addAddEventListener(new addEventListener());
        this.mwView.addSearchListener(new searchListener());


    }

    class addEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            CalendarWindow cw = new CalendarWindow();
            cw.setVisible(true);

        }
    }

    class searchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class changeViewListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String selection = (String)((JComboBox<String>)e.getSource()).getSelectedItem();

            if(selection.equals("day"))
                mwView.calendar.setCurrentView(CalendarView.Timetable);
            else if(selection.equals("week"))
                mwView.calendar.setCurrentView(CalendarView.WeekRange);
            else if(selection.equals("month"))
                mwView.calendar.setCurrentView(CalendarView.MonthRange);



        }
    }

}
