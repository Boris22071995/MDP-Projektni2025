package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.service.OrderQueueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class OrderQueuePanel extends JPanel {
	 private JTable orderTable;
	    private DefaultTableModel tableModel;

	    public OrderQueuePanel() {
	        setLayout(new BorderLayout());

	        tableModel = new DefaultTableModel(new String[]{"Order ID", "Client", "Part", "Qty", "Price", "Time"}, 0);
	        orderTable = new JTable(tableModel);

	        refreshOrders();

	        JButton approveBtn = new JButton("Approve");
	        JButton rejectBtn = new JButton("Reject");

	        approveBtn.addActionListener(e -> handleDecision(true));
	        rejectBtn.addActionListener(e -> handleDecision(false));

	        JPanel buttonPanel = new JPanel();
	        buttonPanel.add(approveBtn);
	        buttonPanel.add(rejectBtn);

	        add(new JScrollPane(orderTable), BorderLayout.CENTER);
	        add(buttonPanel, BorderLayout.SOUTH);
	    }

	    private void refreshOrders() {
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
	        OrderQueueService.processOrder(orderId, approved);
	        refreshOrders();

	        JOptionPane.showMessageDialog(this,
	                approved ? "Order approved and invoice sent." : "Order rejected.");
	    }
}
