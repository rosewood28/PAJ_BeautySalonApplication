package appointment;

import exceptions.DateLimitExceededException;
import exceptions.OverlappingAppointmentException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AppointmentService {
    private final List<Appointment> appointments = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the pool size as needed

    //Future to be able to represent the pending of the current task untill compleition
    public Future<Void> bookAppointment(Appointment appointment) throws DateLimitExceededException, OverlappingAppointmentException {
        return executorService.submit(() -> {
            synchronized (appointments) {
                checkAppointmentDate(appointment.getAppointmentDate());
                checkForOverlappingAppointments(appointment);

                appointments.add(appointment);
            }
            return null;
        });
    }

    public void submitBookingTask(Appointment appointment) {
        executorService.submit(new BookingTask(this, appointment));
    }

    private void checkAppointmentDate(LocalDateTime appointmentDate) throws DateLimitExceededException {
        LocalDateTime now = LocalDateTime.now();
        if (appointmentDate.isBefore(now)) {
            throw new DateLimitExceededException("The appointment date cannot be in the past.");
        }
    }

    private void checkForOverlappingAppointments(Appointment newAppointment) throws OverlappingAppointmentException {
        synchronized (appointments) {
            for (Appointment existingAppointment : appointments) {
                if (existingAppointment.getAppointmentDate().equals(newAppointment.getAppointmentDate())) {
                    throw new OverlappingAppointmentException("An appointment already exists at this time.");
                }
            }
        }
    }

    public List<Appointment> getAppointments() {
        synchronized (appointments) {
            return new ArrayList<>(appointments);
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
