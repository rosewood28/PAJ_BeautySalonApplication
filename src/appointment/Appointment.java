package appointment;

import domain.Customer;
import exceptions.DateLimitExceededException;
import exceptions.OverlappingAppointmentException;

import java.time.LocalDateTime;

public interface Appointment {
    int getId();
    Customer getCustomer();
    double getPrice();
    LocalDateTime getAppointmentDate();
    void book(AppointmentService service) throws DateLimitExceededException, OverlappingAppointmentException;
}
