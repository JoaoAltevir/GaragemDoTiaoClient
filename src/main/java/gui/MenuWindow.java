package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
	LoginWindow login;
	//SERVIÇOS
	UserService userService;

	/**
	 * Create the frame.
	 */
	public MenuWindow(LoginWindow login) {
		setTitle("Menu");
		
		this.login = login;
		this.userService = new UserService();
		initComponents();
		
		
	}
	
	public void abrirTelaProfile() {
		
		User user = userService.getUser(Session.getUsername());
		this.profile = new ProfileWindow(user.getName(), user.getUsername(), this);
		profile.setVisible(true);
		this.setVisible(false);
		
	}
	
	public void fecharJanela() {
		
		this.dispose();
		this.login.setVisible(true);
		
	}
	
	public void voltarParaLogin() {
		
		fecharJanela();
	
	}
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UiStyle.configureFrame(this, "Painel", 1020, 700);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader("Garagem do Tião", "Seu espaço de atendimento."),
				java.awt.BorderLayout.NORTH);

		JPanel mainArea = new JPanel(new java.awt.BorderLayout(20, 0));
		mainArea.setOpaque(false);

		JPanel navigation = UiStyle.createCard();
		navigation.setPreferredSize(new java.awt.Dimension(220, 0));
		navigation.add(UiStyle.createSectionTitle("Navegação"));
		navigation.add(javax.swing.Box.createVerticalStrut(16));

		JButton btn_profile = UiStyle.primaryButton("Meu perfil");
		btn_profile.setAlignmentX(javax.swing.JComponent.LEFT_ALIGNMENT);
		btn_profile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirTelaProfile();
			}
		});
		navigation.add(btn_profile);
		navigation.add(javax.swing.Box.createVerticalStrut(10));

		JButton btn_config = UiStyle.secondaryButton("Alterar servidor");
		btn_config.setAlignmentX(javax.swing.JComponent.LEFT_ALIGNMENT);
		navigation.add(btn_config);
		mainArea.add(navigation, java.awt.BorderLayout.WEST);

		JPanel dataCard = UiStyle.createCard();
		dataCard.add(UiStyle.createSectionTitle("Visão geral"));
		dataCard.add(javax.swing.Box.createVerticalStrut(5));
		dataCard.add(UiStyle.createBodyLabel("As informações disponíveis serão exibidas nesta área."));
		dataCard.add(javax.swing.Box.createVerticalStrut(16));

		table = new JTable();
		table.setRowHeight(32);
		table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
		table.setForeground(UiStyle.TEXT);
		table.setBackground(UiStyle.SURFACE);
		table.setGridColor(UiStyle.BACKGROUND);
		table.setShowVerticalLines(false);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(UiStyle.BORDER));
		scrollPane.getViewport().setBackground(UiStyle.SURFACE);
		scrollPane.setPreferredSize(new java.awt.Dimension(600, 420));
		scrollPane.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		dataCard.add(scrollPane);
		mainArea.add(dataCard, java.awt.BorderLayout.CENTER);
		contentPane.add(mainArea, java.awt.BorderLayout.CENTER);
	}
}
