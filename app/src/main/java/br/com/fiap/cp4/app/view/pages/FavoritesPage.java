package br.com.fiap.cp4.app.view.pages;

import br.com.fiap.cp4.app.controller.GameController;
import br.com.fiap.cp4.app.view.components.GameList;
import br.com.fiap.cp4.core.model.entities.Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FavoritesPage {
    private JPanel rootPanel;
    private GameList gameList;
    private GameController gameController;

    public FavoritesPage() {
        try {
            this.gameController = new GameController();
            initializeUI();
            loadFavoriteGamesAsync();
        } catch (Exception e) {
            initializeErrorUI(e.getMessage());
        }
    }

    private void initializeUI() {
        rootPanel = new JPanel(new BorderLayout());
        gameList = new GameList(gameController);
        rootPanel.add(gameList.getRootPanel(), BorderLayout.CENTER);
        gameList.showLoading();
    }

    private void initializeErrorUI(String error) {
        rootPanel = new JPanel(new BorderLayout());

        JLabel errorLabel = new JLabel("<html><div style='text-align: center; color: red;'>" +
                "Error: " + error + "</div>", JLabel.CENTER);

        JButton retryButton = new JButton("Try Again");
        retryButton.addActionListener(e -> {
            try {
                this.gameController = new GameController();
                this.gameController.setOnGameListChanged(this::refreshFavorites);
                initializeUI();
                loadFavoriteGamesAsync();
            } catch (Exception ex) {
                initializeErrorUI(ex.getMessage());
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(retryButton);

        rootPanel.add(errorLabel, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadFavoriteGamesAsync() {
        SwingWorker<List<Game>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Game> doInBackground() {
                try {
                    return gameController.getFavoriteGames();
                } catch (Exception e) {
                    return List.of();
                }
            }

            @Override
            protected void done() {
                try {
                    List<Game> favoriteGames = get();
                    gameList.showGames(favoriteGames);
                    rootPanel.revalidate();
                    rootPanel.repaint();
                } catch (Exception e) {
                    gameList.showError("Falha ao carregar jogos favoritos: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    public void refreshFavorites() {
        if (gameList != null) {
            gameList.showLoading();
            loadFavoriteGamesAsync();
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}