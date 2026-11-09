package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.google.gson.JsonObject;

import entities.Session;
import service.UserService;

public class AdminUserWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private MenuWindow menu;
	private UserService userService;

	private JTextField tf_name;
	private JTextField tf_username;
	private JPasswordField tf_password;
	private JComboBox<String> cb_role;
	private JCheckBox cb_showPassword;

	public AdminUserWindow(MenuWindow menu) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fecharJanela();
			}
		});

		this.menu = menu;
		this.userService = new UserService();

		initComponents();
	}

	public void fecharJanela() {
		this.dispose();
	}

	public void cadastrarUsuario() {
		String name = tf_name.getText().trim();
		String username = tf_username.getText().trim();
		String password = new String(tf_password.getPassword());
		String role = (String) cb_role.getSelectedItem();

		if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Preencha nome, nome de usuário e senha.", "Campos obrigatórios",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		JsonObject res = userService.register(name, username, password, role, Session.getToken());
		int status = UiStyle.extractStatusCode(res);
		String msg = UiStyle.extractMessage(res, "Operação realizada.");

		if (status == 201 || status == 200) {
			JOptionPane.showMessageDialog(this, msg, "Usuário criado", JOptionPane.INFORMATION_MESSAGE);
			if (menu != null) {
				menu.consultarUsuarioAdmin(username);
			}
			fecharJanela();
		} else {
			JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UiStyle.configureFrame(this, "Cadastrar usuário (Admin)", 540, 620);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader("Cadastrar usuário",
				"Crie uma nova conta informando o perfil de permissão (user ou admin)."), BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();

		tf_name = new JTextField();
		tf_name.setColumns(10);
		card.add(UiStyle.createField("Nome completo", tf_name));
		card.add(Box.createVerticalStrut(14));

		tf_username = new JTextField();
		tf_username.setColumns(10);
		card.add(UiStyle.createField("Nome de usuário", tf_username));
		card.add(Box.createVerticalStrut(14));

		tf_password = new JPasswordField();
		tf_password.setColumns(10);
		card.add(UiStyle.createField("Senha", tf_password));
		char echo = tf_password.getEchoChar();
		cb_showPassword = UiStyle.createCheckBox("Mostrar senha");
		cb_showPassword.addActionListener(e -> tf_password.setEchoChar(cb_showPassword.isSelected() ? (char) 0 : echo));
		cb_showPassword.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		card.add(Box.createVerticalStrut(5));
		card.add(cb_showPassword);
		card.add(Box.createVerticalStrut(14));

		cb_role = new JComboBox<>(new String[] { "user", "admin" });
		card.add(UiStyle.createField("Perfil de acesso (Role)", cb_role));
		card.add(Box.createVerticalStrut(18));

		JButton btn_save = UiStyle.primaryButton("Criar usuário");
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrarUsuario();
			}
		});

		JButton btn_cancel = UiStyle.secondaryButton("Cancelar");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fecharJanela();
			}
		});

		card.add(UiStyle.createActions(btn_save, btn_cancel));
		contentPane.add(card, BorderLayout.CENTER);
		getRootPane().setDefaultButton(btn_save);
	}
}
