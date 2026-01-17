package net.etfbl.mdp.gui;

import net.etfbl.mdp.messaging.OrderConsumer;
import net.etfbl.mdp.model.Invoice;
import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.service.OrderQueueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class OrderQueuePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable orderTable;
	    private DefaultTableModel tableModel;
	    private String queueName;
	    private static HashMap<String, String> ordersMap = new HashMap<String, String>();

	    public OrderQueuePanel(String supplier) {
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
	        orderTable = new JTable(tableModel){
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getToolTipText(MouseEvent e) {
					Point p =e.getPoint();
					int row = rowAtPoint(p);
					int col = columnAtPoint(p);
					
					if(row >=0 && col >=0) {
						Object value = getValueAt(row, col);
						if(value != null) {
							return value.toString();
						}
					}
					return null;
				}
			};

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
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	        for (Order o : orders) {
	            tableModel.addRow(new Object[]{
	                    o.getOrderId(),
	                    o.getClientUsername(),
	                    o.getPartName(),
	                    o.getQuantity(),
	                    o.getPrice(),
	                    o.getTime().format(formatter)
	            });
	            ordersMap.put(o.getOrderId(), "PENDING");
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
	        String createdAt = tableModel.getValueAt(row, 5).toString();
	        DateTimeFormatter formatter =
	                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	        LocalDateTime time = LocalDateTime.parse(createdAt, formatter);
	        Invoice invoice = new Invoice(supplierName, orderId, partTitle, price*quant, time);
	        OrderQueueService.processOrder(invoice,approved);
	        if(true == approved) {
	        	ordersMap.put(orderId, "APPROVED");
	        }else {
	        	ordersMap.put(orderId,"REJECTED");
	        }
	        refreshOrders();

	        JOptionPane.showMessageDialog(this,
	                approved ? "Order approved and invoice sent." : "Order rejected.");
	    }
	    
	    public static HashMap<String,String> getOrderMap(){
	    	return ordersMap;
	    }
}
