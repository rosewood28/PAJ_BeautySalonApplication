package utils;

import appointment.Appointment;

public interface AppointmentListener {
    void onAppointmentBooked(Appointment appointment);
}
