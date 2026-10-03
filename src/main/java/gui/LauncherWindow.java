package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LauncherWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	//GUI'S
	private RegisterWindow register;
	//COMPONENTES
	private JPanel contentPane;
	private JTextField tf_ip;
	private JTextField tf_port;
	

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
		setTitle("Inicio");
		
		setLocationRelativeTo(null);
		
		initComponents();
		
	}
	
	public void abrirRegister() {
		
	}
	
	public boolean conectar() {
		return true;
	}
	
	public void erroServidor() {
		
		this.tf_ip.setText("");
		this.tf_port.setText("");
		
		JOptionPane.showMessageDialog(this, "Não foi possível encontrar servidor!", "404", JOptionPane.WARNING_MESSAGE);
	}
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 259, 224);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_ip = new JLabel("Insira o IP para conexão: ");
		lbl_ip.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lbl_ip.setBounds(10, 24, 164, 25);
		contentPane.add(lbl_ip);
		
		tf_ip = new JTextField();
		tf_ip.setBounds(10, 50, 143, 20);
		contentPane.add(tf_ip);
		tf_ip.setColumns(10);
		
		JLabel lbl_port = new JLabel("Insira a porta para conexão:");
		lbl_port.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lbl_port.setBounds(10, 70, 213, 25);
		contentPane.add(lbl_port);
		
		tf_port = new JTextField();
		tf_port.setBounds(10, 95, 143, 20);
		contentPane.add(tf_port);
		tf_port.setColumns(10);
		
		JButton btn_connect = new JButton("Conectar");
		btn_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(conectar()) {
					abrirRegister();
				}
				erroServidor();
			}
		});
		btn_connect.setBounds(117, 122, 97, 32);
		contentPane.add(btn_connect);
		
		JButton btn_exit = new JButton("Sair");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO fechar o programa
			}
		});
		btn_exit.setBounds(10, 122, 97, 32);
		contentPane.add(btn_exit);
		
	}
}
