package net.etfbl.mdp.service.repository;

import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.beans.XMLDecoder;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

public class AppointmentRepository {

	private static final String FILE_PATH = "C:\\Users\\Boris\\OneDrive\\Desktop\\MDP-Projektni2025\\ServiceApp\\webapp\\WEB-INF\\appointments.xml"; //ConfigurationLoader.getString("appointments.filepath");
	private static final Logger log = AppLogger.getLogger();
	@SuppressWarnings("unchecked")
	public ArrayList<Appointment> getAllAppointments() {
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			log.severe("Error while retriving appointments.");
			return new ArrayList<>();
		}

		try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)))) {
			List<Appointment> clientList = (List<Appointment>) decoder.readObject();
			return new ArrayList<>(clientList);
		} catch (Exception e) {
			log.severe("Error while decoding appointments.");
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void saveAllAppointments(List<Appointment> appointments) {
		try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_PATH)))) {
			encoder.writeObject(appointments);
		} catch (Exception e) {
			log.severe("Error while saving appointments.");
			e.printStackTrace();
		}
	}

	public ArrayList<Appointment> findByUsername(String username) {
		ArrayList<Appointment> allAppointments = getAllAppointments();
		ArrayList<Appointment> allAppointmentsUser = new ArrayList<>();
		for (Appointment app : allAppointments) {
			if (app.getOwnerUsername().equalsIgnoreCase(username))
				allAppointmentsUser.add(app);
		}
		if (allAppointmentsUser.size() > 0)
			return allAppointmentsUser;
		return new ArrayList<>();
	}

	public Appointment addAppointment(Appointment appointment) {
		List<Appointment> appointments = getAllAppointments();
		if (appointments.add(appointment)) {
			saveAllAppointments(appointments);
			return appointment;
		}
		return new Appointment();

	}

	public boolean updateAppointment(Appointment appointment) {
		List<Appointment> appointments = getAllAppointments();
		for (int i = 0; i < appointments.size(); i++) {
			if (appointments.get(i).getId().equalsIgnoreCase(appointment.getId())) {
				appointments.set(i, appointment);
				saveAllAppointments(appointments);
				return true;
			}
		}
		return false;

	}

	public boolean deleteAppointment(String id) {
		List<Appointment> appointments = getAllAppointments();
		if (appointments.removeIf(a -> a.getId().equalsIgnoreCase(id))) {
			saveAllAppointments(appointments);
			return true;
		}
		return false;

	}

	public Appointment findById(String id) {
		Appointment ap = new Appointment();
		ArrayList<Appointment> appointments = getAllAppointments();
		for (Appointment app : appointments) {
			if (app.getId().equalsIgnoreCase(id))
				ap = app;
		}
		return ap;
	}
}
