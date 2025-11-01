package net.etfbl.mdp.model;

import net.etfbl.mdp.messaging.OrderPublisher;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Order order = new Order("O-001", "Brake Pads", 2, 150.0, "boris");
		OrderPublisher.sendOrder(order);
		Order order2 = new Order("O-002", "Brake Pads", 2, 150.0, "boris");
		OrderPublisher.sendOrder(order2);
		Order order3 = new Order("O-003", "Brake Pads", 2, 150.0, "boris");
		OrderPublisher.sendOrder(order3);
		Order order4 = new Order("O-004", "Brake Pads", 2, 150.0, "boris");
		OrderPublisher.sendOrder(order4);
	}

}
