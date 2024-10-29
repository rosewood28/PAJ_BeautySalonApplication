package service;

import appointment.Appointment;
import domain.BeautySalon;
import domain.Customer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class BeautySalonReportCollections {
    private BeautySalon beautySalon;

    public BeautySalonReportCollections(BeautySalon beautySalon) {
        this.beautySalon = beautySalon;
    }

    public BeautySalon getBeautySalon() {
        return beautySalon;
    }

    public int getNumberOfCustomers() {
        return beautySalon.getCustomers().size();
    }

    public List<Customer> getCustomersAlphabetical(Set<Customer> customers) {
        List<Customer> customerList = new ArrayList<>(customers);
        Collections.sort(customerList, new Comparator<Customer>() {
            @Override
            public int compare(Customer c1, Customer c2) {
                return c1.getName().compareToIgnoreCase(c2.getName());
            }
        });
        return customerList;
    }

    public List<Appointment> getAppointmentsForCustomer(Customer customer) {
        List<Appointment> customerAppointments = new ArrayList<>();
        for (Appointment appointment : beautySalon.getAppointments()) {
            if (appointment.getCustomer().equals(customer)) {
                customerAppointments.add(appointment);
            }
        }
        return customerAppointments;
    }

    public List<Appointment> getAppointmentsForToday() {
        List<Appointment> todaysAppointments = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Appointment appointment : beautySalon.getAppointments()) {
            if (appointment.getAppointmentDate().toLocalDate().equals(today)) {
                todaysAppointments.add(appointment);
            }
        }
        return todaysAppointments;
    }

    public long getTotalAppointmentsForNextWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfNextWeek = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY)); // Start of next week (Monday)
        LocalDate endOfNextWeek = startOfNextWeek.plusDays(6); // End of next week (Sunday)

        long count = 0;
        for (Appointment appointment : beautySalon.getAppointments()) {
            LocalDate appointmentDate = appointment.getAppointmentDate().toLocalDate();
            if (!appointmentDate.isBefore(startOfNextWeek) && !appointmentDate.isAfter(endOfNextWeek)) {
                count++;
            }
        }
        return count;
    }

    public List<Appointment> getNextWeekAppointmentsSorted() {
        LocalDate today = LocalDate.now();
        LocalDate startOfNextWeek = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY)); // Start of next week (Monday)
        LocalDate endOfNextWeek = startOfNextWeek.plusDays(6); // End of next week (Sunday)

        List<Appointment> nextWeekAppointments = new ArrayList<>();
        for (Appointment appointment : beautySalon.getAppointments()) {
            LocalDate appointmentDate = appointment.getAppointmentDate().toLocalDate();
            if (!appointmentDate.isBefore(startOfNextWeek) && !appointmentDate.isAfter(endOfNextWeek)) {
                nextWeekAppointments.add(appointment);
            }
        }
        Collections.sort(nextWeekAppointments, new Comparator<Appointment>() {
            @Override
            public int compare(Appointment a1, Appointment a2) {
                return a1.getAppointmentDate().compareTo(a2.getAppointmentDate());
            }
        });
        return nextWeekAppointments;
    }
}

