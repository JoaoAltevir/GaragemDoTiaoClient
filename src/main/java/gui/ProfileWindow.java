package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
	private UpdatePasswordWindow updatePassword;
	private UpdateUsernameWindow updateUserName;
	//componentes
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
			public void windowClosing(WindowEvent e) {
				fecharJanela();
			}
		});
		
		setTitle("Perfil");

		this.servico = new UserService();
		this.menu = menu;
		this.name = name;
		this.username = username;
		
		initComponents();
		
		
	}
	
	public void fecharJanela() {
		
		this.dispose();
		this.menu.setVisible(true);
		
	}
	
	public void atualizarSenha() {
		
		this.updatePassword = new UpdatePasswordWindow();
		this.updatePassword.setVisible(true);
	}
	
	public void atualizarNome() {
		
		this.updateUserName = new UpdateUsernameWindow();
		this.updateUserName.setVisible(true);
	}
	
	public void logout(){
		
		int option = JOptionPane.showConfirmDialog(this, "Você deseja fazer o logout?");
		
		if(option == JOptionPane.YES_OPTION) {
			
			servico.logout();	
			voltarParaLogin();
		}
		
	}
	
	public void deletarUsuario() {
		
		int option = JOptionPane.showConfirmDialog(this, "Você deseja realmente excluir a conta? Esse processo é irreversivel");
		
		if(option == JOptionPane.YES_OPTION) {
			
			servico.deleteUser();
			voltarParaLogin();
	
		}
		
	}
	
	public void voltarParaLogin() {
		
		this.menu.voltarParaLogin();
		this.dispose();
	
	}
	
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UiStyle.configureFrame(this, "Meu perfil", 680, 520);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader("Meu perfil", "Gerencie suas informações e preferências da conta."),
				java.awt.BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();
		card.add(UiStyle.createSectionTitle("Dados da conta"));
		card.add(javax.swing.Box.createVerticalStrut(12));

		JPanel accountInfo = new JPanel(new java.awt.GridLayout(2, 1, 0, 8));
		accountInfo.setOpaque(true);
		accountInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
		lbl_name = new JLabel("Nome completo: " + this.name);
		lbl_name.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
		lbl_name.setForeground(UiStyle.TEXT);
		lbl_username = new JLabel("Nome de usuário: " + this.username);
		lbl_username.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
		lbl_username.setForeground(UiStyle.TEXT);
		accountInfo.add(lbl_name);
		accountInfo.add(lbl_username);
		accountInfo.setBackground(UiStyle.BACKGROUND);
		card.add(accountInfo);
		card.add(javax.swing.Box.createVerticalStrut(20));
		card.add(UiStyle.createSectionTitle("Segurança e sessão"));
		card.add(javax.swing.Box.createVerticalStrut(12));

		btn_updatePassword = UiStyle.secondaryButton("Alterar senha");
		btn_updatePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarSenha();
			}
		});
		btn_updateNome = UiStyle.secondaryButton("Alterar nome");
		btn_updateNome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarNome();
			}
		});
		btnNewButton = UiStyle.secondaryButton("Sair da conta");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		card.add(UiStyle.createActions(btn_updatePassword, btn_updateNome, btnNewButton));
		card.add(javax.swing.Box.createVerticalStrut(16));

		btn_deleteUser = UiStyle.dangerButton("Excluir conta");
		btn_deleteUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletarUsuario();
			}
		});
		card.add(UiStyle.createActions(btn_deleteUser));
		contentPane.add(card, java.awt.BorderLayout.CENTER);
	}
}
