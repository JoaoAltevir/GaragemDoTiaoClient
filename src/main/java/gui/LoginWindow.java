package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.gson.JsonObject;

import service.UserService;

import javax.swing.JOptionPane;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	//GUI'S
	private RegisterWindow register;
	private MenuWindow menu;
	//SERVIÇOS
	private UserService userService;
	//COMPONENTS
	private JsonObject res;
	private JTextField tf_username;
	private JPasswordField tf_password;


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


	public LoginWindow(RegisterWindow register, String username, UserService service) {
		
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

	
	public void fechar() {
		
		this.dispose();
		register.setVisible(true);
	}
	
	public int login() {
		
		res = this.userService.login(tf_username.getText(), new String(tf_password.getPassword()));

		int status = res.get("statusCode").getAsInt();
		
		if(status == 200) {
			return status;
		}

		return status;
		
	}
	
	public void abrirMenu(){
		
		this.menu = new MenuWindow(this);
		
		menu.setVisible(true);
		this.setVisible(false);
	}
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UiStyle.configureFrame(this, "Entrar", 520, 540);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader("Que bom ter você de volta", "Entre com seus dados para acessar sua conta."),
				java.awt.BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();

		tf_username = new JTextField();
		tf_username.setColumns(10);
		card.add(UiStyle.createField("Nome de usuário", tf_username));
		card.add(javax.swing.Box.createVerticalStrut(16));

		tf_password = new JPasswordField();
		tf_password.setColumns(10);
		card.add(UiStyle.createField("Senha", tf_password));
		char passwordEchoChar = tf_password.getEchoChar();
		javax.swing.JCheckBox cb_showPassword = UiStyle.createCheckBox("Mostrar senha");
		cb_showPassword.addActionListener(e -> tf_password.setEchoChar(
				cb_showPassword.isSelected() ? (char) 0 : passwordEchoChar));
		cb_showPassword.setAlignmentX(javax.swing.JComponent.LEFT_ALIGNMENT);
		card.add(javax.swing.Box.createVerticalStrut(6));
		card.add(cb_showPassword);
		card.add(javax.swing.Box.createVerticalStrut(20));

		JButton btn_login = UiStyle.primaryButton("Entrar");
		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int status = login();
				if(status == 200) {
					abrirMenu();
				}else {
					JOptionPane.showMessageDialog(LoginWindow.this, "Credenciais inválidas!");
				}
			}
		});
		JButton btn_exit = UiStyle.secondaryButton("Sair");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fechar();
			}
		});
		card.add(UiStyle.createActions(btn_login, btn_exit));
		contentPane.add(card, java.awt.BorderLayout.CENTER);
		getRootPane().setDefaultButton(btn_login);
	}
}
