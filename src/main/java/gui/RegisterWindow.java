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


public class RegisterWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	//GUI'S
	private LauncherWindow launcher;
	private LoginWindow login;
	//SERVIÇOS
	private UserService userService;
	//COMPONENTS
	private JTextField tf_username;
	private JTextField tf_password;
	private JTextField tf_name;
	private JButton btn_register;
	private JButton btn_hasAccount;
	private JButton btn_exit;
	private JRadioButton rdbtn_eyePass;


	/**
	 * Create the frame.
	 */
	public RegisterWindow(LauncherWindow init) {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				fechar();
			}
		});
		setTitle("Registro");
		
		this.launcher = init;
		this.userService = new UserService();
		
		
		initComponents();
		
	}
	
	public void fechar() {
		
		this.dispose();
		launcher.setVisible(true);
	}
	
	public void abrirLogin() {
		
		this.login = new LoginWindow(this, userService);
		this.setVisible(false);
		login.setVisible(true);
		
		
	}
	
	public void abrirLogin(String username) {
		
		this.login = new LoginWindow(this, username, userService);
		this.setVisible(false);
		login.setVisible(true);
		
		
	}
	
	public boolean enviarRegistro() {
		
		return true;
	}
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 310, 364);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_title = new JLabel("Área de Registro");
		lbl_title.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lbl_title.setBounds(61, 42, 126, 44);
		contentPane.add(lbl_title);
		
		JLabel lbl_username = new JLabel("Nome de usuário: ");
		lbl_username.setBounds(145, 88, 87, 14);
		contentPane.add(lbl_username);
		
		tf_username = new JTextField();
		tf_username.setBounds(145, 115, 125, 20);
		contentPane.add(tf_username);
		tf_username.setColumns(10);
		
		JLabel lbl_name = new JLabel("Nome Completo");
		lbl_name.setBounds(10, 88, 97, 14);
		contentPane.add(lbl_name);
		
		tf_name = new JTextField();
		tf_name.setBounds(10, 115, 126, 20);
		contentPane.add(tf_name);
		tf_name.setColumns(10);
		
		JLabel lbl_password = new JLabel("Insira sua senha:");
		lbl_password.setBounds(10, 146, 87, 14);
		contentPane.add(lbl_password);
		
		tf_password = new JTextField();
		tf_password.setColumns(10);
		tf_password.setBounds(10, 171, 125, 20);
		contentPane.add(tf_password);
		

		JRadioButton rdbtn_eyePass = new JRadioButton("");
		rdbtn_eyePass.setBounds(145, 171, 21, 23);

		contentPane.add(rdbtn_eyePass);
		
		btn_register = new JButton("Finalizar registro");
		btn_register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(enviarRegistro()) {
					abrirLogin(tf_username.getText());
				}
			}
		});
		btn_register.setBounds(10, 202, 126, 23);
		contentPane.add(btn_register);
		
		btn_hasAccount = new JButton("Já tenho conta");
		btn_hasAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirLogin();
			}
		});
		btn_hasAccount.setBounds(10, 257, 132, 23);
		contentPane.add(btn_hasAccount);
		
		btn_exit = new JButton("Sair");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fechar();
			}
		});
		btn_exit.setBounds(10, 291, 89, 23);
		contentPane.add(btn_exit);
		
		setLocationRelativeTo(null);
	}
}
