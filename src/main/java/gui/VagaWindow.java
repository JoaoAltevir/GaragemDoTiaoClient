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
import javax.swing.JTextField;

import com.google.gson.JsonObject;

import entities.Vaga;
import service.VagaService;

public class VagaWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String MANTER_ATUAL = "(Manter atual)";

	private JPanel contentPane;
	private MenuWindow menu;
	private VagaService vagaService;
	private Vaga vagaAtual;
	private boolean modoEdicao;

	private JTextField tf_spotName;
	private JTextField tf_newSpotName;
	private JTextField tf_floor;
	private JComboBox<String> cb_type;
	private JComboBox<String> cb_status;
	private JTextField tf_licensePlate;
	private JCheckBox cb_clearPlate;

	public VagaWindow(MenuWindow menu) {
		this(menu, null);
	}

	public VagaWindow(MenuWindow menu, Vaga vaga) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fecharJanela();
			}
		});

		this.menu = menu;
		this.vagaService = new VagaService();
		this.vagaAtual = vaga;
		this.modoEdicao = (vaga != null);

		initComponents();
	}

	public void fecharJanela() {
		this.dispose();
	}

	public void salvar() {
		String spotName = tf_spotName.getText().trim();
		String floorStr = tf_floor.getText().trim();

		if (spotName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe o identificador da vaga.", "Campo obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Integer floor = null;
		if (!floorStr.isEmpty()) {
			try {
				floor = Integer.parseInt(floorStr);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "O andar deve ser um número inteiro válido.", "Andar inválido",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		} else if (!modoEdicao) {
			JOptionPane.showMessageDialog(this, "Informe o andar da vaga.", "Campo obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!modoEdicao) {
			String type = (String) cb_type.getSelectedItem();
			JsonObject res = vagaService.createVaga(spotName, floor, type);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação realizada.");

			if (status == 201 || status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Vaga criada", JOptionPane.INFORMATION_MESSAGE);
				if (menu != null) {
					menu.carregarVagas();
				}
				fecharJanela();
			} else if (UiStyle.isSessionExpired(res)) {
				JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
				fecharJanela();
				if (menu != null) {
					menu.voltarParaLogin();
				}
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			String newSpotName = tf_newSpotName.getText().trim();
			if (newSpotName.isEmpty()) {
				newSpotName = null;
			}

			String statusSelecionado = (String) cb_status.getSelectedItem();
			String statusVaga = MANTER_ATUAL.equals(statusSelecionado) ? null : statusSelecionado;

			boolean clearLicensePlate = cb_clearPlate != null && cb_clearPlate.isSelected();
			String licensePlate = tf_licensePlate.getText().trim();
			if (licensePlate.isEmpty()) {
				licensePlate = null;
				if ("disponivel".equalsIgnoreCase(statusVaga)
						&& vagaAtual != null
						&& "ocupada".equalsIgnoreCase(vagaAtual.getStatus())) {
					clearLicensePlate = true;
				}
			}

			JsonObject res = vagaService.updateVaga(
					spotName, newSpotName, floor, statusVaga, licensePlate, clearLicensePlate);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação realizada.");

			if (status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Vaga atualizada", JOptionPane.INFORMATION_MESSAGE);
				if (menu != null) {
					menu.carregarVagas();
				}
				fecharJanela();
			} else if (UiStyle.isSessionExpired(res)) {
				JOptionPane.showMessageDialog(this, msg, "Sessão encerrada (401)", JOptionPane.WARNING_MESSAGE);
				fecharJanela();
				if (menu != null) {
					menu.voltarParaLogin();
				}
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String titulo = modoEdicao ? "Atualizar vaga" : "Nova vaga";
		String subtitulo = modoEdicao
				? "Preencha apenas os campos que deseja alterar (campos em branco são mantidos)."
				: "Preencha os dados para criar uma nova vaga na garagem.";

		UiStyle.configureFrame(this, titulo, 540, modoEdicao ? 660 : 500);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader(titulo, subtitulo), BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();

		tf_spotName = new JTextField();
		tf_spotName.setColumns(10);
		if (modoEdicao && vagaAtual != null && vagaAtual.getSpotName() != null && !vagaAtual.getSpotName().isEmpty()) {
			tf_spotName.setText(vagaAtual.getSpotName());
			tf_spotName.setEditable(false);
		}
		card.add(UiStyle.createField(modoEdicao ? "Vaga selecionada" : "Nome da vaga (1 a 4 caracteres, ex: A1)", tf_spotName));
		card.add(Box.createVerticalStrut(12));

		if (modoEdicao) {
			tf_newSpotName = new JTextField();
			tf_newSpotName.setColumns(10);
			card.add(UiStyle.createField("Novo nome da vaga (opcional - Admin)", tf_newSpotName));
			card.add(Box.createVerticalStrut(12));
		}

		tf_floor = new JTextField();
		tf_floor.setColumns(10);
		String labelFloor = modoEdicao
				? "Novo andar (opcional - atual: " + (vagaAtual != null ? vagaAtual.getFloor() : "-") + ")"
				: "Andar";
		card.add(UiStyle.createField(labelFloor, tf_floor));
		card.add(Box.createVerticalStrut(12));

		if (!modoEdicao) {
			cb_type = new JComboBox<>(new String[] { "combustao", "eletrico" });
			card.add(UiStyle.createField("Tipo da vaga", cb_type));
			card.add(Box.createVerticalStrut(18));
		} else {
			cb_status = new JComboBox<>(new String[] { MANTER_ATUAL, "disponivel", "ocupada", "fora_de_servico" });
			if (vagaAtual != null && vagaAtual.getStatus() != null) {
				cb_status.setSelectedItem(vagaAtual.getStatus());
			}
			card.add(UiStyle.createField("Status da vaga", cb_status));
			card.add(Box.createVerticalStrut(12));

			tf_licensePlate = new JTextField();
			tf_licensePlate.setColumns(10);
			if (vagaAtual != null && vagaAtual.getLicensePlate() != null) {
				tf_licensePlate.setText(vagaAtual.getLicensePlate());
			}
			card.add(UiStyle.createField("Placa do veículo (7 caracteres, ex: ABC1234)", tf_licensePlate));
			card.add(Box.createVerticalStrut(6));

			cb_clearPlate = UiStyle.createCheckBox("Desvincular veículo / Liberar vaga (enviar licensePlate: null)");
			cb_clearPlate.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			cb_clearPlate.addActionListener(e -> {
				boolean liberar = cb_clearPlate.isSelected();
				tf_licensePlate.setEnabled(!liberar);
				if (liberar) {
					tf_licensePlate.setText("");
					cb_status.setSelectedItem("disponivel");
				}
			});
			card.add(cb_clearPlate);
			card.add(Box.createVerticalStrut(16));
		}

		JButton btn_save = UiStyle.primaryButton(modoEdicao ? "Salvar alterações" : "Criar vaga");
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvar();
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
