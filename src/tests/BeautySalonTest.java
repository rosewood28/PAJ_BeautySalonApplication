package tests;
import appointment.*;
import domain.*;
import exceptions.CustomerExistsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class BeautySalonTest {
    private BeautySalon beautySalon;
    private AppointmentService appointmentService;

    @Before
    public void setUp() {
        beautySalon = new BeautySalon();
        appointmentService = new AppointmentService();
        beautySalon.setAppointmentService(appointmentService);
    }

    @After
    public void tearDown() {
        appointmentService.shutdown();
    }

    @Test
    public void testAddCustomer() throws CustomerExistsException {
        Customer customer = new Customer("Jane Doe", Gender.FEMALE, "076567263");
        beautySalon.addCustomer(customer);

        Set<Customer> customers = beautySalon.getCustomers();
        assertTrue(customers.contains(customer));
    }

    @Test(expected = CustomerExistsException.class)
    public void testAddExistingCustomer() throws CustomerExistsException {
        Customer customer = new Customer("Jane Doe", Gender.FEMALE, "076567263");
        beautySalon.addCustomer(customer);
        beautySalon.addCustomer(customer);  // This should throw CustomerExistsException
    }

    @Test
    public void testBookSimpleAppointment() throws Exception {
        Customer customer = new Customer("Jane Doe", Gender.FEMALE, "076567263");
        beautySalon.addCustomer(customer);

        Appointment appointment = new SimpleAppointment(1, customer, new HairService(HairServiceType.CUT, 80.00), LocalDateTime.now().plusDays(1));
        beautySalon.bookAppointment(appointment);

        // Check if the appointment is successfully booked (the appointment list should contain this appointment)
        List<Appointment> appointments = beautySalon.getAppointments();
        assertTrue(appointments.contains(appointment));
    }

    @Test
    public void testBookBundleAppointment() throws Exception {
        Customer customer = new Customer("Jane Doe", Gender.FEMALE, "076567263");
        beautySalon.addCustomer(customer);

        SalonService hairCutService = new HairService(HairServiceType.CUT, 50.0);
        SalonService hairWashService = new HairService(HairServiceType.STYLE, 30.0);
        Bundle bundle = new Bundle("wash_cut_bundle", Arrays.asList(hairCutService, hairWashService), 60.00);

        BundleAppointment appointment = new BundleAppointment(1, customer, bundle, LocalDateTime.now().plusDays(1));
        beautySalon.bookAppointment(appointment);

        // Check if the appointment is successfully booked
        List<Appointment> appointments = appointmentService.getAppointments();
        assertTrue(appointments.contains(appointment));
    }

    @Test(expected = ExecutionException.class)
    public void testBookOverlappingAppointment() throws Exception {
        Customer customer = new Customer("Jane Doe", Gender.FEMALE, "076567263");
        beautySalon.addCustomer(customer);

        LocalDateTime appointmentDate = LocalDateTime.now().plusDays(1);
        Appointment appointment1 = new SimpleAppointment(1, customer, new HairService(HairServiceType.CUT, 80.00), appointmentDate);
        Appointment appointment2 = new SimpleAppointment(2, customer, new HairService(HairServiceType.CUT, 80.00), appointmentDate);  // Same time as appointment1

        beautySalon.bookAppointment(appointment1);
        beautySalon.bookAppointment(appointment2);  // This should throw an ExecutionException due to OverlappingAppointmentException
    }

    @Test(expected = ExecutionException.class)
    public void testBookAppointmentInThePast() throws Exception {
        Customer customer = new Customer("Jane Doe", Gender.FEMALE, "076567263");
        beautySalon.addCustomer(customer);

        Appointment appointment = new SimpleAppointment(1, customer, new HairService(HairServiceType.CUT, 80.00), LocalDateTime.now().minusDays(1));  // Date in the past

        beautySalon.bookAppointment(appointment);  // This should throw an ExecutionException due to DateLimitExceededException
    }
}
