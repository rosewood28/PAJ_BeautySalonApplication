package service;

import appointment.Appointment;
import domain.BeautySalon;
import domain.Customer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeautySalonReportStreams {
    private BeautySalon beautySalon;

    public BeautySalonReportStreams(BeautySalon beautySalon) { this.beautySalon = beautySalon; }

    public BeautySalon getBeautySalon() { return beautySalon; }

    public int getNumberOfCustomers() { return beautySalon.getCustomers().size(); }

    public List<Customer> getCustomersAlphabetical(Set<Customer> customers) {
        return customers.stream()
                .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsForCustomer(Customer customer) {
        return beautySalon.getAppointments().stream()
                .filter(appointment -> appointment.getCustomer().equals(customer))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsForToday() {
        LocalDate today = LocalDate.now();
        return beautySalon.getAppointments().stream()
                .filter(appointment -> appointment.getAppointmentDate().toLocalDate().equals(today))
                .collect(Collectors.toList());
    }

    public long getTotalAppointmentsForNextWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfNextWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(LocalDate.now().getDayOfWeek().getValue() == 7 ? 1 : 8 - LocalDate.now().getDayOfWeek().getValue())));
        LocalDate endOfNextWeek = startOfNextWeek.plusDays(7);

        return beautySalon.getAppointments().stream()
                .filter(appointment -> {
                    LocalDate appointmentDate = appointment.getAppointmentDate().toLocalDate();
                    return (appointmentDate.isEqual(startOfNextWeek) || appointmentDate.isAfter(startOfNextWeek)) &&
                            (appointmentDate.isEqual(endOfNextWeek) || appointmentDate.isBefore(endOfNextWeek));
                })
                .count();
    }

    public List<Appointment> getNextWeekAppointmentsSorted() {
        LocalDate today = LocalDate.now();
        LocalDate startOfNextWeek = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY)); // Start of next week (Monday)
        LocalDate endOfNextWeek = startOfNextWeek.plusDays(6); // End of next week (Sunday)

        return beautySalon.getAppointments().stream()
                .filter(appointment -> {
                    LocalDate appointmentDate = appointment.getAppointmentDate().toLocalDate();
                    return !appointmentDate.isBefore(startOfNextWeek) && !appointmentDate.isAfter(endOfNextWeek);
                })
                .sorted(Comparator.comparing(Appointment::getAppointmentDate))
                .collect(Collectors.toList());
    }

}
