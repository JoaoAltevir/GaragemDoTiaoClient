package gui;

import service.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UpdatePasswordWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPasswordField tf_oldPassword;
	private JPasswordField tf_newPassword;
	private UserService servico;
	
	/**
	 * Create the frame.
	 */
	public UpdatePasswordWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fecharJanela();
			}
		});
		this.servico = new UserService();
	
		setTitle("Alterar senha");
		
		initComponents();
	}
	
	public void atualizarSenha() {
		
		servico.updateUserPassword(new String(this.tf_oldPassword.getPassword()),
				new String(this.tf_newPassword.getPassword()));
		fecharJanela();

	}
	
	public void fecharJanela() {
		this.dispose();
	}
	
	public void initComponents(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UiStyle.configureFrame(this, "Alterar senha", 520, 500);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);
		contentPane.add(UiStyle.createHeader("Alterar senha", "Escolha uma nova senha para proteger sua conta."),
				java.awt.BorderLayout.NORTH);

		JPanel card = UiStyle.createCard();
		tf_oldPassword = new JPasswordField();
		tf_oldPassword.setColumns(10);
		card.add(UiStyle.createField("Senha atual", tf_oldPassword));
		card.add(javax.swing.Box.createVerticalStrut(16));

		tf_newPassword = new JPasswordField();
		tf_newPassword.setColumns(10);
		card.add(UiStyle.createField("Nova senha", tf_newPassword));
		char oldPasswordEchoChar = tf_oldPassword.getEchoChar();
		char newPasswordEchoChar = tf_newPassword.getEchoChar();
		javax.swing.JCheckBox cb_showPassword = UiStyle.createCheckBox("Mostrar senhas");
		cb_showPassword.addActionListener(e -> {
			tf_oldPassword.setEchoChar(cb_showPassword.isSelected() ? (char) 0 : oldPasswordEchoChar);
			tf_newPassword.setEchoChar(cb_showPassword.isSelected() ? (char) 0 : newPasswordEchoChar);
		});
		cb_showPassword.setAlignmentX(javax.swing.JComponent.LEFT_ALIGNMENT);
		card.add(javax.swing.Box.createVerticalStrut(6));
		card.add(cb_showPassword);
		card.add(javax.swing.Box.createVerticalStrut(18));

		JButton btn_update = UiStyle.primaryButton("Salvar alterações");
		btn_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarSenha();
			}
		});
		JButton btn_exit = UiStyle.secondaryButton("Cancelar");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fecharJanela();
			}
		});
		card.add(UiStyle.createActions(btn_update, btn_exit));
		contentPane.add(card, java.awt.BorderLayout.CENTER);
		getRootPane().setDefaultButton(btn_update);
	}
}
