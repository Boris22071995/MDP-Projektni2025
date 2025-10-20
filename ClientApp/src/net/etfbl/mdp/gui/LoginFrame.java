package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private AuthService authService = new AuthService();
	
	public LoginFrame() {
		setTitle("Client application - Login");
		setSize(400,250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		panel.add(new JLabel("Username:"));
		txtUsername = new JTextField();
		panel.add(txtUsername);
		
		panel.add(new JLabel("Password:"));
		txtPassword = new JPasswordField();
		panel.add(txtPassword);
		
		JButton btnLogin = new JButton("Login");
		JButton btnRegister = new JButton("Sign up");
		panel.add(btnLogin);
		panel.add(btnRegister);
		
		add(panel, BorderLayout.CENTER);
		
		btnLogin.addActionListener(e -> login());
		btnRegister.addActionListener(e -> register());
	}
	
	private void login() {
		String username = txtUsername.getText();
		String password = new String(txtPassword.getPassword());
		Client client = authService.login(username, password);
		
		if(client == null) {
			JOptionPane.showMessageDialog(this, "Incorrect data!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(!client.isApproved()) {
			 JOptionPane.showMessageDialog(this, "Vaš nalog još nije odobren od strane servisera.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
	            return;
		}
		if(client.isBlocked()) {
			JOptionPane.showMessageDialog(this, "Vaš nalog je blokiran!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
		}
		
		new MainFrame(client).setVisible(true);;
		this.dispose();
	}
	
	private void register() {
		 JTextField f1 = new JTextField();
	        JTextField f2 = new JTextField();
	        JTextField f3 = new JTextField();
	        JTextField f4 = new JTextField();
	        JTextField f5 = new JTextField();
	        JTextField f6 = new JTextField();
	        JTextField f7 = new JTextField();
	        JPasswordField f8 = new JPasswordField();
	        JPasswordField f9 = new JPasswordField();

	        Object[] fields = {
	                "Name:", f1,
	                "Surname:", f2,
	                "Vehicle data:", f3,
	                "Address:", f4,
	                "Email:", f5,
	                "Phone:", f6,
	                "Username:", f7,
	                "Password", f8,
	                "Password", f9
	        };
	        int result = JOptionPane.showConfirmDialog(this, fields, "Registracija", JOptionPane.OK_CANCEL_OPTION);
	        if (result == JOptionPane.OK_OPTION) {
	            Client c = new Client(f1.getText(), f2.getText(), f7.getText(), new String(f8.getPassword()),f4.getText(),
	            		f5.getText(),f6.getText(), f3.getText(),false,false);
	            if (authService.register(c)) {
	                JOptionPane.showMessageDialog(this, "Registracija uspješna! Sačekajte odobrenje servisera.");
	            } else {
	                JOptionPane.showMessageDialog(this, "Greška prilikom registracije.", "Greška", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}
	
}
