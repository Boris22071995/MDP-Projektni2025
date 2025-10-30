package net.etfbl.mdp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Appointment implements Serializable {
	private String id;
	private String ownerUsername;
	private String date;
	private String time;
	private String type;
	private String description;
	private String status;
	
	public Appointment() {
		this.id = UUID.randomUUID().toString();
		this.status = "RESERVED";
	}

	public Appointment(String id, String ownerUsername, String date, String time, String type, String description,
			String status) {
		this.id = UUID.randomUUID().toString();
		this.ownerUsername = ownerUsername;
		this.date = date;
		this.time = time;
		this.type = type;
		this.description = description;
		this.status = "RESERVED";
	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", ownerUsername=" + ownerUsername + ", date=" + date + ", time=" + time
				+ ", type=" + type + ", description=" + description + ", status=" + status + "]";
	}

	
	
}
