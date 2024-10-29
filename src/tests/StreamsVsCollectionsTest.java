package tests;

import appointment.AppointmentService;
import appointment.Bundle;
import appointment.BundleAppointment;
import appointment.Appointment;
import domain.*;
import exceptions.CustomerExistsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.BeautySalonReportCollections;
import service.BeautySalonReportStreams;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class StreamsVsCollectionsTest {
    private BeautySalon beautySalon;
    private AppointmentService appointmentService;
    private BeautySalonReportStreams streamReport;
    private BeautySalonReportCollections collectionReport;

    @Before
    public void setUp() throws CustomerExistsException, Exception {
        beautySalon = new BeautySalon();
        appointmentService = new AppointmentService();
        beautySalon.setAppointmentService(appointmentService);

        streamReport = new BeautySalonReportStreams(beautySalon);
        collectionReport = new BeautySalonReportCollections(beautySalon);

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
        int collectionReportNumberOfCustomers = collectionReport.getNumberOfCustomers();
        int streamReportNumberOfCustomers = streamReport.getNumberOfCustomers();
        assertEquals(collectionReportNumberOfCustomers, streamReportNumberOfCustomers);
    }


    @Test
    public void testGetCustomersAlphabetical() {
        Set<Customer> customers = beautySalon.getCustomers();
        List<Customer> streamSortedCustomers = streamReport.getCustomersAlphabetical(customers);
        List<Customer> collectionSortedCustomers = collectionReport.getCustomersAlphabetical(customers);
        assertEquals(streamSortedCustomers, collectionSortedCustomers);
    }

    @Test
    public void testGetAppointmentsForCustomer() {
        Customer customer = new Customer("Jane Doe", Gender.FEMALE, "076567263");
        List<Appointment> streamAppointments = streamReport.getAppointmentsForCustomer(customer);
        List<Appointment> collectionAppointments = collectionReport.getAppointmentsForCustomer(customer);
        assertEquals(streamAppointments, collectionAppointments);
    }

    @Test
    public void testGetAppointmentsForToday() {
        List<Appointment> streamTodaysAppointments = streamReport.getAppointmentsForToday();
        List<Appointment> collectionTodaysAppointments = collectionReport.getAppointmentsForToday();
        assertEquals(streamTodaysAppointments, collectionTodaysAppointments);
    }

    @Test
    public void testGetTotalAppointmentsForNextWeek() {
        long streamTotalNextWeek = streamReport.getTotalAppointmentsForNextWeek();
        long collectionTotalNextWeek = collectionReport.getTotalAppointmentsForNextWeek();
        assertEquals(streamTotalNextWeek, collectionTotalNextWeek);
    }

    @Test
    public void testGetNextWeekAppointmentsSorted() {
        List<Appointment> streamSortedAppointments = streamReport.getNextWeekAppointmentsSorted();
        List<Appointment> collectionSortedAppointments = collectionReport.getNextWeekAppointmentsSorted();
        assertEquals(streamSortedAppointments, collectionSortedAppointments);
    }
}
