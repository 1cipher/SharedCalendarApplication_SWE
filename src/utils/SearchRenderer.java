package utils;

import controller.SearchStrategy;
import model.Calendar;

import javax.swing.*;
import java.awt.*;

public class SearchRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){

        if (value instanceof SearchStrategy){
            value = ((SearchStrategy)value).textToDisplay();
        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    }
}
