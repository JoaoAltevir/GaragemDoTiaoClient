package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.gson.JsonObject;

import service.UserService;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
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
	private JPasswordField tf_password;
	private JTextField tf_name;
	private JButton btn_register;
	private JButton btn_hasAccount;
	private JButton btn_exit;
	private JCheckBox rdbtn_eyePass;


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
	
	public boolean enviarRegistro(String name, String username, String password) {
		
		
		JsonObject valid = userService.register(name, username , password);

		if(valid != null){
			return true;
		}

		return false;
	}
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UiStyle.configureFrame(this, "Criar conta", 560, 640);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader("Crie sua conta", "Preencha seus dados para continuar."),
				java.awt.BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();

		tf_name = new JTextField();
		tf_name.setColumns(10);
		card.add(UiStyle.createField("Nome completo", tf_name));
		card.add(javax.swing.Box.createVerticalStrut(14));

		tf_username = new JTextField();
		tf_username.setColumns(10);
		card.add(UiStyle.createField("Nome de usuário", tf_username));
		card.add(javax.swing.Box.createVerticalStrut(14));

		tf_password = new JPasswordField();
		tf_password.setColumns(10);
		card.add(UiStyle.createField("Senha", tf_password));
		char passwordEchoChar = tf_password.getEchoChar();
		rdbtn_eyePass = UiStyle.createCheckBox("Mostrar senha");
		rdbtn_eyePass.addActionListener(e -> tf_password.setEchoChar(
				rdbtn_eyePass.isSelected() ? (char) 0 : passwordEchoChar));
		rdbtn_eyePass.setAlignmentX(javax.swing.JComponent.LEFT_ALIGNMENT);
		card.add(javax.swing.Box.createVerticalStrut(5));
		card.add(rdbtn_eyePass);
		card.add(javax.swing.Box.createVerticalStrut(18));

		btn_register = UiStyle.primaryButton("Finalizar cadastro");
		btn_register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(enviarRegistro(tf_name.getText(), tf_username.getText(), new String(tf_password.getPassword()))) {
					abrirLogin();
				}
			}
		});
		btn_hasAccount = UiStyle.secondaryButton("Já tenho conta");
		btn_hasAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirLogin();
			}
		});
		btn_exit = UiStyle.secondaryButton("Sair");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fechar();
			}
		});
		card.add(UiStyle.createActions(btn_register, btn_hasAccount));
		card.add(javax.swing.Box.createVerticalStrut(10));
		card.add(UiStyle.createActions(btn_exit));
		contentPane.add(card, java.awt.BorderLayout.CENTER);
		getRootPane().setDefaultButton(btn_register);
	}
}
