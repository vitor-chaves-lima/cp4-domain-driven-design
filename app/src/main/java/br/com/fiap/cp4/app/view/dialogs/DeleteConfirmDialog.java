package br.com.fiap.cp4.app.view.dialogs;

import br.com.fiap.cp4.app.controller.GameController;
import br.com.fiap.cp4.core.model.entities.Game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeleteConfirmDialog {

    private static final Color BACKGROUND_COLOR = new Color(67, 76, 94);
    private static final Color TEXT_COLOR = new Color(236, 239, 244);
    private static final Color SECONDARY_TEXT_COLOR = new Color(180, 188, 200);
    private static final Color DANGER_COLOR = new Color(220, 85, 95);
    private static final Color DANGER_HOVER_COLOR = new Color(200, 75, 85);
    private static final Color CANCEL_COLOR = new Color(94, 105, 120);
    private static final Color CANCEL_HOVER_COLOR = new Color(110, 120, 135);

    public static void showDialog(Window parent, Game game, GameController controller) {
        JDialog dialog = new JDialog(parent, "Confirmar Exclusão", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(35, 35, 30, 35));

        JLabel titleLabel = new JLabel("Excluir jogo");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String gameName = game.getTitle() != null ? game.getTitle() : "este jogo";
        JLabel messageLabel = new JLabel("Tem certeza que deseja excluir \"" + gameName + "\"?");
        messageLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_COLOR);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel warningLabel = new JLabel("Esta ação não pode ser desfeita.");
        warningLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        warningLabel.setForeground(SECONDARY_TEXT_COLOR);
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton cancelButton = createButton("Cancelar", CANCEL_COLOR, CANCEL_HOVER_COLOR, TEXT_COLOR);
        JButton confirmButton = createButton("Excluir", DANGER_COLOR, DANGER_HOVER_COLOR, Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        cancelButton.addActionListener(e -> dialog.dispose());

        confirmButton.addActionListener(e -> {
            if (controller != null) {
                confirmButton.setEnabled(false);
                confirmButton.setText("Excluindo...");

                SwingUtilities.invokeLater(() -> {
                    try {
                        controller.deleteGame(game);
                        dialog.dispose();

                    } catch (Exception ex) {
                        confirmButton.setEnabled(true);
                        confirmButton.setText("Excluir");

                        JOptionPane.showMessageDialog(
                                parent,
                                "Erro ao excluir o jogo: " + ex.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
            }
        });

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        mainPanel.add(warningLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(buttonPanel);

        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        dialog.getRootPane().registerKeyboardAction(
                e -> dialog.dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        dialog.getRootPane().registerKeyboardAction(
                e -> confirmButton.doClick(),
                KeyStroke.getKeyStroke("ENTER"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        dialog.getRootPane().setDefaultButton(cancelButton);

        dialog.setVisible(true);
    }

    private static JButton createButton(String text, Color bgColor, Color hoverColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Dialog", Font.PLAIN, 13));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 36));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor);
                }
            }
        });

        return button;
    }
}