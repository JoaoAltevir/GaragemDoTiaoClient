package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.gson.JsonObject;

import entities.Vaga;
import service.VagaService;

public class VagaWindow extends JFrame {

	private static final long serialVersionUID = 1L;
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
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			String newSpotName = tf_newSpotName.getText().trim();
			if (newSpotName.isEmpty() || newSpotName.equals(spotName)) {
				newSpotName = null;
			}
			String statusVaga = (String) cb_status.getSelectedItem();
			String licensePlate = tf_licensePlate.getText().trim();
			if (licensePlate.isEmpty()) {
				licensePlate = null;
			}

			JsonObject res = vagaService.updateVaga(spotName, newSpotName, floor, statusVaga, licensePlate);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação realizada.");

			if (status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Vaga atualizada", JOptionPane.INFORMATION_MESSAGE);
				if (menu != null) {
					menu.carregarVagas();
				}
				fecharJanela();
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String titulo = modoEdicao ? "Atualizar vaga" : "Nova vaga";
		String subtitulo = modoEdicao
				? "Altere os dados, status ou veículo estacionado na vaga."
				: "Preencha os dados para criar uma nova vaga na garagem.";

		UiStyle.configureFrame(this, titulo, 520, modoEdicao ? 620 : 500);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader(titulo, subtitulo), BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();

		tf_spotName = new JTextField();
		tf_spotName.setColumns(10);
		if (modoEdicao && vagaAtual != null) {
			tf_spotName.setText(vagaAtual.getSpotName() != null ? vagaAtual.getSpotName() : "");
			tf_spotName.setEditable(false);
		}
		card.add(UiStyle.createField(modoEdicao ? "Vaga selecionada" : "Nome da vaga (ex: A1)", tf_spotName));
		card.add(Box.createVerticalStrut(14));

		if (modoEdicao) {
			tf_newSpotName = new JTextField();
			tf_newSpotName.setColumns(10);
			card.add(UiStyle.createField("Novo nome da vaga (opcional)", tf_newSpotName));
			card.add(Box.createVerticalStrut(14));
		}

		tf_floor = new JTextField();
		tf_floor.setColumns(10);
		if (modoEdicao && vagaAtual != null) {
			tf_floor.setText(String.valueOf(vagaAtual.getFloor()));
		}
		card.add(UiStyle.createField("Andar", tf_floor));
		card.add(Box.createVerticalStrut(14));

		if (!modoEdicao) {
			cb_type = new JComboBox<>(new String[] { "combustao", "eletrico" });
			card.add(UiStyle.createField("Tipo da vaga", cb_type));
			card.add(Box.createVerticalStrut(18));
		} else {
			cb_status = new JComboBox<>(new String[] { "disponivel", "ocupada", "fora_de_servico" });
			if (vagaAtual != null && vagaAtual.getStatus() != null) {
				cb_status.setSelectedItem(vagaAtual.getStatus());
			}
			card.add(UiStyle.createField("Status da vaga", cb_status));
			card.add(Box.createVerticalStrut(14));

			tf_licensePlate = new JTextField();
			tf_licensePlate.setColumns(10);
			if (vagaAtual != null && vagaAtual.getLicensePlate() != null) {
				tf_licensePlate.setText(vagaAtual.getLicensePlate());
			}
			card.add(UiStyle.createField("Placa do veículo (opcional)", tf_licensePlate));
			card.add(Box.createVerticalStrut(18));
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
