package gui;

import java.awt.EventQueue;

import service.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UpdatePasswordWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_oldPassword;
	private JTextField tf_newPassword;
	private JLabel lbl_newPassword;
	private JLabel lbl_oldPassword;
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
		
		servico.updateUserPassword(this.tf_oldPassword.getText(), this.tf_newPassword.getText());
		fecharJanela();

	}
	
	public void fecharJanela() {
		this.dispose();
	}
	
	public void initComponents(){
		
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 189, 235);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lbl_oldPassword = new JLabel("Insira sua antiga senha: ");
		lbl_oldPassword.setBounds(10, 11, 143, 41);
		contentPane.add(lbl_oldPassword);
		
		tf_oldPassword = new JTextField();
		tf_oldPassword.setBounds(10, 48, 143, 20);
		contentPane.add(tf_oldPassword);
		tf_oldPassword.setColumns(10);
		
		lbl_newPassword = new JLabel("Insira sua nova senha:");
		lbl_newPassword.setBounds(10, 62, 143, 30);
		contentPane.add(lbl_newPassword);
		
		tf_newPassword = new JTextField();
		tf_newPassword.setBounds(10, 96, 143, 20);
		contentPane.add(tf_newPassword);
		tf_newPassword.setColumns(10);
		
		JButton btn_update = new JButton("Atualizar");
		btn_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarSenha();
			}
		});
		btn_update.setBounds(10, 127, 89, 23);
		contentPane.add(btn_update);
		
		JButton btn_exit = new JButton("Sair");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fecharJanela();
			}
		});
		btn_exit.setBounds(10, 155, 89, 23);
		contentPane.add(btn_exit);
		
		setLocationRelativeTo(null);
		
	}
}
