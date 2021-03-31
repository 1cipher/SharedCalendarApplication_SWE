package view;

import com.mindfusion.common.DateTime;

import javax.swing.*;

public class DateTextField {

    private int year;
    private int month;
    private int day;
    private JTextField textField;

    public DateTextField(){

        textField = new JTextField();
    }

    public void setDate(DateTime date){
        year = date.getYear();
        month = date.getMonth();
        String monthString = String.format("%02d",month);
        day = date.getDay();
        String dayString = String.format("%02d",day);
        textField.setText(year+"-"+monthString+"-"+dayString);
    }

    public DateTime getDate(){
        
        return new DateTime(year-1900,month,day);

    }

    public JTextField getTextField(){
        return textField;
    }

}
