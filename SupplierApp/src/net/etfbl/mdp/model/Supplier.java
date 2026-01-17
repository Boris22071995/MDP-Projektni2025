package net.etfbl.mdp.model;

public class Supplier {

	private String name;
	private String port;
	private int instaceNumber;

	public Supplier() {
	}

	public Supplier(String name, String port, int instanceNumber) {
		this.name = name;
		this.port = port;
		this.instaceNumber = instanceNumber;
	}

	public int getInstanceNumber() {
		return instaceNumber;
	}

	public void setInstanceNumber(int number) {
		this.instaceNumber = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Supplier [name=" + name + ", port=" + port + "]";
	}

}
