package net.etfbl.mdp.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SupplierMain {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Supplier - panel");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(700, 500); 
			frame.add(new PartsPanel());
			frame.setVisible(true);
		});
	}
	
}
