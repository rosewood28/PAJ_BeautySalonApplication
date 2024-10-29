package tests;

import appointment.Appointment;
import appointment.AppointmentService;
import appointment.Bundle;
import appointment.BundleAppointment;
import domain.*;
import exceptions.CustomerExistsException;
import exceptions.DateLimitExceededException;
import exceptions.OverlappingAppointmentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.BeautySalonReportCollections;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportTest {
    private BeautySalon beautySalon;
    private AppointmentService appointmentService;
    private BeautySalonReportCollections beautySalonReport;

    @Before
    public void setUp() throws CustomerExistsException, OverlappingAppointmentException, DateLimitExceededException {
        beautySalon = new BeautySalon();
        appointmentService = new AppointmentService();
        beautySalon.setAppointmentService(appointmentService);
        //beautySalonReport = new BeautySalonReportStreams(beautySalon);
        beautySalonReport = new BeautySalonReportCollections(beautySalon);
        // Add sample customers
        Customer customer1 = new Customer("Jane Doe", Gender.FEMALE, "076567263");
        Customer customer2 = new Customer("John Smith", Gender.MALE, "076567264");
        Customer customer3 = new Customer("Alice Johnson", Gender.FEMALE, "076567265");

        beautySalon.addCustomer(customer1);
        beautySalon.addCustomer(customer2);
        beautySalon.addCustomer(customer3);

        // Add sample appointments
        SalonService hairCutService = new HairService(HairServiceType.CUT, 50.0);
        SalonService hairWashService = new HairService(HairServiceType.STYLE, 30.0);
        Bundle bundle = new Bundle("cut_style_bundle", Arrays.asList(hairCutService, hairWashService), 60);


        try {
            appointmentService.bookAppointment(new BundleAppointment(1, customer1, bundle, LocalDateTime.now().plusDays(1))).get();
            appointmentService.bookAppointment(new BundleAppointment(2, customer2, bundle, LocalDateTime.now().plusDays(8))).get();
            appointmentService.bookAppointment(new BundleAppointment(3, customer3, bundle, LocalDateTime.now().plusDays(9))).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @After
    public void tearDown() {
        appointmentService.shutdown();
    }

    @Test
    public void testGetNumberOfCustomers() {
        int numberOfCustomers = beautySalonReport.getNumberOfCustomers();
        assertEquals(3, numberOfCustomers);
    }

    @Test
    public void testGetCustomersAlphabetical() {
        Set<Customer> customers = beautySalon.getCustomers();
        List<Customer> sortedCustomers = beautySalonReport.getCustomersAlphabetical(customers);
        assertEquals(3, sortedCustomers.size());
        assertEquals("Alice Johnson", sortedCustomers.get(0).getName());
        assertEquals("Jane Doe", sortedCustomers.get(1).getName());
        assertEquals("John Smith", sortedCustomers.get(2).getName());
    }

    @Test
    public void testGetAppointmentsForCustomer() throws CustomerExistsException {
        Customer customer = new Customer("Jane Doe", Gender.FEMALE, "076567263");
        List<Appointment> appointments = beautySalonReport.getAppointmentsForCustomer(customer);
        assertEquals(1, appointments.size());
        assertEquals(customer, appointments.get(0).getCustomer());
    }

    @Test
    public void testGetAppointmentsForToday() {
        List<Appointment> appointments = appointmentService.getAppointments();
        List<Appointment> todaysAppointments = beautySalonReport.getAppointmentsForToday();
        assertTrue(todaysAppointments.isEmpty());  // Assuming no appointments are booked for today in setup
    }

    @Test
    public void testGetTotalAppointmentsForNextWeek() {
        long totalAppointmentsNextWeek = beautySalonReport.getTotalAppointmentsForNextWeek();
        assertEquals(2, totalAppointmentsNextWeek);  // Two appointments are booked for next week in setup
    }

    @Test
    public void testGetNextWeekAppointmentsSorted() {
        List<Appointment> sortedAppointments = beautySalonReport.getNextWeekAppointmentsSorted();
        assertEquals(2, sortedAppointments.size());
        assertTrue(sortedAppointments.get(0).getAppointmentDate().isBefore(sortedAppointments.get(1).getAppointmentDate()));
    }
}
