package br.com.fiap.cp4.app.view.components;

import br.com.fiap.cp4.app.controller.GameController;
import br.com.fiap.cp4.core.model.entities.Game;
import br.com.fiap.cp4.core.model.enums.GameGenre;
import br.com.fiap.cp4.core.model.enums.GamePlatform;
import br.com.fiap.cp4.core.model.enums.GameStatus;
import br.com.fiap.cp4.app.view.dialogs.DeleteConfirmDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.Year;

public class GameCard extends JPanel {
    private Game game;
    private GameController controller;

    private static final Color CARD_COLOR = new Color(76, 86, 106);
    private static final Color CARD_HOVER_COLOR = new Color(88, 101, 242);
    private static final Color TEXT_COLOR = new Color(236, 239, 244);
    private static final Color DELETE_COLOR = new Color(255, 107, 107);
    private static final Dimension CARD_SIZE = new Dimension(300, 120);

    private static Game createDefaultGame() {
        return new Game(
                "Game Title",
                GameGenre.ACTION,
                GamePlatform.PC,
                Year.now().getValue(),
                GameStatus.WISHLIST,
                null
        );
    }

    public GameCard(Game game, GameController controller) {
        this.game = game != null ? game : createDefaultGame();
        this.controller = controller;

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(12, 8));
        setBackground(CARD_COLOR);
        setBorder(new EmptyBorder(16, 16, 16, 16));
        setPreferredSize(CARD_SIZE);
        setMaximumSize(CARD_SIZE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Título
        JLabel titleLabel = new JLabel(game.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);

        // Informações do jogo
        String statusText = "Status: " + (game.getStatus() != null ? game.getStatus().getDisplayName() : "N/A");
        JLabel statusLabel = new JLabel(statusText);
        statusLabel.setForeground(getStatusColor(game.getStatus()));

        String platformText = "Plataforma: " + (game.getPlatform() != null ? game.getPlatform().getDisplayName() : "N/A");
        JLabel platformLabel = new JLabel(platformText);
        platformLabel.setForeground(TEXT_COLOR);

        String genreText = "Gênero: " + (game.getGenre() != null ? game.getGenre().getDisplayName() : "N/A");
        JLabel genreLabel = new JLabel(genreText);
        genreLabel.setForeground(TEXT_COLOR);

        // Panel com as informações
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_COLOR);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(statusLabel);
        infoPanel.add(platformLabel);
        infoPanel.add(genreLabel);

        // Botão de deletar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(CARD_COLOR);

        JButton deleteButton = new JButton("×");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setForeground(DELETE_COLOR);
        deleteButton.setBackground(CARD_COLOR);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setToolTipText("Remover jogo");

        deleteButton.addActionListener(e -> {
            if (controller != null) {
                DeleteConfirmDialog.showDialog(
                        SwingUtilities.getWindowAncestor(this),
                        game,
                        controller
                );
            }
        });

        buttonPanel.add(deleteButton);

        // Layout principal
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

        // Mouse listeners para hover e clique
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(CARD_HOVER_COLOR);
                infoPanel.setBackground(CARD_HOVER_COLOR);
                buttonPanel.setBackground(CARD_HOVER_COLOR);
                deleteButton.setBackground(CARD_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(CARD_COLOR);
                infoPanel.setBackground(CARD_COLOR);
                buttonPanel.setBackground(CARD_COLOR);
                deleteButton.setBackground(CARD_COLOR);
            }

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (controller != null) {
                    controller.editGame(SwingUtilities.getWindowAncestor(GameCard.this), game);
                }
            }
        });

        if (controller != null) {
            setupContextMenu();
        }
    }

    private Color getStatusColor(GameStatus status) {
        if (status == null) return TEXT_COLOR;

        switch (status) {
            case PLAYING: return new Color(163, 190, 140);
            case COMPLETED: return new Color(163, 190, 140);
            case ON_HOLD: return new Color(235, 203, 139);
            case DROPPED: return new Color(191, 97, 106);
            case WISHLIST: return new Color(180, 142, 173);
            default: return TEXT_COLOR;
        }
    }

    private void setupContextMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Editar Jogo");
        editItem.addActionListener(e -> {
            if (controller != null) {
                controller.editGame(SwingUtilities.getWindowAncestor(this), game);
            }
        });
        popupMenu.add(editItem);

        JMenuItem deleteItem = new JMenuItem("Excluir Jogo");
        deleteItem.setForeground(DELETE_COLOR);
        deleteItem.addActionListener(e -> {
            DeleteConfirmDialog.showDialog(
                    SwingUtilities.getWindowAncestor(this),
                    game,
                    controller
            );
        });
        popupMenu.add(deleteItem);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
}