package net.etfbl.mdp.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.etfbl.mdp.model.Supplier;

public class SupplierMain extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private CardLayout cardLayout;
    private JPanel cardPanel;
   
    private static final String COUNTER_FILE = "supplier_counter.txt";
    private Supplier supplier;
    
    
    public SupplierMain() {    	
    	
    	 int instanceNumber = loadAndIncrementCounter();

         String name = "Supplier" + instanceNumber;
         String port = String.valueOf(5000 + instanceNumber);
         supplier = new Supplier(name, port, instanceNumber);
    	
        setTitle("Supplier: " + name + " - Control Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel navPanel = new JPanel();
        JButton btnParts = new JButton("Manage Parts");
        JButton btnOrders = new JButton("Order Queue");
        navPanel.add(btnParts);
        navPanel.add(btnOrders);
        add(navPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new PartsPanel(supplier), "parts");
        cardPanel.add(new OrderQueuePanel(supplier.getName()), "orders");

        add(cardPanel, BorderLayout.CENTER);

        btnParts.addActionListener(e -> cardLayout.show(cardPanel, "parts"));
        btnOrders.addActionListener(e -> cardLayout.show(cardPanel, "orders"));
        
       
    }
      
    private int loadAndIncrementCounter() {
        int counter = 0;
        File file = new File(COUNTER_FILE);
        try {
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    if (line != null) {
                        counter = Integer.parseInt(line.trim());
                    }
                }
            }
            if(counter == 4) counter = 0;
            counter++;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(String.valueOf(counter));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            counter = 1;
        }
        return counter;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupplierMain().setVisible(true));
        
    }
	
}
