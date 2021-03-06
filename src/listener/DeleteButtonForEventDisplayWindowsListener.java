package listener;

import com.mindfusion.scheduling.model.Appointment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class DeleteButtonForEventDisplayWindowsListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {


        Appointment appointment = (Appointment) mwView.calendar.getSchedule().getItems().get(eventView.getTitle());
        model.deleteEvent(appointment.getId());
        mwView.calendar.getSchedule().getItems().remove(appointment);
        mwView.calendar.repaint();
        eventView.setVisible(false);
        eventView.dispose();

        //TODO:     INTERESSANTE TROVARE UNA SOLUZIONE ALTERNATIVA PER ACQUISIRE ID

    }
}
