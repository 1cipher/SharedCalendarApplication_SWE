package view;

import com.mindfusion.common.DateTime;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HourComboBox extends JComboBox<String> {

    private int hour;
    private int minute;
    private DateFormat format;

    public HourComboBox(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);

        java.util.Calendar end = java.util.Calendar.getInstance();
        end.set(java.util.Calendar.HOUR_OF_DAY, 23);
        end.set(java.util.Calendar.MINUTE, 59);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        do {
            format = new SimpleDateFormat("HH:mm");
            model.addElement(format.format(calendar.getTime()));
            calendar.add(java.util.Calendar.MINUTE, 15);
        } while (calendar.getTime().before(end.getTime()));
        this.setModel(model);
    }

    public void setTime(DateTime dateTime){
        this.hour = dateTime.getHour();
        this.minute = dateTime.getMinute();
        String hourString = String.format("%02d",hour);
        String minuteString = String.format("%02d",minute);
        this.setSelectedItem(hourString+":"+minuteString);
    }

    public DateTime getTime(){
        DateTime dateTime = new DateTime(0);
        dateTime = dateTime.addHours(hour).addMinutes(minute);
        return dateTime;
    }

    @Override
    protected void selectedItemChanged() {
        try {
            Date d = format.parse((String )this.getSelectedItem());
            hour=d.getHours();
            minute=d.getMinutes();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.selectedItemChanged();
    }
}
