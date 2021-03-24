package view;

import com.mindfusion.common.DateTime;

import javax.swing.*;

public class DateTextField extends JTextField {

    private int year;
    private int month;
    private int day;

    public void setText(DateTime date){
        year = date.getYear();
        month = date.getMonth();
        String monthString = String.format("%02d",month);
        day = date.getDay();
        String dayString = String.format("%02d",day);
        this.setText(year+"-"+monthString+"-"+dayString);
    }

    public DateTime getDate(){
        DateTime dateTime = new DateTime(year-1900,month,day);
        return dateTime;
    }

}
