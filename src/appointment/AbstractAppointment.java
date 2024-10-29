package appointment;

import domain.Customer;
import exceptions.DateLimitExceededException;
import exceptions.OverlappingAppointmentException;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AbstractAppointment implements Appointment {
    private final int id;
    private final Customer customer;
    private double price;
    private LocalDateTime appointmentDate;

    public AbstractAppointment(int id, Customer customer, double price, LocalDateTime appointmentDate) {
        this.id = id;
        this.price = price;
        this.customer = customer;
        this.appointmentDate = appointmentDate;
    }

    @Override
    public abstract void book(AppointmentService service) throws DateLimitExceededException, OverlappingAppointmentException;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAppointment that = (AbstractAppointment) o;
        return getId() == that.getId() && Objects.equals(getCustomer(), that.getCustomer()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getAppointmentDate(), that.getAppointmentDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCustomer(), getPrice(), getAppointmentDate());
    }


}
