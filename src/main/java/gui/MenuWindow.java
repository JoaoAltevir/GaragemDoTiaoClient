package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonObject;

import entities.Session;
import entities.User;
import entities.Vaga;
import entities.Vehicle;
import service.UserService;
import service.VagaService;
import service.VehicleService;

public class MenuWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String PANEL_VAGAS = "VAGAS";
	private static final String PANEL_VEICULOS = "VEICULOS";
	private static final String PANEL_ADMIN = "ADMIN";

	private JPanel contentPane;
	private JPanel centerCards;
	private CardLayout cardLayout;

	// Tabelas e modelos
	private JTable table;
	private DefaultTableModel vagasTableModel;
	private JTable tableVehicles;
	private DefaultTableModel vehiclesTableModel;

	// Dados em memória
	private List<Vaga> listaVagas = new ArrayList<>();
	private List<Vehicle> listaVeiculos = new ArrayList<>();

	// Indicadores de vagas
	private JLabel lbl_totalVagas;
	private JLabel lbl_vagasDisponiveis;
	private JLabel lbl_vagasOcupadas;
	private JLabel lbl_vagasManutencao;
	private JLabel lbl_statusVagas;
	private JTextField tf_findPlate;

	// Componentes de veículos
	private JLabel lbl_statusVeiculos;
	private JTextField tf_searchVehiclePlate;

	// Componentes de administração
	private JTextField tf_adminUsername;
	private JLabel lbl_adminUserNameVal;
	private JLabel lbl_adminUserUsernameVal;
	private JLabel lbl_adminUserRoleVal;
	private JLabel lbl_statusAdmin;
	private JTextField tf_adminPlate;
	private String ultimoUsernameConsultado;

	// Rótulo de sessão no menu lateral
	private JLabel lbl_sessionUser;
	private JLabel lbl_sessionRole;

	// GUI'S
	ProfileWindow profile;
	LoginWindow login;

	// SERVIÇOS
	UserService userService;
	VagaService vagaService;
	VehicleService vehicleService;

	/**
	 * Create the frame.
	 */
	public MenuWindow(LoginWindow login) {
		setTitle("Menu");

		this.login = login;
		this.userService = new UserService();
		this.vagaService = new VagaService();
		this.vehicleService = new VehicleService();

		initComponents();
		atualizarResumoSessao();
		carregarVagas();
	}

	public void atualizarResumoSessao() {
		String uname = Session.getUsername() != null ? Session.getUsername() : "-";
		String role = Session.getRole();
		if (role == null || role.isEmpty()) {
			try {
				User u = userService.getUser(uname);
				if (u != null && u.getRole() != null) {
					role = u.getRole();
				}
			} catch (Exception ignored) {
			}
		}
		if (lbl_sessionUser != null) {
			lbl_sessionUser.setText("Usuário: " + uname);
		}
		if (lbl_sessionRole != null) {
			String roleTexto = "admin".equalsIgnoreCase(role) ? "Administrador" : "Usuário";
			lbl_sessionRole.setText("Perfil: " + roleTexto);
		}
	}

	public void abrirTelaProfile() {
		User user = userService.getUser(Session.getUsername());
		JsonObject res = userService.getRes();
		if (UiStyle.isSessionExpired(res)) {
			String msg = UiStyle.extractMessage(res, "Sessão expirada ou encerrada.");
			JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
			voltarParaLogin();
			return;
		}
		String nome = (user != null && user.getName() != null) ? user.getName() : Session.getUsername();
		String uname = (user != null && user.getUsername() != null) ? user.getUsername() : Session.getUsername();
		String role = (user != null && user.getRole() != null) ? user.getRole() : Session.getRole();

		this.profile = new ProfileWindow(nome, uname, role, this);
		profile.setVisible(true);
		this.setVisible(false);
	}

	public void alterarServidor() {
		int opt = JOptionPane.showConfirmDialog(this,
				"Deseja desconectar da sessão atual e alterar o endereço do servidor?",
				"Alterar servidor", JOptionPane.YES_NO_OPTION);
		if (opt == JOptionPane.YES_OPTION) {
			try {
				if (Session.getToken() != null) {
					userService.logout();
				}
			} catch (Exception ignored) {
			}
			this.dispose();
			if (this.login != null) {
				this.login.dispose();
			}
			LauncherWindow launcher = new LauncherWindow();
			launcher.setVisible(true);
		}
	}

	public void fecharJanela() {
		this.dispose();
		if (this.login != null) {
			this.login.setVisible(true);
		}
	}

	public void voltarParaLogin() {
		fecharJanela();
	}

	// ==================== OPERAÇÕES DE VAGAS ====================

	public void carregarVagas() {
		if (lbl_statusVagas != null) {
			lbl_statusVagas.setForeground(UiStyle.MUTED);
			lbl_statusVagas.setText("Carregando vagas...");
		}

		listaVagas = vagaService.getVagas();
		JsonObject res = vagaService.getRes();
		int status = UiStyle.extractStatusCode(res);

		vagasTableModel.setRowCount(0);
		int disp = 0;
		int ocup = 0;
		int manut = 0;

		if (status == 200) {
			for (Vaga v : listaVagas) {
				String st = v.getStatus() != null ? v.getStatus() : "-";
				if ("disponivel".equalsIgnoreCase(st)) {
					disp++;
				} else if ("ocupada".equalsIgnoreCase(st)) {
					ocup++;
				} else if ("fora_de_servico".equalsIgnoreCase(st)) {
					manut++;
				}

				vagasTableModel.addRow(new Object[] {
						v.getFloor(),
						v.getSpotName() != null ? v.getSpotName() : "-",
						v.getType() != null ? v.getType() : "-",
						st,
						(v.getLicensePlate() != null && !v.getLicensePlate().isEmpty()) ? v.getLicensePlate() : "-"
				});
			}

			lbl_totalVagas.setText(String.valueOf(listaVagas.size()));
			lbl_vagasDisponiveis.setText(String.valueOf(disp));
			lbl_vagasOcupadas.setText(String.valueOf(ocup));
			lbl_vagasManutencao.setText(String.valueOf(manut));

			if (listaVagas.isEmpty()) {
				lbl_statusVagas.setForeground(UiStyle.MUTED);
				lbl_statusVagas.setText("Nenhuma vaga cadastrada no momento.");
			} else {
				lbl_statusVagas.setForeground(UiStyle.SUCCESS);
				lbl_statusVagas.setText(UiStyle.extractMessage(res, "Vagas obtidas com sucesso.")
						+ " (" + listaVagas.size() + " registro(s))");
			}
		} else {
			lbl_totalVagas.setText("0");
			lbl_vagasDisponiveis.setText("0");
			lbl_vagasOcupadas.setText("0");
			lbl_vagasManutencao.setText("0");
			String msg = UiStyle.extractMessage(res, "Não foi possível carregar as vagas.");
			lbl_statusVagas.setForeground(UiStyle.DANGER);
			lbl_statusVagas.setText("Erro (" + status + "): " + msg);
			if (UiStyle.isSessionExpired(res)) {
				JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
				voltarParaLogin();
			}
		}
	}

	public void abrirNovaVaga() {
		VagaWindow window = new VagaWindow(this);
		window.setVisible(true);
	}

	public void abrirEdicaoVaga() {
		int row = table.getSelectedRow();
		if (row >= 0 && row < listaVagas.size()) {
			Vaga selecionada = listaVagas.get(row);
			VagaWindow window = new VagaWindow(this, selecionada);
			window.setVisible(true);
		} else {
			String spotName = JOptionPane.showInputDialog(this,
					"Selecione uma vaga na tabela ou digite o nome da vaga que deseja atualizar:",
					"Atualizar vaga", JOptionPane.QUESTION_MESSAGE);
			if (spotName != null && !spotName.trim().isEmpty()) {
				Vaga vagaManual = new Vaga();
				vagaManual.setSpotName(spotName.trim());
				vagaManual.setFloor(1);
				vagaManual.setStatus("disponivel");
				VagaWindow window = new VagaWindow(this, vagaManual);
				window.setVisible(true);
			}
		}
	}

	public void excluirVagaSelecionada() {
		String spotName = null;
		int row = table.getSelectedRow();
		if (row >= 0 && row < listaVagas.size()) {
			spotName = listaVagas.get(row).getSpotName();
		} else {
			spotName = JOptionPane.showInputDialog(this,
					"Selecione uma vaga na tabela ou informe o nome da vaga a remover:",
					"Remover vaga", JOptionPane.QUESTION_MESSAGE);
		}

		if (spotName == null || spotName.trim().isEmpty()) {
			return;
		}
		spotName = spotName.trim();

		int confirm = JOptionPane.showConfirmDialog(this,
				"Deseja realmente remover a vaga '" + spotName + "'?",
				"Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (confirm == JOptionPane.YES_OPTION) {
			JsonObject res = vagaService.deleteVaga(spotName);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação concluída.");

			if (status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Vaga removida", JOptionPane.INFORMATION_MESSAGE);
				carregarVagas();
			} else if (UiStyle.isSessionExpired(res)) {
				JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
				voltarParaLogin();
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void localizarVeiculoPorPlaca(String placaInput) {
		String placa = placaInput != null ? placaInput.trim() : "";
		if (placa.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe a placa do veículo que deseja localizar.",
					"Campo obrigatório", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Vaga vagaEncontrada = vagaService.findVehicle(placa);
		JsonObject res = vagaService.getRes();
		int status = UiStyle.extractStatusCode(res);
		String msg = UiStyle.extractMessage(res, "Consulta realizada.");

		if (status == 200 && vagaEncontrada != null) {
			for (int i = 0; i < listaVagas.size(); i++) {
				Vaga v = listaVagas.get(i);
				if (v.getSpotName() != null && v.getSpotName().equalsIgnoreCase(vagaEncontrada.getSpotName())) {
					table.setRowSelectionInterval(i, i);
					table.scrollRectToVisible(table.getCellRect(i, 0, true));
					break;
				}
			}

			String detalhes = String.format(
					"%s\n\nPlaca: %s\nVaga: %s\nAndar: %d\nStatus: %s",
					msg,
					placa,
					vagaEncontrada.getSpotName() != null ? vagaEncontrada.getSpotName() : "-",
					vagaEncontrada.getFloor(),
					vagaEncontrada.getStatus() != null ? vagaEncontrada.getStatus() : "-");
			JOptionPane.showMessageDialog(this, detalhes, "Veículo localizado", JOptionPane.INFORMATION_MESSAGE);
		} else if (UiStyle.isSessionExpired(res)) {
			JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
			voltarParaLogin();
		} else {
			JOptionPane.showMessageDialog(this, msg, "Não localizado (" + status + ")", JOptionPane.WARNING_MESSAGE);
		}
	}

	// ==================== OPERAÇÕES DE VEÍCULOS ====================

	public void carregarVeiculos() {
		if (lbl_statusVeiculos != null) {
			lbl_statusVeiculos.setForeground(UiStyle.MUTED);
			lbl_statusVeiculos.setText("Carregando veículos...");
		}

		listaVeiculos = vehicleService.getVehicles();
		JsonObject res = vehicleService.getRes();
		int status = UiStyle.extractStatusCode(res);

		vehiclesTableModel.setRowCount(0);

		if (status == 200) {
			for (Vehicle v : listaVeiculos) {
				vehiclesTableModel.addRow(new Object[] {
						v.getLicensePlate() != null ? v.getLicensePlate() : "-",
						v.getBrand() != null ? v.getBrand() : "-",
						v.getModel() != null ? v.getModel() : "-",
						v.getType() != null ? v.getType() : "-"
				});
			}

			if (listaVeiculos.isEmpty()) {
				lbl_statusVeiculos.setForeground(UiStyle.MUTED);
				lbl_statusVeiculos.setText("Você ainda não possui veículos cadastrados.");
			} else {
				lbl_statusVeiculos.setForeground(UiStyle.SUCCESS);
				lbl_statusVeiculos.setText(UiStyle.extractMessage(res, "Veículos obtidos com sucesso.")
						+ " (" + listaVeiculos.size() + " veículo(s))");
			}
		} else {
			String msg = UiStyle.extractMessage(res, "Não foi possível obter os veículos.");
			lbl_statusVeiculos.setForeground(UiStyle.DANGER);
			lbl_statusVeiculos.setText("Erro (" + status + "): " + msg);
			if (UiStyle.isSessionExpired(res)) {
				JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
				voltarParaLogin();
			}
		}
	}

	public void abrirNovoVeiculo() {
		VehicleWindow window = new VehicleWindow(this);
		window.setVisible(true);
	}

	public void abrirEdicaoVeiculo() {
		int row = tableVehicles.getSelectedRow();
		if (row >= 0 && row < listaVeiculos.size()) {
			Vehicle selecionado = listaVeiculos.get(row);
			VehicleWindow window = new VehicleWindow(this, selecionado);
			window.setVisible(true);
		} else {
			String placa = JOptionPane.showInputDialog(this,
					"Selecione um veículo na tabela ou informe a placa para editar:",
					"Editar veículo", JOptionPane.QUESTION_MESSAGE);
			if (placa != null && !placa.trim().isEmpty()) {
				Vehicle v = vehicleService.getVehicle(placa.trim());
				int st = UiStyle.extractStatusCode(vehicleService.getRes());
				if (st == 200 && v != null && v.getLicensePlate() != null) {
					VehicleWindow window = new VehicleWindow(this, v);
					window.setVisible(true);
				} else {
					Vehicle manual = new Vehicle("", "", "combustao", placa.trim());
					VehicleWindow window = new VehicleWindow(this, manual);
					window.setVisible(true);
				}
			}
		}
	}

	public void consultarVeiculoPorPlaca(String placaInput) {
		String placa = placaInput != null ? placaInput.trim() : "";
		if (placa.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe a placa do veículo para consulta.",
					"Campo obrigatório", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Vehicle v = vehicleService.getVehicle(placa);
		JsonObject res = vehicleService.getRes();
		int status = UiStyle.extractStatusCode(res);
		String msg = UiStyle.extractMessage(res, "Consulta realizada.");

		if (status == 200 && v != null && v.getLicensePlate() != null) {
			String detalhes = String.format(
					"%s\n\nPlaca: %s\nMarca: %s\nModelo: %s\nTipo: %s",
					msg,
					v.getLicensePlate(),
					v.getBrand() != null ? v.getBrand() : "-",
					v.getModel() != null ? v.getModel() : "-",
					v.getType() != null ? v.getType() : "-");
			JOptionPane.showMessageDialog(this, detalhes, "Dados do veículo", JOptionPane.INFORMATION_MESSAGE);
		} else if (UiStyle.isSessionExpired(res)) {
			JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
			voltarParaLogin();
		} else {
			JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void excluirVeiculoSelecionado() {
		String placa = null;
		int row = tableVehicles.getSelectedRow();
		if (row >= 0 && row < listaVeiculos.size()) {
			placa = listaVeiculos.get(row).getLicensePlate();
		} else {
			placa = JOptionPane.showInputDialog(this,
					"Selecione um veículo na tabela ou informe a placa a remover:",
					"Remover veículo", JOptionPane.QUESTION_MESSAGE);
		}

		excluirVeiculoPorPlaca(placa);
	}

	public void excluirVeiculoPorPlaca(String placa) {
		if (placa == null || placa.trim().isEmpty()) {
			return;
		}
		placa = placa.trim();

		int confirm = JOptionPane.showConfirmDialog(this,
				"Deseja realmente remover o veículo de placa '" + placa + "'?",
				"Confirmar remoção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (confirm == JOptionPane.YES_OPTION) {
			JsonObject res = vehicleService.deleteVehicle(placa);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação concluída.");

			if (status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Veículo removido", JOptionPane.INFORMATION_MESSAGE);
				carregarVeiculos();
			} else if (UiStyle.isSessionExpired(res)) {
				JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
				voltarParaLogin();
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ==================== OPERAÇÕES DE ADMINISTRAÇÃO ====================

	public void consultarUsuarioAdmin(String usernameInput) {
		String target = usernameInput != null ? usernameInput.trim() : "";
		if (target.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe o nome de usuário (username) para consultar.",
					"Campo obrigatório", JOptionPane.WARNING_MESSAGE);
			return;
		}

		tf_adminUsername.setText(target);
		User u = userService.getUser(target);
		JsonObject res = userService.getRes();
		int status = UiStyle.extractStatusCode(res);
		String msg = UiStyle.extractMessage(res, "Consulta concluída.");

		if (status == 200 && u != null && u.getUsername() != null) {
			ultimoUsernameConsultado = u.getUsername();
			lbl_adminUserNameVal.setText("Nome completo: " + (u.getName() != null ? u.getName() : "-"));
			lbl_adminUserUsernameVal.setText("Nome de usuário: " + u.getUsername());
			lbl_adminUserRoleVal.setText("Perfil (Role): " + (u.getRole() != null ? u.getRole() : "user"));
			lbl_statusAdmin.setForeground(UiStyle.SUCCESS);
			lbl_statusAdmin.setText(msg);
		} else {
			ultimoUsernameConsultado = null;
			lbl_adminUserNameVal.setText("Nome completo: -");
			lbl_adminUserUsernameVal.setText("Nome de usuário: -");
			lbl_adminUserRoleVal.setText("Perfil (Role): -");
			lbl_statusAdmin.setForeground(UiStyle.DANGER);
			lbl_statusAdmin.setText("Erro (" + status + "): " + msg);
			if (UiStyle.isSessionExpired(res)) {
				JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
				voltarParaLogin();
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void alterarNomeUsuarioAdmin() {
		String target = tf_adminUsername.getText().trim();
		if (target.isEmpty() && ultimoUsernameConsultado != null) {
			target = ultimoUsernameConsultado;
		}
		if (target.isEmpty()) {
			target = JOptionPane.showInputDialog(this, "Informe o username do usuário que terá o nome alterado:",
					"Alterar nome (Admin)", JOptionPane.QUESTION_MESSAGE);
		}
		if (target == null || target.trim().isEmpty()) {
			return;
		}
		target = target.trim();

		String novoNome = JOptionPane.showInputDialog(this,
				"Informe o novo nome completo para o usuário '" + target + "':",
				"Alterar nome de usuário", JOptionPane.QUESTION_MESSAGE);
		if (novoNome == null || novoNome.trim().isEmpty()) {
			return;
		}

		JsonObject res = userService.updateUserName(target, novoNome.trim());
		int status = UiStyle.extractStatusCode(res);
		String msg = UiStyle.extractMessage(res, "Operação realizada.");

		if (status == 200) {
			JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			consultarUsuarioAdmin(target);
		} else if (UiStyle.isSessionExpired(res)) {
			JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
			voltarParaLogin();
		} else {
			JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void excluirUsuarioAdmin() {
		String target = tf_adminUsername.getText().trim();
		if (target.isEmpty() && ultimoUsernameConsultado != null) {
			target = ultimoUsernameConsultado;
		}
		if (target.isEmpty()) {
			target = JOptionPane.showInputDialog(this, "Informe o username do usuário que deseja excluir:",
					"Excluir usuário (Admin)", JOptionPane.QUESTION_MESSAGE);
		}
		if (target == null || target.trim().isEmpty()) {
			return;
		}
		target = target.trim();

		int confirm = JOptionPane.showConfirmDialog(this,
				"Tem certeza que deseja excluir permanentemente o usuário '" + target + "'?",
				"Confirmar exclusão de usuário", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (confirm == JOptionPane.YES_OPTION) {
			boolean autoExclusao = target.equals(Session.getUsername());
			JsonObject res = userService.deleteUser(target);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação realizada.");

			if (status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Usuário excluído", JOptionPane.INFORMATION_MESSAGE);
				if (autoExclusao) {
					voltarParaLogin();
					return;
				}
				ultimoUsernameConsultado = null;
				tf_adminUsername.setText("");
				lbl_adminUserNameVal.setText("Nome completo: -");
				lbl_adminUserUsernameVal.setText("Nome de usuário: -");
				lbl_adminUserRoleVal.setText("Perfil (Role): -");
				lbl_statusAdmin.setForeground(UiStyle.SUCCESS);
				lbl_statusAdmin.setText(msg);
			} else if (UiStyle.isSessionExpired(res)) {
				JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
				voltarParaLogin();
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ==================== CONSTRUÇÃO DA INTERFACE ====================

	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UiStyle.configureFrame(this, "Painel - Garagem do Tião", 1120, 740);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader("Garagem do Tião",
				"Gerencie vagas, veículos e usuários em um único painel."), BorderLayout.NORTH);

		JPanel mainArea = new JPanel(new BorderLayout(20, 0));
		mainArea.setOpaque(false);

		// Menu lateral de navegação
		JPanel navigation = UiStyle.createCard();
		navigation.setPreferredSize(new Dimension(240, 0));
		navigation.add(UiStyle.createSectionTitle("Navegação"));
		navigation.add(Box.createVerticalStrut(8));

		lbl_sessionUser = UiStyle.createBodyLabel("Usuário: " + (Session.getUsername() != null ? Session.getUsername() : "-"));
		lbl_sessionUser.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		navigation.add(lbl_sessionUser);

		lbl_sessionRole = UiStyle.createBodyLabel("Perfil: -");
		lbl_sessionRole.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		navigation.add(lbl_sessionRole);
		navigation.add(Box.createVerticalStrut(18));

		JButton btn_navVagas = UiStyle.primaryButton("Vagas da garagem");
		btn_navVagas.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		btn_navVagas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		btn_navVagas.addActionListener(e -> {
			cardLayout.show(centerCards, PANEL_VAGAS);
			carregarVagas();
		});
		navigation.add(btn_navVagas);
		navigation.add(Box.createVerticalStrut(10));

		JButton btn_navVeiculos = UiStyle.secondaryButton("Meus veículos");
		btn_navVeiculos.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		btn_navVeiculos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		btn_navVeiculos.addActionListener(e -> {
			cardLayout.show(centerCards, PANEL_VEICULOS);
			carregarVeiculos();
		});
		navigation.add(btn_navVeiculos);
		navigation.add(Box.createVerticalStrut(10));

		JButton btn_navAdmin = UiStyle.secondaryButton("Administração");
		btn_navAdmin.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		btn_navAdmin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		btn_navAdmin.addActionListener(e -> cardLayout.show(centerCards, PANEL_ADMIN));
		navigation.add(btn_navAdmin);
		navigation.add(Box.createVerticalStrut(24));

		navigation.add(UiStyle.createSectionTitle("Conta e conexão"));
		navigation.add(Box.createVerticalStrut(12));

		JButton btn_profile = UiStyle.secondaryButton("Meu perfil");
		btn_profile.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		btn_profile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		btn_profile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirTelaProfile();
			}
		});
		navigation.add(btn_profile);
		navigation.add(Box.createVerticalStrut(10));

		JButton btn_config = UiStyle.secondaryButton("Alterar servidor");
		btn_config.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		btn_config.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		btn_config.addActionListener(e -> alterarServidor());
		navigation.add(btn_config);

		mainArea.add(navigation, BorderLayout.WEST);

		// Área central com CardLayout
		cardLayout = new CardLayout();
		centerCards = new JPanel(cardLayout);
		centerCards.setOpaque(false);

		centerCards.add(criarPainelVagas(), PANEL_VAGAS);
		centerCards.add(criarPainelVeiculos(), PANEL_VEICULOS);
		centerCards.add(criarPainelAdmin(), PANEL_ADMIN);

		mainArea.add(centerCards, BorderLayout.CENTER);
		contentPane.add(mainArea, BorderLayout.CENTER);
	}

	private JPanel criarPainelVagas() {
		JPanel dataCard = UiStyle.createCard();

		JLabel title = UiStyle.createSectionTitle("Vagas da garagem");
		title.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		dataCard.add(title);
		dataCard.add(Box.createVerticalStrut(4));

		JLabel subtitle = UiStyle.createBodyLabel(
				"Visualize todas as vagas, localize veículos estacionados e gerencie a ocupação.");
		subtitle.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		dataCard.add(subtitle);
		dataCard.add(Box.createVerticalStrut(14));

		// Indicadores (Cards de resumo)
		JPanel statsRow = new JPanel(new GridLayout(1, 4, 12, 0));
		statsRow.setOpaque(false);
		statsRow.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 74));

		lbl_totalVagas = new JLabel("0");
		lbl_vagasDisponiveis = new JLabel("0");
		lbl_vagasOcupadas = new JLabel("0");
		lbl_vagasManutencao = new JLabel("0");

		statsRow.add(UiStyle.createStatCard("Total de vagas", lbl_totalVagas));
		statsRow.add(UiStyle.createStatCard("Disponíveis", lbl_vagasDisponiveis));
		statsRow.add(UiStyle.createStatCard("Ocupadas", lbl_vagasOcupadas));
		statsRow.add(UiStyle.createStatCard("Fora de serviço", lbl_vagasManutencao));

		dataCard.add(statsRow);
		dataCard.add(Box.createVerticalStrut(14));

		// Barra de busca por placa (FindVehicle)
		JPanel searchRow = new JPanel(new BorderLayout(10, 0));
		searchRow.setOpaque(false);
		searchRow.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

		tf_findPlate = new JTextField();
		UiStyle.styleField(tf_findPlate);
		tf_findPlate.setToolTipText("Digite a placa do veículo (ex: ABC1234)");

		JButton btn_findVehicle = UiStyle.secondaryButton("Localizar veículo por placa");
		btn_findVehicle.setPreferredSize(new Dimension(220, 42));
		btn_findVehicle.addActionListener(e -> localizarVeiculoPorPlaca(tf_findPlate.getText()));

		searchRow.add(tf_findPlate, BorderLayout.CENTER);
		searchRow.add(btn_findVehicle, BorderLayout.EAST);
		dataCard.add(searchRow);
		dataCard.add(Box.createVerticalStrut(14));

		// Tabela de vagas
		vagasTableModel = new DefaultTableModel(
				new Object[] { "Andar", "Vaga", "Tipo", "Status", "Placa do Veículo" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(vagasTableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		UiStyle.styleTable(table);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		scrollPane.setBorder(BorderFactory.createLineBorder(UiStyle.BORDER));
		scrollPane.getViewport().setBackground(UiStyle.SURFACE);
		scrollPane.setPreferredSize(new Dimension(600, 290));
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		dataCard.add(scrollPane);
		dataCard.add(Box.createVerticalStrut(10));

		lbl_statusVagas = UiStyle.createBodyLabel("Pronto.");
		lbl_statusVagas.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		dataCard.add(lbl_statusVagas);
		dataCard.add(Box.createVerticalStrut(12));

		JButton btn_newVaga = UiStyle.primaryButton("Nova vaga");
		btn_newVaga.addActionListener(e -> abrirNovaVaga());

		JButton btn_editVaga = UiStyle.secondaryButton("Editar / Ocupar vaga");
		btn_editVaga.setPreferredSize(new Dimension(175, 44));
		btn_editVaga.addActionListener(e -> abrirEdicaoVaga());

		JButton btn_deleteVaga = UiStyle.dangerButton("Excluir vaga");
		btn_deleteVaga.addActionListener(e -> excluirVagaSelecionada());

		JButton btn_refreshVagas = UiStyle.secondaryButton("Atualizar");
		btn_refreshVagas.addActionListener(e -> carregarVagas());

		dataCard.add(UiStyle.createActions(btn_newVaga, btn_editVaga, btn_deleteVaga, btn_refreshVagas));
		return dataCard;
	}

	private JPanel criarPainelVeiculos() {
		JPanel vehicleCard = UiStyle.createCard();

		JLabel title = UiStyle.createSectionTitle("Meus veículos");
		title.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		vehicleCard.add(title);
		vehicleCard.add(Box.createVerticalStrut(4));

		JLabel subtitle = UiStyle.createBodyLabel(
				"Cadastre seus veículos, consulte dados por placa e localize em qual vaga estão estacionados.");
		subtitle.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		vehicleCard.add(subtitle);
		vehicleCard.add(Box.createVerticalStrut(14));

		// Barra de consulta por placa (GetVehicle / FindVehicle)
		JPanel searchRow = new JPanel(new BorderLayout(10, 0));
		searchRow.setOpaque(false);
		searchRow.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

		tf_searchVehiclePlate = new JTextField();
		UiStyle.styleField(tf_searchVehiclePlate);
		tf_searchVehiclePlate.setToolTipText("Digite a placa do veículo (ex: ABC1234)");

		JPanel searchButtons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
		searchButtons.setOpaque(false);

		JButton btn_getVehicle = UiStyle.secondaryButton("Consultar placa");
		btn_getVehicle.setPreferredSize(new Dimension(150, 42));
		btn_getVehicle.addActionListener(e -> consultarVeiculoPorPlaca(tf_searchVehiclePlate.getText()));

		JButton btn_locateVehicle = UiStyle.secondaryButton("Localizar na garagem");
		btn_locateVehicle.setPreferredSize(new Dimension(180, 42));
		btn_locateVehicle.addActionListener(e -> localizarVeiculoPorPlaca(tf_searchVehiclePlate.getText()));

		searchButtons.add(btn_getVehicle);
		searchButtons.add(btn_locateVehicle);

		searchRow.add(tf_searchVehiclePlate, BorderLayout.CENTER);
		searchRow.add(searchButtons, BorderLayout.EAST);
		vehicleCard.add(searchRow);
		vehicleCard.add(Box.createVerticalStrut(14));

		// Tabela de veículos do usuário
		vehiclesTableModel = new DefaultTableModel(
				new Object[] { "Placa", "Marca", "Modelo", "Tipo de Motorização" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableVehicles = new JTable(vehiclesTableModel);
		tableVehicles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		UiStyle.styleTable(tableVehicles);

		JScrollPane scrollPane = new JScrollPane(tableVehicles);
		scrollPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		scrollPane.setBorder(BorderFactory.createLineBorder(UiStyle.BORDER));
		scrollPane.getViewport().setBackground(UiStyle.SURFACE);
		scrollPane.setPreferredSize(new Dimension(600, 340));
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		vehicleCard.add(scrollPane);
		vehicleCard.add(Box.createVerticalStrut(10));

		lbl_statusVeiculos = UiStyle.createBodyLabel("Pronto.");
		lbl_statusVeiculos.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		vehicleCard.add(lbl_statusVeiculos);
		vehicleCard.add(Box.createVerticalStrut(12));

		JButton btn_createVehicle = UiStyle.primaryButton("Cadastrar veículo");
		btn_createVehicle.setPreferredSize(new Dimension(165, 44));
		btn_createVehicle.addActionListener(e -> abrirNovoVeiculo());

		JButton btn_editVehicle = UiStyle.secondaryButton("Editar veículo");
		btn_editVehicle.addActionListener(e -> abrirEdicaoVeiculo());

		JButton btn_findSelected = UiStyle.secondaryButton("Onde está estacionado?");
		btn_findSelected.setPreferredSize(new Dimension(195, 44));
		btn_findSelected.addActionListener(e -> {
			int row = tableVehicles.getSelectedRow();
			if (row >= 0 && row < listaVeiculos.size()) {
				localizarVeiculoPorPlaca(listaVeiculos.get(row).getLicensePlate());
			} else {
				String placa = JOptionPane.showInputDialog(this, "Informe a placa do veículo:",
						"Localizar veículo", JOptionPane.QUESTION_MESSAGE);
				if (placa != null && !placa.trim().isEmpty()) {
					localizarVeiculoPorPlaca(placa);
				}
			}
		});

		JButton btn_deleteVehicle = UiStyle.dangerButton("Remover veículo");
		btn_deleteVehicle.setPreferredSize(new Dimension(155, 44));
		btn_deleteVehicle.addActionListener(e -> excluirVeiculoSelecionado());

		JButton btn_refreshVehicles = UiStyle.secondaryButton("Atualizar");
		btn_refreshVehicles.addActionListener(e -> carregarVeiculos());

		vehicleCard.add(UiStyle.createActions(
				btn_createVehicle, btn_editVehicle, btn_findSelected, btn_deleteVehicle, btn_refreshVehicles));
		return vehicleCard;
	}

	private JPanel criarPainelAdmin() {
		JPanel adminCard = UiStyle.createCard();

		JLabel title = UiStyle.createSectionTitle("Administração de usuários e veículos");
		title.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		adminCard.add(title);
		adminCard.add(Box.createVerticalStrut(4));

		JLabel subtitle = UiStyle.createBodyLabel(
				"Consulte usuários, cadastre novas contas com perfil user/admin, altere nomes ou remova registros.");
		subtitle.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		adminCard.add(subtitle);
		adminCard.add(Box.createVerticalStrut(16));

		// Busca de usuário por username
		JPanel userSearchRow = new JPanel(new BorderLayout(10, 0));
		userSearchRow.setOpaque(false);
		userSearchRow.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		userSearchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

		tf_adminUsername = new JTextField();
		UiStyle.styleField(tf_adminUsername);
		tf_adminUsername.setToolTipText("Digite o username que deseja consultar");

		JButton btn_searchUser = UiStyle.primaryButton("Buscar usuário");
		btn_searchUser.setPreferredSize(new Dimension(160, 42));
		btn_searchUser.addActionListener(e -> consultarUsuarioAdmin(tf_adminUsername.getText()));

		userSearchRow.add(tf_adminUsername, BorderLayout.CENTER);
		userSearchRow.add(btn_searchUser, BorderLayout.EAST);
		adminCard.add(userSearchRow);
		adminCard.add(Box.createVerticalStrut(14));

		// Card de detalhes do usuário consultado
		JPanel userInfoBox = new JPanel(new GridLayout(3, 1, 0, 8));
		userInfoBox.setBackground(UiStyle.BACKGROUND);
		userInfoBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		userInfoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
		userInfoBox.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UiStyle.BORDER),
				BorderFactory.createEmptyBorder(12, 14, 12, 14)));

		lbl_adminUserNameVal = new JLabel("Nome completo: -");
		lbl_adminUserNameVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lbl_adminUserNameVal.setForeground(UiStyle.TEXT);

		lbl_adminUserUsernameVal = new JLabel("Nome de usuário: -");
		lbl_adminUserUsernameVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lbl_adminUserUsernameVal.setForeground(UiStyle.TEXT);

		lbl_adminUserRoleVal = new JLabel("Perfil (Role): -");
		lbl_adminUserRoleVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lbl_adminUserRoleVal.setForeground(UiStyle.TEXT);

		userInfoBox.add(lbl_adminUserNameVal);
		userInfoBox.add(lbl_adminUserUsernameVal);
		userInfoBox.add(lbl_adminUserRoleVal);
		adminCard.add(userInfoBox);
		adminCard.add(Box.createVerticalStrut(10));

		lbl_statusAdmin = UiStyle.createBodyLabel("Informe um username acima para consultar ou gerenciar um usuário.");
		lbl_statusAdmin.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		adminCard.add(lbl_statusAdmin);
		adminCard.add(Box.createVerticalStrut(14));

		JButton btn_registerAdmin = UiStyle.primaryButton("Novo usuário / Admin");
		btn_registerAdmin.setPreferredSize(new Dimension(185, 44));
		btn_registerAdmin.addActionListener(e -> {
			AdminUserWindow win = new AdminUserWindow(this);
			win.setVisible(true);
		});

		JButton btn_updateNameAdmin = UiStyle.secondaryButton("Alterar nome do usuário");
		btn_updateNameAdmin.setPreferredSize(new Dimension(195, 44));
		btn_updateNameAdmin.addActionListener(e -> alterarNomeUsuarioAdmin());

		JButton btn_deleteUserAdmin = UiStyle.dangerButton("Excluir usuário");
		btn_deleteUserAdmin.setPreferredSize(new Dimension(150, 44));
		btn_deleteUserAdmin.addActionListener(e -> excluirUsuarioAdmin());

		adminCard.add(UiStyle.createActions(btn_registerAdmin, btn_updateNameAdmin, btn_deleteUserAdmin));
		adminCard.add(Box.createVerticalStrut(26));

		// Seção de gerenciamento rápido de veículos por placa (GetVehicle / FindVehicle / DeleteVehicle)
		JLabel vehicleAdminTitle = UiStyle.createSectionTitle("Consulta e remoção de veículos por placa");
		vehicleAdminTitle.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		adminCard.add(vehicleAdminTitle);
		adminCard.add(Box.createVerticalStrut(6));

		JLabel vehicleAdminSub = UiStyle.createBodyLabel(
				"Consulte os detalhes de qualquer veículo pela placa, localize sua vaga ou realize sua remoção.");
		vehicleAdminSub.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		adminCard.add(vehicleAdminSub);
		adminCard.add(Box.createVerticalStrut(12));

		tf_adminPlate = new JTextField();
		adminCard.add(UiStyle.createField("Placa do veículo (ex: ABC1234)", tf_adminPlate));
		adminCard.add(Box.createVerticalStrut(12));

		JButton btn_adminGetVehicle = UiStyle.secondaryButton("Consultar veículo");
		btn_adminGetVehicle.setPreferredSize(new Dimension(160, 44));
		btn_adminGetVehicle.addActionListener(e -> consultarVeiculoPorPlaca(tf_adminPlate.getText()));

		JButton btn_adminFindVehicle = UiStyle.secondaryButton("Localizar vaga");
		btn_adminFindVehicle.setPreferredSize(new Dimension(150, 44));
		btn_adminFindVehicle.addActionListener(e -> localizarVeiculoPorPlaca(tf_adminPlate.getText()));

		JButton btn_adminDeleteVehicle = UiStyle.dangerButton("Remover veículo");
		btn_adminDeleteVehicle.setPreferredSize(new Dimension(160, 44));
		btn_adminDeleteVehicle.addActionListener(e -> {
			String p = tf_adminPlate.getText().trim();
			if (p.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Informe a placa do veículo que deseja remover.",
						"Campo obrigatório", JOptionPane.WARNING_MESSAGE);
				return;
			}
			excluirVeiculoPorPlaca(p);
		});

		adminCard.add(UiStyle.createActions(btn_adminGetVehicle, btn_adminFindVehicle, btn_adminDeleteVehicle));
		adminCard.add(Box.createVerticalGlue());
		return adminCard;
	}
}
