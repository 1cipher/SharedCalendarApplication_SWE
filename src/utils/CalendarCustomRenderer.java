package utils;

import model.Calendar;

import javax.swing.*;
import java.awt.*;

public class CalendarCustomRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){

        if (value instanceof Calendar){
            Calendar calendar = (Calendar) value;
            value = calendar.getName();
        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    }

}
