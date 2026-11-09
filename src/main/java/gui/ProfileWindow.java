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
	private JLabel lbl_role;
	private String name;
	private String username;
	private String role;
	//SERVIÇOS
	private UserService servico;



	/**
	 * Create the frame.
	 */
	public ProfileWindow(String name, String username, MenuWindow menu) {
		this(name, username, entities.Session.getRole(), menu);
	}

	public ProfileWindow(String name, String username, String role, MenuWindow menu) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fecharJanela();
			}
		});
		
		setTitle("Perfil");

		this.servico = new UserService();
		this.menu = menu;
		this.name = name != null ? name : "-";
		this.username = username != null ? username : "-";
		this.role = (role != null && !role.isEmpty()) ? role : "user";
		
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
		
		this.updateUserName = new UpdateUsernameWindow(this);
		this.updateUserName.setVisible(true);
	}
	
	public void refresh(String name) {
		this.name = name;
		this.lbl_name.setText("Nome completo: " + name);
		
	}
	
	public void logout(){
		
		int option = JOptionPane.showConfirmDialog(this, "Você deseja fazer o logout?", "Confirmar saída",
				JOptionPane.YES_NO_OPTION);
		
		if(option == JOptionPane.YES_OPTION) {
			
			com.google.gson.JsonObject res = servico.logout();
			String msg = UiStyle.extractMessage(res, "Sessão encerrada com sucesso.");
			JOptionPane.showMessageDialog(this, msg, "Logout", JOptionPane.INFORMATION_MESSAGE);
			voltarParaLogin();
		}
		
	}
	
	public void deletarUsuario() {
		
		int option = JOptionPane.showConfirmDialog(this,
				"Você deseja realmente excluir a conta? Esse processo é irreversível.",
				"Excluir conta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if(option == JOptionPane.YES_OPTION) {
			
			com.google.gson.JsonObject res = servico.deleteUser();
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Usuário deletado com sucesso.");
			JOptionPane.showMessageDialog(this, msg,
					status == 200 ? "Conta excluída" : "Aviso (" + status + ")",
					status == 200 ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
			voltarParaLogin();
	
		}
		
	}
	
	public void voltarParaLogin() {
		
		this.menu.voltarParaLogin();
		this.dispose();
	
	}
	
	
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UiStyle.configureFrame(this, "Meu perfil", 680, 540);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader("Meu perfil", "Gerencie suas informações e preferências da conta."),
				java.awt.BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();
		card.add(UiStyle.createSectionTitle("Dados da conta"));
		card.add(javax.swing.Box.createVerticalStrut(12));

		JPanel accountInfo = new JPanel(new java.awt.GridLayout(3, 1, 0, 8));
		accountInfo.setOpaque(true);
		accountInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
		lbl_name = new JLabel("Nome completo: " + this.name);
		lbl_name.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
		lbl_name.setForeground(UiStyle.TEXT);
		lbl_username = new JLabel("Nome de usuário: " + this.username);
		lbl_username.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
		lbl_username.setForeground(UiStyle.TEXT);
		lbl_role = new JLabel("Perfil de acesso: " + ("admin".equalsIgnoreCase(this.role) ? "Administrador (admin)" : "Usuário (user)"));
		lbl_role.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
		lbl_role.setForeground(UiStyle.TEXT);
		accountInfo.add(lbl_name);
		accountInfo.add(lbl_username);
		accountInfo.add(lbl_role);
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
