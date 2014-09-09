package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public LoginWindow() {
		setTitle("Gestionnaire : Ouverture de session");
		setSize(400, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		initComponent();
	}
	

	private void initComponent() {
		Font fontTitleLogin = new Font("Verdana", Font.ITALIC | Font.BOLD, 20);
		Font fontStatusMessage = new Font("Verdana", Font.BOLD, 12);
		lblTitleLogin.setFont(fontTitleLogin);
		lblStatusMessage.setForeground(Color.RED);
		lblStatusMessage.setFont(fontStatusMessage);
		
		pnlLoginInfo.setOpaque(false);
		pnlLoginInfo.setLayout(new MigLayout());

		pnlLoginInfo.add(lblUsername);
		pnlLoginInfo.add(jtfUsername, "wrap");
		pnlLoginInfo.add(lblPassword);
		pnlLoginInfo.add(jpfPassword, "wrap");
		pnlLoginInfo.add(btnConnect);
		
		pnlContainer.setLayout(new BorderLayout());
		pnlContainer.add(lblTitleLogin, BorderLayout.NORTH);
		pnlContainer.add(pnlLoginInfo, BorderLayout.WEST);
		pnlContainer.add(lblStatusMessage, BorderLayout.SOUTH);
		
		this.add(pnlContainer);
	}
	
	private void verifyIdentity() {
	}

	AbstractAction connectAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
        	verifyIdentity();
        }
    };

	private LoginPanel pnlContainer = new LoginPanel();
	private JPanel pnlLoginInfo = new JPanel();

	private JLabel lblTitleLogin = new JLabel("Connexion");
	private JLabel lblUsername = new JLabel("Username : ");
	private JLabel lblPassword = new JLabel("Password : ");
	private JLabel lblStatusMessage = new JLabel();
	
	private JTextField jtfUsername = new JTextField(10);
	private JPasswordField jpfPassword = new JPasswordField(10);
	private JButton btnConnect = new JButton("Connexion");
}
