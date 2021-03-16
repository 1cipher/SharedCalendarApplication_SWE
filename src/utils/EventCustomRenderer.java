package utils;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.model.Item;

import javax.swing.*;
import java.awt.*;

public class EventCustomRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){

        if (value instanceof Item){
            DateTime startTime = ((Item)value).getStartTime();
            DateTime endTime = ((Item)value).getEndTime();
            value = ((Item)value).getHeaderText()+"    From: "+startTime.getDay()+"/"+startTime.getMonth()+"/"+startTime.getYear()
            +"  To: "+endTime.getDay()+"/"+endTime.getMonth()+"/"+endTime.getYear();
        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    }
}
