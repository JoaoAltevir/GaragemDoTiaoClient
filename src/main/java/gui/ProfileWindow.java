package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import service.*;

public class ProfileWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	//GUIS
	private MenuWindow menu;
	private JButton btn_updatePassword;
	private JButton btn_updateNome;
	private JButton btnNewButton;
	private JButton btn_deleteUser;
	private JLabel lbl_name;
	private JLabel lbl_username;
	private String name;
	private String username;
	//SERVIÇOS
	private UserService servico;



	/**
	 * Create the frame.
	 */
	public ProfileWindow(String name, String username, MenuWindow menu) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				fecharJanela();
			}
		});
		
		setTitle("Perfil");
		this.servico = new UserService();
		
		initComponents();
		
		this.name = name;
		this.username = username;
		
	}
	
	public void fecharJanela() {
		
		this.dispose();
		this.menu.setVisible(true);
		
	}
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 368, 243);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btn_deleteUser = new JButton("DELETAR USUÁRIO");
		btn_deleteUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn_deleteUser.setBounds(10, 174, 329, 23);
		contentPane.add(btn_deleteUser);
		
		btn_updatePassword = new JButton("Alterar Senha");
		btn_updatePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn_updatePassword.setBounds(10, 142, 99, 23);
		contentPane.add(btn_updatePassword);
		
		btn_updateNome = new JButton("Alterar Nome");
		btn_updateNome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn_updateNome.setBounds(121, 142, 108, 23);
		contentPane.add(btn_updateNome);
		
		btnNewButton = new JButton("LOGOUT");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(250, 142, 89, 23);
		contentPane.add(btnNewButton);
		
		lbl_name = new JLabel("Nome: " + this.name);
		lbl_name.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_name.setBounds(58, 97, 197, 23);
		contentPane.add(lbl_name);
		
		lbl_username = new JLabel("Nome de usuário: " + this.username);
		lbl_username.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_username.setBounds(58, 72, 264, 14);
		contentPane.add(lbl_username);
		
		setLocationRelativeTo(null);
		
	}
}
