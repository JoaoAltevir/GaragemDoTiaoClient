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

public class UpdateUsernameWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_newName;
	private JLabel lbl_newName;
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
		setBounds(100, 100, 189, 188);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lbl_newName = new JLabel("Insira seu novo nome:");
		lbl_newName.setBounds(10, 11, 143, 41);
		contentPane.add(lbl_newName);
		
		tf_newName = new JTextField();
		tf_newName.setBounds(10, 48, 143, 20);
		contentPane.add(tf_newName);
		tf_newName.setColumns(10);
		
		JButton btn_update = new JButton("Atualizar");
		btn_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarNome();
			}
		});
		btn_update.setBounds(10, 79, 89, 23);
		contentPane.add(btn_update);
		
		JButton btn_exit = new JButton("Sair");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fecharJanela();
			}
		});
		btn_exit.setBounds(10, 113, 89, 23);
		contentPane.add(btn_exit);
		
		setLocationRelativeTo(null);
		
	}
}
