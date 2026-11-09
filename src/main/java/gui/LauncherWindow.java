package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import entities.Session;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import service.UserService;

public class LauncherWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	//GUI'S
	private RegisterWindow register;
	//SERVIÇOS
	private UserService network;
	//COMPONENTES
	private JPanel contentPane;
	private JTextField tf_ip;
	private JTextField tf_port;
	private JButton btn_exit;
	private JButton btn_connect;

	private String testando = "não";
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LauncherWindow frame = new LauncherWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LauncherWindow() {
		initComponents();
	}
	
	public void abrirRegister() {
		
		this.register = new RegisterWindow(this);
		this.register.setVisible(true);
		this.setVisible(false);
		
	}
	
	public boolean conectar(String ip, int port) {
		
		Session.setIp(ip);
		Session.setPort(port);
		
		this.network = new UserService();
		
		return true;
	}
	
	public void erroServidor() {
		
		this.tf_ip.setText("");
		this.tf_port.setText("");
		
		JOptionPane.showMessageDialog(this, "Não foi possível encontrar servidor!", "404", JOptionPane.WARNING_MESSAGE);
	}
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UiStyle.configureFrame(this, "Início", 540, 470);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader("Garagem do Tião", "Conecte-se ao servidor para começar."),
				java.awt.BorderLayout.NORTH);

		JPanel card = UiStyle.createCard();
		tf_ip = new JTextField();
		tf_ip.setColumns(10);
		card.add(UiStyle.createField("Endereço IP", tf_ip));

		tf_port = new JTextField();
		tf_port.setColumns(10);
		card.add(javax.swing.Box.createVerticalStrut(16));
		card.add(UiStyle.createField("Porta do servidor", tf_port));
		card.add(javax.swing.Box.createVerticalStrut(22));

		btn_connect = UiStyle.primaryButton("Conectar");
		btn_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(testando.equals("sim")) {
					abrirRegister();
				}else {
					String ip = tf_ip.getText().trim();
					String portText = tf_port.getText().trim();
					if (ip.isEmpty() || portText.isEmpty()) {
						JOptionPane.showMessageDialog(LauncherWindow.this,
								"Informe o endereço IP e a porta do servidor.",
								"Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
						return;
					}
					try {
						int port = Integer.parseInt(portText);
						if (port <= 0 || port > 65535) {
							throw new NumberFormatException();
						}
						if(conectar(ip, port)) {
							abrirRegister();
						}else {
							erroServidor();											
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(LauncherWindow.this,
								"Informe uma porta numérica válida (1 a 65535).",
								"Porta inválida", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		btn_exit = UiStyle.secondaryButton("Sair");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
		card.add(UiStyle.createActions(btn_connect, btn_exit));
		contentPane.add(card, java.awt.BorderLayout.CENTER);
		getRootPane().setDefaultButton(btn_connect);
	}
}
