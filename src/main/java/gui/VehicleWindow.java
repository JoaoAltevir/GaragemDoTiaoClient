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

import entities.Vehicle;
import service.VehicleService;

public class VehicleWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private MenuWindow menu;
	private VehicleService vehicleService;
	private Vehicle vehicleAtual;
	private boolean modoEdicao;

	private JTextField tf_licensePlate;
	private JTextField tf_brand;
	private JTextField tf_model;
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
		String type = (String) cb_type.getSelectedItem();

		if (licensePlate.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe a placa do veículo.", "Campo obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!modoEdicao) {
			if (brand.isEmpty() || model.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Preencha a marca e o modelo do veículo.", "Campos obrigatórios",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			JsonObject res = vehicleService.createVehicle(brand, model, type, licensePlate);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação realizada.");

			if (status == 201 || status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Veículo cadastrado", JOptionPane.INFORMATION_MESSAGE);
				if (menu != null) {
					menu.carregarVeiculos();
				}
				fecharJanela();
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			String brandParam = brand.isEmpty() ? null : brand;
			String modelParam = model.isEmpty() ? null : model;

			JsonObject res = vehicleService.updateVehicle(licensePlate, brandParam, modelParam, type);
			int status = UiStyle.extractStatusCode(res);
			String msg = UiStyle.extractMessage(res, "Operação realizada.");

			if (status == 200) {
				JOptionPane.showMessageDialog(this, msg, "Veículo atualizado", JOptionPane.INFORMATION_MESSAGE);
				if (menu != null) {
					menu.carregarVeiculos();
				}
				fecharJanela();
			} else {
				JOptionPane.showMessageDialog(this, msg, "Erro (" + status + ")", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String titulo = modoEdicao ? "Editar veículo" : "Cadastrar veículo";
		String subtitulo = modoEdicao
				? "Atualize as informações de marca, modelo ou motorização."
				: "Informe os dados do veículo para vinculá-lo à sua conta.";

		UiStyle.configureFrame(this, titulo, 520, 560);
		contentPane = UiStyle.createPage();
		setContentPane(contentPane);

		contentPane.add(UiStyle.createHeader(titulo, subtitulo), BorderLayout.NORTH);
		JPanel card = UiStyle.createCard();

		tf_licensePlate = new JTextField();
		tf_licensePlate.setColumns(10);
		if (modoEdicao && vehicleAtual != null) {
			tf_licensePlate.setText(vehicleAtual.getLicensePlate() != null ? vehicleAtual.getLicensePlate() : "");
			tf_licensePlate.setEditable(false);
		}
		card.add(UiStyle.createField("Placa do veículo (ex: ABC1234)", tf_licensePlate));
		card.add(Box.createVerticalStrut(14));

		tf_brand = new JTextField();
		tf_brand.setColumns(10);
		if (modoEdicao && vehicleAtual != null && vehicleAtual.getBrand() != null) {
			tf_brand.setText(vehicleAtual.getBrand());
		}
		card.add(UiStyle.createField("Marca", tf_brand));
		card.add(Box.createVerticalStrut(14));

		tf_model = new JTextField();
		tf_model.setColumns(10);
		if (modoEdicao && vehicleAtual != null && vehicleAtual.getModel() != null) {
			tf_model.setText(vehicleAtual.getModel());
		}
		card.add(UiStyle.createField("Modelo", tf_model));
		card.add(Box.createVerticalStrut(14));

		cb_type = new JComboBox<>(new String[] { "combustao", "eletrico" });
		if (modoEdicao && vehicleAtual != null && vehicleAtual.getType() != null) {
			cb_type.setSelectedItem(vehicleAtual.getType());
		}
		card.add(UiStyle.createField("Tipo de motorização", cb_type));
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
