package net.etfbl.mdp.service.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ServiceMain extends JFrame {

	public ServiceMain() {
		setTitle("Service Administration Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setLocationRelativeTo(null);
		
		add(new ClientPanel(), BorderLayout.CENTER);
		JFrame serverFrame = new JFrame("Server Chat Kontrola");
		serverFrame.add(new ChatServerPanel());
		serverFrame.setSize(400, 400);
		serverFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ServiceMain().setVisible(true));
	}
	
}
