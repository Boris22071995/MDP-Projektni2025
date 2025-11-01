package net.etfbl.mdp.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.etfbl.mdp.model.Order;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class OrderPublisher {
	
	private static final String QUEUE_NAME = "orders_queue";
	
	 public static void sendOrder(Order order) {
	        try {
	            ConnectionFactory factory = new ConnectionFactory();
	            factory.setHost("localhost");
	            factory.setUsername("guest");
	            factory.setPassword("guest");

	            try (Connection connection = factory.newConnection();) {
	            	Channel channel = connection.createChannel();
	                channel.queueDeclare(QUEUE_NAME, false, false, false, null);

	                ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                ObjectOutputStream oos = new ObjectOutputStream(baos);
	                oos.writeObject(order);
	                oos.close();

	                byte[] data = baos.toByteArray();
	                channel.basicPublish("", QUEUE_NAME, null, data);
	                System.out.println("[ServiceApp] Sent order: " + order);
	                channel.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
