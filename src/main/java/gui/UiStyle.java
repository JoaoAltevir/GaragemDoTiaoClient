package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

final class UiStyle {

	static final Color BACKGROUND = new Color(244, 246, 248);
	static final Color SURFACE = Color.WHITE;
	static final Color TEXT = new Color(30, 41, 59);
	static final Color MUTED = new Color(100, 116, 139);
	static final Color ACCENT = new Color(194, 91, 24);
	static final Color ACCENT_HOVER = new Color(165, 73, 15);
	static final Color BORDER = new Color(218, 224, 230);
	static final Color DANGER = new Color(185, 49, 49);
	static final Color SUCCESS = new Color(22, 128, 60);
	static final Color WARNING = new Color(180, 110, 16);

	private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);

	private UiStyle() {}

	static void configureFrame(JFrame frame, String title, int width, int height) {
		frame.setTitle(title);
		frame.setSize(width, height);
		frame.setMinimumSize(new Dimension(Math.min(width, 430), Math.min(height, 360)));
		frame.setLocationRelativeTo(null);
	}

	static JPanel createPage() {
		JPanel page = new JPanel(new java.awt.BorderLayout(0, 20));
		page.setBackground(BACKGROUND);
		page.setBorder(new EmptyBorder(28, 34, 30, 34));
		return page;
	}

	static JPanel createHeader(String title, String subtitle) {
		JPanel header = new JPanel();
		header.setLayout(new javax.swing.BoxLayout(header, javax.swing.BoxLayout.Y_AXIS));
		header.setOpaque(false);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
		titleLabel.setForeground(TEXT);
		titleLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		header.add(titleLabel);

		JLabel subtitleLabel = new JLabel(subtitle);
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subtitleLabel.setForeground(MUTED);
		subtitleLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
		subtitleLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		header.add(subtitleLabel);
		return header;
	}

	static JPanel createCard() {
		JPanel card = new JPanel();
		card.setLayout(new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS));
		card.setBackground(SURFACE);
		card.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(BORDER, 1, true),
				new EmptyBorder(24, 26, 24, 26)));
		return card;
	}

	static JPanel createField(String label, JComponent input) {
		JPanel group = new JPanel();
		group.setLayout(new javax.swing.BoxLayout(group, javax.swing.BoxLayout.Y_AXIS));
		group.setOpaque(false);
		group.setAlignmentX(JComponent.LEFT_ALIGNMENT);

		JLabel fieldLabel = new JLabel(label);
		fieldLabel.setFont(LABEL_FONT);
		fieldLabel.setForeground(TEXT);
		fieldLabel.setBorder(new EmptyBorder(0, 0, 7, 0));
		fieldLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		group.add(fieldLabel);

		styleField(input);
		group.add(input);
		group.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
		return group;
	}

	static void styleField(JComponent field) {
		field.setFont(BODY_FONT);
		field.setForeground(TEXT);
		field.setBackground(SURFACE);
		if (field instanceof javax.swing.JComboBox) {
			field.setBorder(new LineBorder(BORDER, 1, true));
		} else {
			field.setBorder(BorderFactory.createCompoundBorder(
					new LineBorder(BORDER, 1, true),
					new EmptyBorder(8, 11, 8, 11)));
		}
		field.setPreferredSize(new Dimension(280, 42));
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent event) {
				if (field instanceof javax.swing.JComboBox) {
					field.setBorder(new LineBorder(ACCENT, 1, true));
				} else {
					field.setBorder(BorderFactory.createCompoundBorder(
							new LineBorder(ACCENT, 1, true),
							new EmptyBorder(8, 11, 8, 11)));
				}
			}

			@Override
			public void focusLost(FocusEvent event) {
				if (field instanceof javax.swing.JComboBox) {
					field.setBorder(new LineBorder(BORDER, 1, true));
				} else {
					field.setBorder(BorderFactory.createCompoundBorder(
							new LineBorder(BORDER, 1, true),
							new EmptyBorder(8, 11, 8, 11)));
				}
			}
		});
	}

	static JButton primaryButton(String text) {
		return styleButton(new JButton(text), ACCENT, Color.WHITE, ACCENT_HOVER);
	}

	static JButton secondaryButton(String text) {
		return styleButton(new JButton(text), new Color(237, 240, 243), TEXT, new Color(224, 229, 234));
	}

	static JButton dangerButton(String text) {
		return styleButton(new JButton(text), DANGER, Color.WHITE, new Color(151, 36, 36));
	}

	private static JButton styleButton(JButton button, Color background, Color foreground, Color hover) {
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setForeground(foreground);
		button.setBackground(background);
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(11, 18, 11, 18));
		button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
		button.setPreferredSize(new Dimension(140, 44));
		button.setMinimumSize(new Dimension(108, 42));
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.getModel().addChangeListener(event -> {
			if (button.getModel().isRollover()) {
				button.setBackground(hover);
			} else {
				button.setBackground(background);
			}
		});
		return button;
	}

	static JPanel createActions(JButton... buttons) {
		JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		actions.setOpaque(false);
		actions.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		for (JButton button : buttons) {
			actions.add(button);
		}
		return actions;
	}

	static JCheckBox createCheckBox(String text) {
		JCheckBox checkBox = new JCheckBox(text);
		checkBox.setFont(BODY_FONT);
		checkBox.setForeground(MUTED);
		checkBox.setOpaque(false);
		checkBox.setFocusPainted(false);
		return checkBox;
	}

	static JLabel createSectionTitle(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.BOLD, 17));
		label.setForeground(TEXT);
		return label;
	}

	static JLabel createBodyLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(BODY_FONT);
		label.setForeground(MUTED);
		return label;
	}

	static JPanel createStatCard(String title, JLabel valueLabel) {
		JPanel stat = new JPanel();
		stat.setLayout(new javax.swing.BoxLayout(stat, javax.swing.BoxLayout.Y_AXIS));
		stat.setBackground(BACKGROUND);
		stat.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(BORDER, 1, true),
				new EmptyBorder(10, 14, 10, 14)));

		JLabel titleLbl = new JLabel(title);
		titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		titleLbl.setForeground(MUTED);
		titleLbl.setAlignmentX(JComponent.LEFT_ALIGNMENT);

		valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
		valueLabel.setForeground(TEXT);
		valueLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);

		stat.add(titleLbl);
		stat.add(javax.swing.Box.createVerticalStrut(4));
		stat.add(valueLabel);
		return stat;
	}

	static void styleTable(javax.swing.JTable table) {
		table.setRowHeight(32);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.setForeground(TEXT);
		table.setBackground(SURFACE);
		table.setGridColor(BACKGROUND);
		table.setSelectionBackground(new Color(254, 237, 224));
		table.setSelectionForeground(TEXT);
		table.setShowVerticalLines(false);
		table.setFillsViewportHeight(true);
		if (table.getTableHeader() != null) {
			table.getTableHeader().setFont(LABEL_FONT);
			table.getTableHeader().setBackground(BACKGROUND);
			table.getTableHeader().setForeground(TEXT);
			table.getTableHeader().setReorderingAllowed(false);
		}
	}

	static String extractMessage(com.google.gson.JsonObject res, String fallback) {
		if (res != null && res.has("message") && !res.get("message").isJsonNull()) {
			return res.get("message").getAsString();
		}
		return fallback;
	}

	static int extractStatusCode(com.google.gson.JsonObject res) {
		if (res != null && res.has("statusCode") && !res.get("statusCode").isJsonNull()) {
			return res.get("statusCode").getAsInt();
		}
		return 500;
	}
}
