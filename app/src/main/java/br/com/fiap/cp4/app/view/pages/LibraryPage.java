package br.com.fiap.cp4.app.view.pages;

import br.com.fiap.cp4.app.view.components.GameList;
import br.com.fiap.cp4.core.model.entities.Game;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LibraryPage {
    private JPanel rootPanel;
    private GameList gameList;

    public LibraryPage() {
        gameList.showLoading();
        loadGamesAsync();
    }

    private void loadGamesAsync() {
        CompletableFuture<List<Game>> gamesFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);

                return Arrays.asList(
                        createGame(1, "The Legend of Zelda: Breath of the Wild", 2017),
                        createGame(2, "Cyberpunk 2077", 2020),
                        createGame(3, "God of War", 2018),
                        createGame(4, "Red Dead Redemption 2", 2018),
                        createGame(5, "Minecraft", 2011),
                        createGame(6, "Grand Theft Auto V", 2013)
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return List.of();
            }
        });

        SwingWorker<List<Game>, Void> worker = new SwingWorker<List<Game>, Void>() {
            @Override
            protected List<Game> doInBackground() throws Exception {
                return gamesFuture.get();
            }

            @Override
            protected void done() {
                try {
                    List<Game> games = get();
                    gameList.showGames(games);
                } catch (Exception e) {
                    gameList.showError(e.getMessage());
                }
            }
        };

        worker.execute();
    }

    private Game createGame(int id, String title, int year) {
        Game game = new Game(title, null, null, year, null, null);
        game.setId(id);
        return game;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}