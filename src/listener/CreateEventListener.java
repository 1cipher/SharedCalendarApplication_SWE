package listener;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.model.Appointment;
import com.mindfusion.scheduling.model.Item;
import com.mindfusion.scheduling.model.Location;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

class CreateEventListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        String uid = java.util.UUID.randomUUID().toString().substring(0, 19);
        String name = cwView.name.getText();
        String location = cwView.location.getText();
        String descr = cwView.descr.getText();
        String startdate = cwView.startDate.getText();
        String enddate = cwView.endDate.getText();

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String startHour = (String) cwView.startHour.getSelectedItem();
        String endHour = (String) cwView.endHour.getSelectedItem();
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = dateFormatter.parse(startdate);
            date2 = dateFormatter.parse(enddate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        java.util.Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(date1);
        java.util.Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(date2);

        DateTime startDate = new DateTime(calendar1.get(java.util.Calendar.YEAR) - 1900, calendar1.get(java.util.Calendar.MONTH) + 1, calendar1.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(startHour.substring(0, 2)), Integer.parseInt(startHour.substring(3, 5)), 0);//TODO: PROBLEMI QUANDO NON SI SELEZIONA LA DATA
        DateTime endDate = new DateTime(calendar2.get(java.util.Calendar.YEAR) - 1900, calendar2.get(java.util.Calendar.MONTH) + 1, calendar2.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(endHour.substring(0, 2)), Integer.parseInt(endHour.substring(3, 5)), 0); //TODO:COLLEZIONARE GLI ORARI DAI RISPETTIVI TEXTFIELD
        if (!name.isEmpty() && !uid.isEmpty() && !startDate.toString().isEmpty() && !endDate.toString().isEmpty()) {
            dialog = new view.Dialog.Builder().setColor(Color.green).setLabel("model.Event Created!").setDialogTitle("model.Event").build();
            dialog.setVisible(true);
            dialog.addDialogListener(new MWController.dialogListener());
        } else {
            dialog = new view.Dialog.Builder().setColor(Color.red).setLabel("Check null values").setDialogTitle("model.Event").build();
            dialog.setVisible(true);
            dialog.addDialogListener(new MWController.dialogListener());
        }

        Event event = new Event(uid, name, startDate, endDate, location, descr);
        model.addEventinEvents(event, cwView.getCurrentCalendar().getId());
        Item appointment = new Appointment();
        appointment.setStartTime(new DateTime(calendar1.get(java.util.Calendar.YEAR), calendar1.get(java.util.Calendar.MONTH) + 1, calendar1.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(startHour.substring(0, 2)), Integer.parseInt(startHour.substring(3, 5)), 00));
        appointment.setEndTime(new DateTime(calendar2.get(java.util.Calendar.YEAR), calendar2.get(java.util.Calendar.MONTH) + 1, calendar2.get(java.util.Calendar.DAY_OF_MONTH), Integer.parseInt(endHour.substring(0, 2)), Integer.parseInt(endHour.substring(3, 5)), 00));
        appointment.setHeaderText(name);
        appointment.setId(uid);
        appointment.setDescriptionText(descr);
        Location loc = new Location();
        loc.setName(location);
        appointment.setLocation(loc);
        CalendarCollection cc = new CalendarCollection();
        cc.addCalendarToCollection(mwView.getCurrentCalendar());
        mwView.calendar.getSchedule().getAllItems().clear();
        loadView(cc);
        cwView.setVisible(false);
        cwView.dispose();


    }
}

