package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entities.Session;
import service.UserService;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	//GUI'S
	private RegisterWindow register;
	//SERVIÇOS
	private UserService userService;
	//COMPONENTS
	private JTextField tf_username;
	private JTextField tf_password;


	/**
	 * Create the frame.
	 * @wbp.parser.constructor
	 */
	public LoginWindow(RegisterWindow register, UserService service) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				fechar();
			}
		});
		setTitle("Login");
		
		this.register = register;
		this.userService = service;
		
		setLocationRelativeTo(null);
		
		initComponents();
		
	}

	public LoginWindow(RegisterWindow register, String username,UserService service) {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				fechar();
			}
		});
		
		setTitle("Login");
		
		this.register = register;
		this.userService = service;
		
		
		initComponents();

		tf_username.setText(username);
		
	}
	
	public void fechar() {
		
		this.dispose();
		register.setVisible(true);
	}
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 195, 307);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_title = new JLabel("Área de Login");
		lbl_title.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lbl_title.setBounds(10, 51, 126, 44);
		contentPane.add(lbl_title);
		
		JLabel lbl_username = new JLabel("Nome de usuário: ");
		lbl_username.setBounds(10, 92, 113, 14);
		contentPane.add(lbl_username);
		
		tf_username = new JTextField();
		tf_username.setBounds(10, 117, 125, 20);
		contentPane.add(tf_username);
		tf_username.setColumns(10);
		
		JLabel lbl_password = new JLabel("Insira sua senha:");
		lbl_password.setBounds(10, 148, 126, 14);
		contentPane.add(lbl_password);
		
		tf_password = new JTextField();
		tf_password.setColumns(10);
		tf_password.setBounds(11, 173, 125, 20);
		contentPane.add(tf_password);
		
		JRadioButton rdbtn_eyePass = new JRadioButton("");
		rdbtn_eyePass.setBounds(142, 173, 21, 23);
		contentPane.add(rdbtn_eyePass);
		
		JButton btn_register = new JButton("Login");
		btn_register.setBounds(10, 204, 126, 23);
		contentPane.add(btn_register);
		
		JButton btn_exit = new JButton("Sair");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fechar();
			}
		});
		btn_exit.setBounds(10, 239, 89, 23);
		contentPane.add(btn_exit);

		setLocationRelativeTo(null);
		
	}
}
