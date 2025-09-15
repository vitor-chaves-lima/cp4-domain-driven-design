package br.com.fiap.cp4.app.view.components;

import br.com.fiap.cp4.app.controller.GameController;
import br.com.fiap.cp4.core.model.entities.Game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class GameList {
    private final JPanel rootPanel;
    private final JPanel contentPanel;
    private final JPanel gamesPanel;
    private final JScrollPane scrollPane;
    private final CardLayout cardLayout;
    private final JLabel statusLabel;
    private final JButton addButton;
    private GameController controller;

    private static final Color BACKGROUND_COLOR = new Color(67, 76, 94);
    private static final Color TEXT_COLOR = new Color(236, 239, 244);
    private static final Color ACCENT_COLOR = new Color(88, 101, 242);

    public GameList(GameController controller) {
        this.controller = controller;

        this.rootPanel = new JPanel(new BorderLayout());
        this.rootPanel.setBackground(BACKGROUND_COLOR);
        this.rootPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.cardLayout = new CardLayout(10, 10);
        this.contentPanel = new JPanel(cardLayout);
        this.contentPanel.setBackground(BACKGROUND_COLOR);

        this.gamesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        this.gamesPanel.setBackground(BACKGROUND_COLOR);
        this.gamesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.scrollPane = new JScrollPane(gamesPanel);
        this.scrollPane.setBackground(BACKGROUND_COLOR);
        this.scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.scrollPane.setBorder(null);

        this.statusLabel = new JLabel("Carregando jogos...");
        this.statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.statusLabel.setForeground(TEXT_COLOR);

        this.addButton = new JButton("Adicionar Jogo");
        this.addButton.setBackground(ACCENT_COLOR);
        this.addButton.setForeground(Color.WHITE);
        this.addButton.setFont(new Font("Arial", Font.BOLD, 14));
        this.addButton.setFocusPainted(false);
        this.addButton.setBorderPainted(false);
        this.addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addButton.addActionListener(e -> {
            if (controller != null) {
                Window window = SwingUtilities.getWindowAncestor(rootPanel);
                controller.createNewGame(window);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(addButton);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.setBackground(BACKGROUND_COLOR);

        contentPanel.add(statusPanel, "status");
        contentPanel.add(scrollPane, "games");
        contentPanel.add(emptyPanel, "empty");

        rootPanel.add(buttonPanel, BorderLayout.NORTH);
        rootPanel.add(contentPanel, BorderLayout.CENTER);

        setupAutoRefresh();

        showLoading();
        loadGames();
    }

    private void setupAutoRefresh() {
        if (controller != null) {
            controller.setOnGameListChanged(() -> {
                SwingUtilities.invokeLater(() -> loadGames());
            });
        }
    }

    public void loadGames() {
        if (controller != null) {
            showLoading();
            SwingUtilities.invokeLater(() -> {
                try {
                    List<Game> games = controller.getAllGames();
                    showGames(games);
                } catch (Exception e) {
                    showError("Erro ao carregar jogos: " + e.getMessage());
                }
            });
        }
    }

    public void showLoading() {
        if (statusLabel != null) {
            statusLabel.setText("Carregando jogos...");
            statusLabel.setForeground(TEXT_COLOR);
        }
        if (cardLayout != null) {
            cardLayout.show(contentPanel, "status");
        }
    }

    public void showError(String message) {
        if (statusLabel != null) {
            statusLabel.setText("Erro: " + message);
            statusLabel.setForeground(new Color(255, 107, 107));
            if (cardLayout != null) {
                cardLayout.show(contentPanel, "status");
            }
        }
    }

    private void showEmptyState(String message) {
        Component[] components = contentPanel.getComponents();
        JPanel emptyPanel = null;

        for (Component comp : components) {
            if (comp instanceof JPanel && ((JPanel) comp).getLayout() instanceof GridBagLayout) {
                emptyPanel = (JPanel) comp;
                break;
            }
        }

        if (emptyPanel != null) {
            emptyPanel.removeAll();

            JLabel emptyLabel = new JLabel(
                    "<html><div style='text-align: center;'>" +
                            "<p style='font-size:18px; color:#eceff4; margin-bottom:10px;'>" + message + "</p>" +
                            "<p style='font-size:14px; color:#d8dee9;'>Clique em 'Adicionar Jogo' para começar</p>" +
                            "</div></html>",
                    SwingConstants.CENTER
            );

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;

            emptyPanel.add(emptyLabel, gbc);
            emptyPanel.revalidate();
            emptyPanel.repaint();
        }

        cardLayout.show(contentPanel, "empty");
    }

    public void showGames(List<Game> games) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> showGames(games));
            return;
        }

        gamesPanel.removeAll();

        if (games == null || games.isEmpty()) {
            showEmptyState("Nenhum jogo encontrado");
            return;
        }

        java.util.Set<String> addedGames = new java.util.HashSet<>();

        for (Game game : games) {
            if (game != null && game.getTitle() != null) {
                String gameKey = game.getTitle().trim().toLowerCase();

                if (!addedGames.contains(gameKey)) {
                    GameCard gameCard = new GameCard(game, controller);
                    gamesPanel.add(gameCard);
                    addedGames.add(gameKey);
                }
            }
        }

        cardLayout.show(contentPanel, "games");
        gamesPanel.revalidate();
        gamesPanel.repaint();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}