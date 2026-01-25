package net.etfbl.mdp.messaging;

import com.rabbitmq.client.*;
import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.service.OrderQueueService;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.logging.Logger;

public class OrderConsumer implements Runnable {

	private String QUEUE_NAME = ConfigurationLoader.getString("queue.name");
	private static final Logger log = AppLogger.getLogger();

	public OrderConsumer(String queueName) {
		this.QUEUE_NAME = queueName;
	}

	@Override
	public void run() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				byte[] data = delivery.getBody();
				try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
					Order order = (Order) ois.readObject();
					OrderQueueService.addOrder(order);
				} catch (Exception e) {
					log.severe("Error while retriving orders");
					e.printStackTrace();
				}
			};

			CancelCallback cancelCallback = consumerTag -> System.out
					.println("[SupplierApp] Cancelled: " + consumerTag);

			channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
