package net.etfbl.mdp.service;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.service.repository.AppointmentRepository;

import java.util.List;

public class AppointmentService {

	private AppointmentRepository repository = new AppointmentRepository();

	public List<Appointment> getAll() {
		return repository.getAllAppointments();
	}

	public List<Appointment> getByUser(String username) {
		return repository.findByUsername(username);
	}

	public Appointment create(Appointment a) {
		return repository.addAppointment(a);
	}

	public boolean updateAppointment(Appointment a) {
		return repository.updateAppointment(a);
	}

	public boolean delete(String id) {
		return repository.deleteAppointment(id);
	}

	public Appointment findById(String id) {
		return repository.findById(id);
	}
}
