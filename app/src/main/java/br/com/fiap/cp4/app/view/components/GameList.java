package br.com.fiap.cp4.app.view.components;

import br.com.fiap.cp4.core.model.entities.Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameList {
    private JPanel rootPanel;
    private JPanel contentPanel;
    private JLabel statusLabel;
    private JScrollPane gamesContainer;
    private JPanel gamesPanel;

    private final CardLayout cardLayout;

    public GameList() {
        if (contentPanel.getLayout() instanceof CardLayout) {
            cardLayout = (CardLayout) contentPanel.getLayout();
        } else {
            throw new IllegalStateException("contentPanel deve ter CardLayout configurado");
        }
    }

    public void showLoading() {
        statusLabel.setText("Carregando...");
        cardLayout.show(contentPanel, "STATUS_CARD");
    }

    public void showGames(List<Game> games) {
        gamesPanel.removeAll();

        if (games != null && !games.isEmpty()) {
            for (Game game : games) {
                gamesPanel.add(new GameCard(game));
            }
            gamesContainer.revalidate();
            gamesContainer.repaint();
            cardLayout.show(contentPanel, "GAMES_CARD");
        } else {
            statusLabel.setText("Nenhum jogo encontrado");
            cardLayout.show(contentPanel, "STATUS_CARD");
        }
    }
    public void showError(String message) {
        statusLabel.setText("Erro: " + message);
        cardLayout.show(contentPanel, "STATUS_CARD");
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}