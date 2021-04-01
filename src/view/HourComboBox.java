package view;

import com.mindfusion.common.DateTime;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HourComboBox {

    private int hour;
    private int minute;
    private DateFormat format;
    private JComboBox<String> stringJComboBox;

    public HourComboBox(){

        stringJComboBox = new JComboBox<>();
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
        stringJComboBox.setModel(model);
    }


    public void setTime(DateTime dateTime){
        this.hour = dateTime.getHour();
        this.minute = dateTime.getMinute();
        String hourString = String.format("%02d",hour);
        String minuteString = String.format("%02d",minute);
        stringJComboBox.setSelectedItem(hourString+":"+minuteString);

    }

    public DateTime getTime(){
        Date d = null;
        try {
            d = format.parse((String )stringJComboBox.getSelectedItem());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        hour=d.getHours();
        minute=d.getMinutes();
        DateTime dateTime = new DateTime(0);
        dateTime = dateTime.addHours(hour).addMinutes(minute);
        return dateTime;
    }

    public JComboBox<String> getStringJComboBox() {
        return stringJComboBox;
    }

}
