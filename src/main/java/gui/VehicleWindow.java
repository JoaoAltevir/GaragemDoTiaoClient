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

import entities.Vehicle;
import service.VehicleService;

public class VehicleWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String MANTER_ATUAL = "(Manter atual)";

	private JPanel contentPane;
	private MenuWindow menu;
	private VehicleService vehicleService;
	private Vehicle vehicleAtual;
	private boolean modoEdicao;

	private JTextField tf_licensePlate;
	private JTextField tf_brand;
	private JTextField tf_model;
	private JCheckBox cb_clearModel;
	private JComboBox<String> cb_type;

	public VehicleWindow(MenuWindow menu) {
		this(menu, null);
	}

	public VehicleWindow(MenuWindow menu, Vehicle vehicle) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fecharJanela();
			}
		});

		this.menu = menu;
		this.vehicleService = new VehicleService();
		this.vehicleAtual = vehicle;
		this.modoEdicao = (vehicle != null);

		initComponents();
	}

	public void fecharJanela() {
		this.dispose();
	}

	public void salvar() {
		String licensePlate = tf_licensePlate.getText().trim();
		String brand = tf_brand.getText().trim();
		String model = tf_model.getText().trim();
		String typeSelection = (String) cb_type.getSelectedItem();

		if (licensePlate.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe a placa do veículo (7 caracteres).", "Campo obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!modoEdicao) {
			if (brand.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Informe a marca do veículo.", "Campo obrigatório",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			String modelParam = model.isEmpty() ? null : model;
			JsonObject res = vehicleService.createVehicle(brand, modelParam, typeSelection, licensePlate);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação realizada.");

			if (status == 201 || status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Veículo cadastrado", JOptionPane.INFORMATION_MESSAGE);
				if (menu != null) {
					menu.carregarVeiculos();
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
			String brandParam = brand.isEmpty() ? null : brand;
			boolean clearModel = cb_clearModel != null && cb_clearModel.isSelected();
			String modelParam = model.isEmpty() ? null : model;
			String typeParam = MANTER_ATUAL.equals(typeSelection) ? null : typeSelection;

			JsonObject res = vehicleService.updateVehicle(licensePlate, brandParam, modelParam, clearModel, typeParam);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação realizada.");

			if (status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Veículo atualizado", JOptionPane.INFORMATION_MESSAGE);
				if (menu != null) {
					menu.carregarVeiculos();
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
		String titulo = modoEdicao ? "Editar veículo" : "Cadastrar veículo";
		String subtitulo = modoEdicao
				? "Altere marca, modelo ou tipo (campos em branco preservam o valor atual)."
				: "Informe os dados do veículo para vinculá-lo à sua conta.";

		UiStyle.configureFrame(this, titulo, 540, modoEdicao ? 610 : 560);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader(titulo, subtitulo), BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();

		tf_licensePlate = new JTextField();
		tf_licensePlate.setColumns(10);
		if (modoEdicao && vehicleAtual != null && vehicleAtual.getLicensePlate() != null
				&& !vehicleAtual.getLicensePlate().isEmpty()) {
			tf_licensePlate.setText(vehicleAtual.getLicensePlate());
			tf_licensePlate.setEditable(false);
		}
		card.add(UiStyle.createField("Placa do veículo (7 caracteres, ex: ABC1234)", tf_licensePlate));
		card.add(Box.createVerticalStrut(14));

		tf_brand = new JTextField();
		tf_brand.setColumns(10);
		if (modoEdicao && vehicleAtual != null && vehicleAtual.getBrand() != null) {
			tf_brand.setText(vehicleAtual.getBrand());
		}
		card.add(UiStyle.createField(modoEdicao ? "Marca (opcional)" : "Marca (1 a 20 caracteres)", tf_brand));
		card.add(Box.createVerticalStrut(14));

		tf_model = new JTextField();
		tf_model.setColumns(10);
		if (modoEdicao && vehicleAtual != null && vehicleAtual.getModel() != null) {
			tf_model.setText(vehicleAtual.getModel());
		}
		card.add(UiStyle.createField("Modelo (opcional, máx. 20 caracteres)", tf_model));

		if (modoEdicao) {
			card.add(Box.createVerticalStrut(6));
			cb_clearModel = UiStyle.createCheckBox("Limpar modelo (enviar model: null)");
			cb_clearModel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			cb_clearModel.addActionListener(e -> {
				boolean clear = cb_clearModel.isSelected();
				tf_model.setEnabled(!clear);
				if (clear) {
					tf_model.setText("");
				}
			});
			card.add(cb_clearModel);
		}
		card.add(Box.createVerticalStrut(14));

		if (!modoEdicao) {
			cb_type = new JComboBox<>(new String[] { "combustao", "eletrico" });
		} else {
			cb_type = new JComboBox<>(new String[] { MANTER_ATUAL, "combustao", "eletrico" });
		}
		String labelType = modoEdicao
				? "Tipo de motorização (atual: " + (vehicleAtual != null && vehicleAtual.getType() != null ? vehicleAtual.getType() : "-") + ")"
				: "Tipo de motorização";
		card.add(UiStyle.createField(labelType, cb_type));
		card.add(Box.createVerticalStrut(18));

		JButton btn_save = UiStyle.primaryButton(modoEdicao ? "Salvar alterações" : "Cadastrar");
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
