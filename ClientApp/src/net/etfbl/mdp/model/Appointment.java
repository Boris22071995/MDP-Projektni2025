package net.etfbl.mdp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public enum Type {SERVICE, REPARATION}
	public enum Status {RESERVED, FINISHED, REJECTED}
	
	private LocalDate date;
	private LocalTime time;
	private Type type;
	private String description;
	private Status status;
	
	public Appointment(LocalDate date, LocalTime time, Type type, String description, Status status) {
		super();
		this.date = date;
		this.time = time;
		this.type = type;
		this.description = description;
		this.status = status;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Appointment [date=" + date + ", time=" + time + ", type=" + type + ", description=" + description
				+ ", status=" + status + "]";
	}	

}
