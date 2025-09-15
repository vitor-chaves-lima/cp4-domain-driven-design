package br.com.fiap.cp4.app.view.pages;

import br.com.fiap.cp4.app.controller.GameController;
import br.com.fiap.cp4.app.view.components.GameList;
import br.com.fiap.cp4.core.model.entities.Game;
import br.com.fiap.cp4.core.model.enums.GameGenre;
import br.com.fiap.cp4.core.model.enums.GamePlatform;
import br.com.fiap.cp4.core.model.enums.GameStatus;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class LibraryPage {
    private JPanel rootPanel;
    private GameList gameList;
    private GameController gameController;

    public LibraryPage() {
        try {
            this.gameController = new GameController();
            initializeUI();
            loadGamesAsync();
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
                this.gameController.setOnGameListChanged(this::refreshGames);
                initializeUI();
                loadGamesAsync();
            } catch (Exception ex) {
                initializeErrorUI(ex.getMessage());
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(retryButton);

        rootPanel.add(errorLabel, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadGamesAsync() {
        SwingWorker<List<Game>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Game> doInBackground() {
                try {
                    return gameController.getAllGames();
                } catch (Exception e) {
                    return List.of();
                }
            }

            @Override
            protected void done() {
                try {
                    List<Game> games = get();
                    gameList.showGames(games);
                    rootPanel.revalidate();
                    rootPanel.repaint();
                } catch (Exception e) {
                    gameList.showError("Falha ao carregar jogos: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    public void refreshGames() {
        if (gameList != null) {
            gameList.showLoading();
            loadGamesAsync();
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}