package controller;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.Calendar;
import view.EditEventView;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class CalendarListener implements MouseListener {

    private JFrame view;

    public CalendarListener(JFrame cwView){
        view = cwView;
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
        ((EditEventView)view).setStartDate(pointedDate);

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        Calendar calendar = ((Calendar)(e.getSource()));

        calendar.getSelection().reset();

        DateTime pointedDate = calendar.getDateAt(e.getX(), e.getY());
        DateTime defaultDate = new DateTime(1,1,1);

        DateTime startDate = ((EditEventView)view).getStartDate();
        if (startDate.isLessThanOrEqual(defaultDate)) {
            ((EditEventView)view).setEndDate(startDate);
            ((EditEventView)view).setStartDate(startDate);
        }

        else
            ((EditEventView)view).setEndDate(pointedDate);

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