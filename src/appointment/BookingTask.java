package appointment;

import exceptions.DateLimitExceededException;
import exceptions.OverlappingAppointmentException;

public class BookingTask implements Runnable {
    private final AppointmentService service;
    private final Appointment appointment;

    public BookingTask(AppointmentService service, Appointment appointment) {
        this.service = service;
        this.appointment = appointment;
    }

    @Override
    public void run() {
        try {
            service.bookAppointment(appointment);
        } catch (DateLimitExceededException | OverlappingAppointmentException e) {
            System.out.println("Failed to book appointment: " + e.getMessage());
        }
    }
}
