package net.etfbl.mdp.service;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Appointment.Status;
import net.etfbl.mdp.model.Appointment.Type;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

	private static List<Appointment> appointments = new ArrayList<>();
	
	static {
		appointments.add(new Appointment(LocalDate.now().minusDays(5), LocalTime.of(10, 0),
						Type.SERVICE, "Redovni servis - promjena ulja", Status.FINISHED));
		appointments.add(new Appointment(LocalDate.now().minusDays(2), LocalTime.of(13, 30),
                Type.REPARATION, "Zamjena kočnica", Status.REJECTED));
	}
	
	public static List<Appointment> getAll() {
		return appointments;
	}
	
	public static void add(Appointment appointment) {
		appointments.add(appointment);
	}
	
	public static void cancel(Appointment appointmetnt) {
		appointmetnt.setStatus(Status.REJECTED);
	}
	
	public static void finish(Appointment appointment) {
		appointment.setStatus(Status.FINISHED);
	}
	
}
