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
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}

		setTitle("Client Portal - Login");
		setSize(420, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(245, 247, 250));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel title = new JLabel("Login", SwingConstants.CENTER);
		title.setFont(new Font("Segoe UI", Font.BOLD, 20));
		title.setForeground(new Color(52, 73, 94));
		panel.add(title, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		form.setBackground(panel.getBackground());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel lblUser = new JLabel("Username:");
		lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtUsername = new JTextField(15);

		JLabel lblPass = new JLabel("Password:");
		lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtPassword = new JPasswordField(15);

		gbc.gridx = 0;
		gbc.gridy = 0;
		form.add(lblUser, gbc);
		gbc.gridx = 1;
		form.add(txtUsername, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		form.add(lblPass, gbc);
		gbc.gridx = 1;
		form.add(txtPassword, gbc);

		panel.add(form, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		JButton btnLogin = new JButton("Login");
		JButton btnRegister = new JButton("Sign up");
		btnLogin.setBackground(new Color(52, 73, 94));
		btnLogin.setForeground(Color.BLACK);
		btnLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnRegister.setBackground(new Color(52, 73, 94));
		btnRegister.setForeground(Color.BLACK);
		btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		buttons.add(btnLogin);
		buttons.add(btnRegister);
		panel.add(buttons, BorderLayout.SOUTH);

		add(panel);

		btnLogin.addActionListener(e -> login());
		btnRegister.addActionListener(e -> register());

	}

	private void login() {

		String username = txtUsername.getText().trim();
		String password = new String(txtPassword.getPassword());
		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Username and password are required.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Client client = authService.login(username, password);

		if (client == null) {
			JOptionPane.showMessageDialog(this, "Wrong data!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!client.isApproved()) {
			JOptionPane.showMessageDialog(this, "Your account is not approved yet.", "Notification",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (client.isBlocked()) {
			JOptionPane.showMessageDialog(this, "Your account is blocked.", "Notification",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		new MainFrame(client).setVisible(true);
		dispose();
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

		Object[] fields = { "Name:", f1, "Surname:", f2, "Vehicle data:", f3, "Address:", f4, "Email:", f5, "Phone:",
				f6, "Username:", f7, "Password:", f8 };

		int result = JOptionPane.showConfirmDialog(this, fields, "Registration", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			Client c = new Client(f1.getText(), f2.getText(), f7.getText(), new String(f8.getPassword()), f4.getText(),
					f5.getText(), f6.getText(), f3.getText(), false, false);

			if (authService.register(c)) {
				JOptionPane.showMessageDialog(this,
						"Registration completed successfully! Please wait for administrator approval");
			} else {
				JOptionPane.showMessageDialog(this, "Registration error.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}

}
