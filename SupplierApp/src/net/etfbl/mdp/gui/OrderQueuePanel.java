package net.etfbl.mdp.gui;

import net.etfbl.mdp.messaging.OrderConsumer;
import net.etfbl.mdp.model.Invoice;
import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.server.SupplierServer;
import net.etfbl.mdp.service.OrderQueueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class OrderQueuePanel extends JPanel {
	 private JTable orderTable;
	    private DefaultTableModel tableModel;
	    private String supplier;
	    private String queueName;

	    public OrderQueuePanel(String supplier) {
	    	this.supplier = supplier;
	    	 if("Supplier1".equals(supplier)) {
		        	queueName = "orders_queue1";
		        }else if ("Supplier2".equals(supplier)) {
		        	queueName = "orders_queue2";
		        }else if("Supplier3".equals(supplier)) {
		        	queueName = "orders_queue3";
		        }else {
		        	queueName = "orders_queue4";
		        }
	        setLayout(new BorderLayout());

	        tableModel = new DefaultTableModel(new String[]{"Order ID", "Client", "Part", "Qty", "Price", "Time"}, 0);
	        orderTable = new JTable(tableModel);

	        refreshOrders();

	        JButton approveBtn = new JButton("Approve");
	        JButton rejectBtn = new JButton("Reject");
	        JButton refreshBtn = new JButton("Refresh");

	        approveBtn.addActionListener(e -> handleDecision(true));
	        rejectBtn.addActionListener(e -> handleDecision(false));
	        refreshBtn.addActionListener(e ->refreshOrders());

	        JPanel buttonPanel = new JPanel();
	        buttonPanel.add(approveBtn);
	        buttonPanel.add(rejectBtn);
	        buttonPanel.add(refreshBtn);

	        add(new JScrollPane(orderTable), BorderLayout.CENTER);
	        add(buttonPanel, BorderLayout.SOUTH);
	    }

	    private void refreshOrders() {
	    	new Thread(new OrderConsumer(queueName)).start();
	    	
	        tableModel.setRowCount(0);
	        List<Order> orders = OrderQueueService.getPendingOrders();
	        for (Order o : orders) {
	            tableModel.addRow(new Object[]{
	                    o.getOrderId(),
	                    o.getClientUsername(),
	                    o.getPartName(),
	                    o.getQuantity(),
	                    o.getPrice(),
	                    o.getCreatedAt()
	            });
	        }
	    }

	    private void handleDecision(boolean approved) {
	        int row = orderTable.getSelectedRow();
	        if (row == -1) {
	            JOptionPane.showMessageDialog(this, "Select an order first.");
	            return;
	        }
	        
	        String orderId = tableModel.getValueAt(row, 0).toString();
	        String supplierName = tableModel.getValueAt(row,1).toString();
	        String partTitle = tableModel.getValueAt(row, 2).toString();
	        int quant = (Integer)tableModel.getValueAt(row, 3);
	        double price = (Double)tableModel.getValueAt(row, 4);
	        Invoice invoice = new Invoice(orderId, supplierName, orderId, partTitle, price*quant);
	        OrderQueueService.processOrder(invoice,approved);
	        refreshOrders();

	        JOptionPane.showMessageDialog(this,
	                approved ? "Order approved and invoice sent." : "Order rejected.");
	    }
}
