package appointment;

import domain.Customer;
import exceptions.DateLimitExceededException;
import exceptions.OverlappingAppointmentException;

import java.time.LocalDateTime;

public class BundleAppointment extends AbstractAppointment {
    private final Bundle bundle;

    public BundleAppointment(int id, Customer customer, Bundle bundle, LocalDateTime date) {
        super(id, customer, bundle.getBundlePrice(), date);
        this.bundle = bundle;
    }

    @Override
    public void book(AppointmentService service) throws DateLimitExceededException, OverlappingAppointmentException {
        service.bookAppointment(this);
    }

    @Override
    public String toString() {
        return "BundleAppointment{" +
                "bundlePrice=" + bundle.getBundlePrice() +
                '}';
    }
}
