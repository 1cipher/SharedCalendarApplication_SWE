package listener;

import view.Dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CreateCalendarListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String cid = java.util.UUID.randomUUID().toString().substring(0, 19);
        if (!createCalendarWindow.getName().isEmpty()) {
            Calendar newCalendar = new Calendar(model.getCurrentUser(), cid, createCalendarWindow.getName());
            model.CreateCalendar(newCalendar);
            model.getCurrentUserCalendars().addCalendarToCollection(newCalendar);
            dialog = new view.Dialog.Builder().setDialogTitle("model.Calendar").setLabel("model.Calendar Created").setColor(Color.green).build();
            dialog.setVisible(true);
        } else {
            dialog = new Dialog.Builder().setDialogTitle("model.Calendar").setLabel("model.Calendar can't be created").setColor(Color.red).build();
            dialog.setVisible(true);
        }

        createCalendarWindow.setVisible(false);
        createCalendarWindow.dispose();
    }
}