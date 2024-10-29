package domain;

import appointment.Appointment;
import appointment.AppointmentService;
import exceptions.CustomerExistsException;
import utils.AppointmentListener;
import utils.CustomerRegistrationListener;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Future;

public class BeautySalon implements Serializable {
    private final Set<Customer> customers = new HashSet<>();
    private AppointmentService appointmentService;
    private final ArrayList<CustomerRegistrationListener> listeners = new ArrayList<CustomerRegistrationListener>();
    private final ArrayList<AppointmentListener> appointmentListeners = new ArrayList<>();

    public BeautySalon() {
        listeners.add(customer -> System.out.println("Customer registered: " + customer.getName()));

        appointmentListeners.add(new AppointmentListener() {
            @Override
            public void onAppointmentBooked(Appointment appointment) {
                System.out.println("Appointment booked for " + appointment.getCustomer().getName() + " on " + appointment.getAppointmentDate());
            }
        });
    }

    public void addCustomer(Customer customer) throws CustomerExistsException {
        if (customers.contains(customer)) {
            throw new CustomerExistsException("Customer already exists");
        }

        customers.add(customer);
        notifyCustomerRegistered(customer);
    }

    private void notifyCustomerRegistered(Customer customer) {
        for (CustomerRegistrationListener listener : listeners) {
            listener.onCustomerRegistered(customer);
        }
    }

    public void bookAppointment(Appointment appointment) throws Exception {
        Future<Void> future = appointmentService.bookAppointment(appointment);
        future.get();  // Ensure the appointment is processed
        notifyAppointmentBooked(appointment);
    }

    private void notifyAppointmentBooked(Appointment appointment) {
        for (AppointmentListener listener : appointmentListeners) {
            listener.onAppointmentBooked(appointment);
        }
    }

    public Set<Customer> getCustomers() {
        return Collections.unmodifiableSet(customers);
    }

    public Customer getCustomer(String name) {
        for (Customer customer : customers)
            if (customer.getName().equals(name))
                return customer;
        return null;
    }

    public List<Appointment> getAppointments() {
        return appointmentService.getAppointments();
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
}
