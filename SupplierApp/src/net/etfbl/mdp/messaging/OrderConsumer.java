package net.etfbl.mdp.messaging;

import com.rabbitmq.client.*;
import net.etfbl.mdp.model.Order;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class OrderConsumer {

    private static final String QUEUE_NAME = "orders_queue";

    public static void startListening() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("[SupplierApp] Waiting for orders...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                byte[] data = delivery.getBody();
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                    Order order = (Order) ois.readObject();
                    System.out.println("[SupplierApp] Received order: " + order);
                } catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
