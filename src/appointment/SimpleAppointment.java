package appointment;

import domain.Customer;
import domain.SalonService;
import exceptions.DateLimitExceededException;
import exceptions.OverlappingAppointmentException;

import java.time.LocalDateTime;

public class SimpleAppointment extends AbstractAppointment {
    private SalonService salonService;

    public SimpleAppointment(int id, Customer customer, SalonService salonService, LocalDateTime date) {
        super(id, customer, salonService.getPrice(), date);
    }

    @Override
    public void book(AppointmentService service) throws DateLimitExceededException, OverlappingAppointmentException {
        service.bookAppointment(this);
    }
}
