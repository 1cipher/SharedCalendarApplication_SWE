package utils;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.model.Item;

import javax.swing.*;
import java.awt.*;

public class EventCustomRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){

        if (value instanceof Item){
            DateTime dt = ((Item)value).getStartTime();
            value = ((Item)value).getHeaderText()+"  "+dt.getYear()+"/"+dt.getMonth()+"/"+dt.getDay();
        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    }
}
