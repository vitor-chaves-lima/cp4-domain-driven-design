package br.com.fiap.cp4.app.view.pages;

import br.com.fiap.cp4.app.controller.GameController;
import br.com.fiap.cp4.app.view.components.GameList;
import br.com.fiap.cp4.core.model.entities.Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FinishedPage {
    private JPanel rootPanel;
    private GameList gameList;
    private GameController gameController;

    public FinishedPage() {
        try {
            this.gameController = new GameController();
            initializeUI();
            loadFinishedGamesAsync();
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
                this.gameController.setOnGameListChanged(this::refreshFinished);
                initializeUI();
                loadFinishedGamesAsync();
            } catch (Exception ex) {
                initializeErrorUI(ex.getMessage());
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(retryButton);

        rootPanel.add(errorLabel, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadFinishedGamesAsync() {
        SwingWorker<List<Game>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Game> doInBackground() {
                try {
                    return gameController.getFinishedGames();
                } catch (Exception e) {
                    return List.of();
                }
            }

            @Override
            protected void done() {
                try {
                    List<Game> finishedGames = get();
                    gameList.showGames(finishedGames);
                    rootPanel.revalidate();
                    rootPanel.repaint();
                } catch (Exception e) {
                    gameList.showError("Falha ao carregar jogos finalizados: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    public void refreshFinished() {
        if (gameList != null) {
            gameList.showLoading();
            loadFinishedGamesAsync();
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}