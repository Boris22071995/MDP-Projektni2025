package net.etfbl.mdp.service;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.service.repository.AppointmentRepository;
import net.etfbl.mdp.util.AppLogger;

import java.util.List;
import java.util.logging.Logger;

public class AppointmentService {

	private static final Logger log = AppLogger.getLogger();
	private AppointmentRepository repository = new AppointmentRepository();

	public List<Appointment> getAll() {
		log.info("Appointments are retrived");
		return repository.getAllAppointments();
	}

	public List<Appointment> getByUser(String username) {
		log.info("Appointment for user is retrievd");
		return repository.findByUsername(username);
	}

	public Appointment create(Appointment a) {
		log.info("Appointment is created");
		return repository.addAppointment(a);
	}

	public boolean updateAppointment(Appointment a) {
		log.info("Appointment is updated");
		return repository.updateAppointment(a);
	}

	public boolean delete(String id) {
		log.info("Appointment is deleted");
		return repository.deleteAppointment(id);
	}

	public Appointment findById(String id) {
		return repository.findById(id);
	}
}
