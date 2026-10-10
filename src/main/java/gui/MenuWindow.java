package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entities.Session;
import entities.User;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import service.UserService;

public class MenuWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	//GUI'S
	ProfileWindow profile;
	//SERVIÇOS
	UserService userService;

	/**
	 * Create the frame.
	 */
	public MenuWindow() {
		setTitle("Menu");
		
		this.userService = new UserService();
		initComponents();
		
		
	}
	
	public void abrirTelaProfile() {
		
		User user = userService.getUser(Session.getUsername());
		this.profile = new ProfileWindow(user.getName(), user.getUsername(), this);
		profile.setVisible(true);
		this.setVisible(false);
		
	}
	
	public void initComponents() {
		
		
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 893, 627);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(249, 11, 581, 467);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JButton btn_profile = new JButton("Perfil");
		btn_profile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirTelaProfile();
			}
		});
		btn_profile.setBounds(51, 28, 135, 42);
		contentPane.add(btn_profile);
		
		JButton btn_config = new JButton("Alterar servidor");
		btn_config.setBounds(51, 84, 135, 48);
		contentPane.add(btn_config);
		
		setLocationRelativeTo(null);
		
	}
}
