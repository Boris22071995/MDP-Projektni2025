package net.etfbl.mdp.service.gui;

import javax.swing.*;
import java.awt.*;

public class InvoicePanel extends JPanel {

	public InvoicePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Invoicing"));

        JTextArea invoicePreview = new JTextArea();
        invoicePreview.setEditable(false);
        invoicePreview.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JPanel controls = new JPanel(new FlowLayout());
        JButton btnGenerate = new JButton("Create Invoice");
        JButton btnSend = new JButton("Sent to Email");
        
        for (JButton b : new JButton[]{btnGenerate, btnSend}) {
            b.setFocusPainted(false);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setBackground(new Color(52, 73, 94));
            b.setForeground(Color.BLACK);
        }
        

        controls.add(btnGenerate);
        controls.add(btnSend);

        add(new JScrollPane(invoicePreview), BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        // TODO: RMI logika + ZIP + email slanje
    }
	
}
