package net.etfbl.mdp.model;

import net.etfbl.mdp.messaging.OrderPublisher;

public class Test {

	  public static void main(String[] args) {
	        Order o1 = new Order("O1", "boris", "Ulje 5W40", 2, 50.0);
	        Order o2 = new Order("O2", "korisnik1", "Disk plocice", 1, 90.0);

	        //OrderPublisher.sendOrder(o1);
	        //OrderPublisher.sendOrder(o2);
	    }

}
