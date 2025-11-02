package net.etfbl.mdp.messaging;

import com.rabbitmq.client.*;
import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.service.OrderQueueService;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class OrderConsumer implements Runnable{

    private static final String QUEUE_NAME = "orders_queue";

    @Override
    public void run() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("[SupplierApp] Waiting for orders...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                byte[] data = delivery.getBody();
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                    Order order = (Order) ois.readObject();
                    System.out.println("[SupplierApp] Received order: " + order);
                    OrderQueueService.addOrder(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            CancelCallback cancelCallback = consumerTag ->
                    System.out.println("[SupplierApp] Cancelled: " + consumerTag);

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
