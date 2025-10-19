package net.etfbl.mdp.model;

import java.io.Serializable;

public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String lastName;
	private String username;
	private String password;
	private String address;
	private String email;
	private String phone;
	private String vehicleData;
	private boolean approved;
	private boolean blocked;
	
	public Client() {
	
	}

	public Client(String name, String lastName, String username, String password, String address, String email,
			String phone, String vehicleData, boolean approved, boolean blocked) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.vehicleData = vehicleData;
		this.approved = approved;
		this.blocked = blocked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVehicleData() {
		return vehicleData;
	}

	public void setVehicleData(String vehicleData) {
		this.vehicleData = vehicleData;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	

	@Override
	public String toString() {
		return "Client [name=" + name + ", lastName=" + lastName + ", username=" + username + ", address=" + address
				+ ", email=" + email + ", phone=" + phone + ", vehicleData=" + vehicleData + ", approved=" + approved
				+ ", blocked=" + blocked + "]";
	}

}
