package net.etfbl.mdp.messaging;

import com.rabbitmq.client.*;
import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.service.OrderQueueService;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class OrderConsumer implements Runnable {

	private String QUEUE_NAME = "orders_queue";

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
			// TODO:loger

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				byte[] data = delivery.getBody();
				try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
					Order order = (Order) ois.readObject();
					// TODO:loger
					OrderQueueService.addOrder(order);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

			CancelCallback cancelCallback = consumerTag -> System.out
					.println("[SupplierApp] Cancelled: " + consumerTag); // TODO:loger

			channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
