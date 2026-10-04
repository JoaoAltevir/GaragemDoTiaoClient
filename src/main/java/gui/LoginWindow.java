package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	//GUI'S
	RegisterWindow register;
	//COMPONENTS



	/**
	 * Create the frame.
	 */
	public LoginWindow(RegisterWindow register, String username) {
		
		this.register = register;
		//this.tf_username.setText(username);
		setLocationRelativeTo(null);
		
		initComponents();
		
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public LoginWindow(RegisterWindow register) {
		this.register = register;
		setLocationRelativeTo(null);
		initComponents();
	}
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
	}

}
