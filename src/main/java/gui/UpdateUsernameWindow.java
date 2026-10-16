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
	
	/**
	 * Create the frame.
	 */
	public UpdateUsernameWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fecharJanela();
			}
		});
		this.servico = new UserService();
	
		setTitle("Alterar nome");
		
		initComponents();
	}
	
	public void atualizarNome() {
		
		servico.updateUserName(this.tf_newName.getText());
		fecharJanela();

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
