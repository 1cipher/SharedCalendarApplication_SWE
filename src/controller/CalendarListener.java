package controller;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.Calendar;
import view.EditEventWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class CalendarListener implements MouseListener {

    private EditEventWindow observer;

    public CalendarListener(EditEventWindow cwView){
        observer = cwView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //always handled by press and release action

    }

    @Override
    public void mousePressed(MouseEvent e) {

        Calendar calendar = ((Calendar)(e.getSource()));
        calendar.getSelection().reset();
        DateTime pointedDate = calendar.getDateAt(e.getX(), e.getY());
        observer.setStartDate(pointedDate);

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        Calendar calendar = ((Calendar)(e.getSource()));

        calendar.getSelection().reset();

        DateTime pointedDate = calendar.getDateAt(e.getX(), e.getY());
        DateTime defaultDate = new DateTime(1,1,1);

        if (observer.getStartDate().isLessThanOrEqual(defaultDate)) {
            observer.setEndDate(observer.getStartDate());
            observer.setStartDate(observer.getStartDate());
        }

        else
            observer.setEndDate(pointedDate);

    }

    @Override
    public void mouseEntered(MouseEvent e) {

        //no need to handle this,it only matters when mouse is inside the calendar
    }

    @Override
    public void mouseExited(MouseEvent e) {

        //no need to handle this,it only matters when mouse is inside the calendar

    }
}