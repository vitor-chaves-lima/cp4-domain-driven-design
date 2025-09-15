package br.com.fiap.cp4.app.controller;

import br.com.fiap.cp4.core.model.dao.impl.GameDAOImpl;
import br.com.fiap.cp4.core.model.dao.interfaces.GameDAO;
import br.com.fiap.cp4.core.model.entities.Game;
import br.com.fiap.cp4.app.view.dialogs.GameFormDialog;
import java.time.Year;
import java.awt.*;
import javax.swing.*;
import java.util.List;

public class GameController {

    private GameDAO gameDAO;
    private Runnable onGameListChanged;

    public GameController() {
        this.gameDAO = new GameDAOImpl();
    }

    public List<Game> getAllGames() {
        try {
            return gameDAO.findAll();
        } catch (Exception e) {
            showErrorMessage("Erro ao carregar jogos: " + e.getMessage());
            return List.of();
        }
    }

    public Game createNewGame(Component parent) {
        Game newGame = new Game("", null, null, Year.now().getValue(), null, null);
        Game result = GameFormDialog.showDialog(parent, this, newGame, false);
        if (result != null) {
            try {
                Game createdGame = gameDAO.save(result);
                refreshGameList();
                return createdGame;
            } catch (Exception e) {
                showErrorMessage("Erro ao criar jogo: " + e.getMessage());
            }
        }
        return null;
    }

    public void editGame(Component parent, Game game) {
        if (game == null) {
            showErrorMessage("Nenhum jogo selecionado para edição");
            return;
        }

        Game gameToEdit = new Game(
                game.getTitle(),
                game.getGenre(),
                game.getPlatform(),
                game.getReleaseYear(),
                game.getStatus(),
                game.getImageData()
        );
        gameToEdit.setId(game.getId());

        Game result = GameFormDialog.showDialog(parent, this, gameToEdit, true);
        if (result != null) {
            try {
                gameDAO.update(result);
                refreshGameList();
            } catch (Exception e) {
                showErrorMessage("Erro ao atualizar jogo: " + e.getMessage());
            }
        }
    }

    public void deleteGame(Game game) {
        if (game == null) {
            showErrorMessage("Nenhum jogo selecionado para exclusão");
            return;
        }

        try {
            boolean deleted = gameDAO.deleteById(game.getId());
            if (deleted) {
                SwingUtilities.invokeLater(() -> refreshGameList());
            } else {
                showErrorMessage("Falha ao excluir jogo");
            }
        } catch (Exception e) {
            showErrorMessage("Erro ao excluir jogo: " + e.getMessage());
        }
    }

    public void toggleFavorite(Game game) {
        if (game == null) {
            showErrorMessage("Nenhum jogo selecionado");
            return;
        }

        try {
            boolean success = gameDAO.toggleFavorite(game.getId());
            if (success) {
                refreshGameList();
            } else {
                showErrorMessage("Falha ao alterar favorito");
            }
        } catch (Exception e) {
            showErrorMessage("Erro ao alterar favorito: " + e.getMessage());
        }
    }

    public void refreshGameList() {
        SwingUtilities.invokeLater(() -> {
            if (onGameListChanged != null) {
                onGameListChanged.run();
            }
        });
    }

    public void setOnGameListChanged(Runnable callback) {
        this.onGameListChanged = callback;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}