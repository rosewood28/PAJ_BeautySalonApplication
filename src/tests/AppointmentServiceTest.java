package tests;

import appointment.*;
import domain.*;
import exceptions.DateLimitExceededException;
import exceptions.OverlappingAppointmentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AppointmentServiceTest {
    private AppointmentService appointmentService;
    private Customer customer;

    @Before
    public void setUp() {
        appointmentService = new AppointmentService();
        customer = new Customer("Jane Doe", Gender.FEMALE, "076567263");
    }

    @After
    public void tearDown() {
        appointmentService.shutdown();
    }

    @Test
    public void testBookSingleAppointment() throws Exception {
        Appointment appointment = new SimpleAppointment(1, customer, new HairService(HairServiceType.CUT, 80.00), LocalDateTime.now().plusDays(1));
        appointmentService.submitBookingTask(appointment);

        // Give some time for the appointment to be processed
        sleep(1000);

        List<Appointment> appointments = appointmentService.getAppointments();
        assertEquals(1, appointments.size());
        assertEquals(appointment.getId(), appointments.get(0).getId());
    }

    @Test
    public void testBookMultipleAppointmentsConcurrently() throws Exception {
        int numberOfAppointments = 10;
        CountDownLatch latch = new CountDownLatch(numberOfAppointments);

        for (int i = 0; i < numberOfAppointments; i++) {
            int id = i + 1;
            Appointment appointment = new SimpleAppointment(id, customer,  new HairService(HairServiceType.CUT, 80.00), LocalDateTime.now().plusDays(id));
            new Thread(() -> {
                try {
                    appointmentService.submitBookingTask(appointment);
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        // Wait for all threads to finish
        latch.await();

        sleep(1000);
        List<Appointment> appointments = appointmentService.getAppointments();
        assertEquals(numberOfAppointments, appointments.size());
    }

    @Test
    public void testOverlappingAppointment() {
        Appointment appointment1 = new SimpleAppointment(1, customer,  new HairService(HairServiceType.CUT, 80.00), LocalDateTime.now().plusDays(1));
        Appointment appointment2 = new SimpleAppointment(2, customer,  new HairService(HairServiceType.CUT, 80.00), LocalDateTime.now().plusDays(1));

        try {
            appointmentService.submitBookingTask(appointment1);
        } catch (Exception e) {
            Exception exception = assertThrows(OverlappingAppointmentException.class, () -> appointmentService.submitBookingTask(appointment2));
            assertEquals("An appointment already exists at this time.", exception.getMessage());
        }

    }

    @Test
    public void testBookingAppointmentInThePast() {
        Appointment appointment = new SimpleAppointment(1, customer, new HairService(HairServiceType.CUT, 80.00), LocalDateTime.now().minusDays(1));

        try {
            appointmentService.submitBookingTask(appointment);
        } catch (Exception e) {
            Exception exception = assertThrows(DateLimitExceededException.class, () -> appointmentService.submitBookingTask(appointment));
            assertEquals("The appointment date cannot be in the past.", exception.getMessage());
        }
    }
}