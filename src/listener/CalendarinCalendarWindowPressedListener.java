package listener;

import com.mindfusion.common.DateTime;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

class CalendarinCalendarWindowPressedListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getClickCount() == 2) {

            cwView.cal.getSelection().reset();

            DateTime pointedDate = cwView.cal.getDateAt(e.getX(), e.getY());

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
            Date utilDate = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            cwView.setSelectedStartDate(sqlDate);


        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

        cwView.cal.getSelection().reset();

        DateTime pointedDate = cwView.cal.getDateAt(e.getX(), e.getY());

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
        Date utilDate = cal.getTime();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        cwView.setSelectedStartDate(sqlDate);

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        cwView.cal.getSelection().reset();

        DateTime pointedDate = cwView.cal.getDateAt(e.getX(), e.getY());

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(pointedDate.getYear(), pointedDate.getMonth() - 1, pointedDate.getDay());
        Date utilDate = cal.getTime();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        if (cwView.startDate.getText().compareTo(sqlDate.toString()) > 0) {
            java.sql.Date d = java.sql.Date.valueOf(cwView.startDate.getText());
            cwView.setSelectedEndDate(d);
            cwView.setSelectedStartDate(sqlDate);
        } else
            cwView.setSelectedEndDate(sqlDate);

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}