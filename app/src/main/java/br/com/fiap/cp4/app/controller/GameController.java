package br.com.fiap.cp4.app.controller;

import br.com.fiap.cp4.core.model.dao.impl.GameDAOImpl;
import br.com.fiap.cp4.core.model.dao.interfaces.GameDAO;
import br.com.fiap.cp4.core.model.entities.Game;
import br.com.fiap.cp4.app.view.dialogs.GameFormDialog;
import java.time.Year;
import java.awt.*;
import javax.swing.*;
import java.util.List;

/**
 * Controller responsible for managing game-related operations.
 * This class acts as an intermediary between the presentation layer (view) and the data layer (DAO),
 * providing methods for game CRUD operations, favorites management, and status control.
 * 
 * @author Game Management System
 * @version 1.0
 * @since 2024
 */
public class GameController {

    /** Data Access Object for game persistence operations */
    private GameDAO gameDAO;
    
    /** Callback executed when the game list is modified */
    private Runnable onGameListChanged;

    /**
     * Default constructor that initializes the controller with a GameDAO implementation.
     * Creates a new instance of GameDAOImpl to manage data persistence.
     */
    public GameController() {
        this.gameDAO = new GameDAOImpl();
    }

    /**
     * Retrieves all games registered in the system.
     * 
     * @return List containing all registered games. Returns an empty list in case of error.
     * @throws RuntimeException if there is an error communicating with the database
     */
    public List<Game> getAllGames() {
        try {
            return gameDAO.findAll();
        } catch (Exception e) {
            showErrorMessage("Erro ao carregar jogos: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Retrieves all games marked as favorites by the user.
     * 
     * @return List containing only the favorite games. Returns an empty list in case of error.
     * @throws RuntimeException if there is an error communicating with the database
     */
    public List<Game> getFavoriteGames() {
        try {
            return gameDAO.findFavorites();
        } catch (Exception e) {
            showErrorMessage("Erro ao carregar jogos favoritos: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Retrieves all games with status "COMPLETED" (finished).
     * 
     * @return List containing only the completed games. Returns an empty list in case of error.
     * @throws RuntimeException if there is an error communicating with the database
     */
    public List<Game> getFinishedGames() {
        try {
            return gameDAO.findByStatus("COMPLETED");
        } catch (Exception e) {
            showErrorMessage("Erro ao carregar jogos finalizados: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Creates a new game through a form dialog.
     * Displays a form for the user to fill in the new game's data and,
     * if confirmed, persists the game in the database.
     * 
     * @param parent Parent component for the modal dialog
     * @return The created game with assigned ID, or null if the operation was canceled or failed
     * @throws RuntimeException if an error occurs during persistence
     */
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

    /**
     * Edits an existing game through a form dialog.
     * Creates a copy of the game for editing and displays the pre-filled form.
     * If confirmed, updates the game in the database.
     * 
     * @param parent Parent component for the modal dialog
     * @param game Game to be edited (cannot be null)
     * @throws IllegalArgumentException if the provided game is null
     * @throws RuntimeException if an error occurs during the update
     */
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

    /**
     * Removes a game from the system.
     * Executes the game deletion in the database and updates the interface.
     * The list update is performed asynchronously in the EDT (Event Dispatch Thread).
     * 
     * @param game Game to be removed (cannot be null)
     * @throws IllegalArgumentException if the provided game is null
     * @throws RuntimeException if an error occurs during deletion
     */
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

    /**
     * Toggles the favorite status of a game.
     * If the game is marked as favorite, removes the mark.
     * If not marked, adds as favorite.
     * 
     * @param game Game to toggle favorite status (cannot be null)
     * @throws IllegalArgumentException if the provided game is null
     * @throws RuntimeException if an error occurs during the operation
     */
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

    /**
     * Updates the game list in the user interface.
     * Executes the update callback asynchronously in the EDT (Event Dispatch Thread)
     * to ensure thread safety in UI operations.
     */
    public void refreshGameList() {
        SwingUtilities.invokeLater(() -> {
            if (onGameListChanged != null) {
                onGameListChanged.run();
            }
        });
    }

    /**
     * Sets the callback to be executed when the game list is modified.
     * This callback is typically used to update the user interface
     * after CRUD operations on games.
     * 
     * @param callback Function to be executed when the game list changes
     */
    public void setOnGameListChanged(Runnable callback) {
        this.onGameListChanged = callback;
    }

    /**
     * Displays an error message in a modal dialog.
     * Uses JOptionPane to show errors to the user in a standardized way.
     * 
     * @param message Error message to be displayed
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}