package gui;

import service.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UpdateUsernameWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_newName;
	private UserService servico;
	private ProfileWindow perfil;
	
	/**
	 * Create the frame.
	 */
	public UpdateUsernameWindow(ProfileWindow p) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fecharJanela();
			}
		});
		
		this.perfil = p;
		this.servico = new UserService();
	
		setTitle("Alterar nome");
		
		initComponents();
	}
	
	public void atualizarNome() {
		String novoNome = this.tf_newName.getText().trim();
		if (novoNome.isEmpty()) {
			javax.swing.JOptionPane.showMessageDialog(this, "Informe o novo nome.", "Campo obrigatório",
					javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		com.google.gson.JsonObject res = servico.updateUserName(novoNome);
		int status = UiStyle.extractStatusCode(res);
		String msg = UiStyle.extractMessage(res, "Não foi possível atualizar o nome.");

		if (status == 200) {
			javax.swing.JOptionPane.showMessageDialog(this, msg, "Sucesso",
					javax.swing.JOptionPane.INFORMATION_MESSAGE);
			if (perfil != null) {
				perfil.refresh(novoNome);
			}
			fecharJanela();
		} else if (UiStyle.isSessionExpired(res)) {
			javax.swing.JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)",
					javax.swing.JOptionPane.WARNING_MESSAGE);
			fecharJanela();
			if (perfil != null) {
				perfil.voltarParaLogin();
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void fecharJanela() {
		this.dispose();
	}
	
	public void initComponents(){
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UiStyle.configureFrame(this, "Alterar nome", 500, 390);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);
		contentPane.add(UiStyle.createHeader("Alterar nome", "Informe o novo nome que será exibido no seu perfil."),
				java.awt.BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();

		tf_newName = new JTextField();
		tf_newName.setColumns(10);
		card.add(UiStyle.createField("Novo nome", tf_newName));
		card.add(javax.swing.Box.createVerticalStrut(20));

		JButton btn_update = UiStyle.primaryButton("Salvar alterações");
		btn_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarNome();
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
