package br.com.fiap.cp4.app.controller;

import br.com.fiap.cp4.core.model.dao.impl.GameDAOImpl;
import br.com.fiap.cp4.core.model.dao.interfaces.GameDAO;
import br.com.fiap.cp4.core.model.entities.Game;

import javax.swing.*;
import java.util.List;

public class GameController {

    private GameDAO gameDAO;

    public GameController() {
        this.gameDAO = new GameDAOImpl();
    }

    public List<Game> getAllGames() {
        try {
            return gameDAO.findAll();
        } catch (Exception e) {
            showErrorMessage("Error loading games: " + e.getMessage());
            return List.of(); // Return empty list on error
        }
    }

    public void createNewGame() {
        // TODO: Open game form dialog for creation
        showInfoMessage("Create New Game - Coming Soon!");
    }

    public void editGame(Game game) {
        if (game == null) {
            showErrorMessage("No game selected for editing");
            return;
        }
        // TODO: Open game form dialog for editing
        showInfoMessage("Edit Game - Coming Soon!");
    }

    public void deleteGame(Game game) {
        if (game == null) {
            showErrorMessage("No game selected for deletion");
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete '" + game.getTitle() + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = gameDAO.deleteById(game.getId());
                if (deleted) {
                    showSuccessMessage("Game deleted successfully!");
                    refreshGameList();
                } else {
                    showErrorMessage("Failed to delete game");
                }
            } catch (Exception e) {
                showErrorMessage("Error deleting game: " + e.getMessage());
            }
        }
    }

    public void refreshGameList() {
        // This will be called by the view to refresh data
        showInfoMessage("Refreshing game list...");
    }

    public int getTotalGames() {
        try {
            return gameDAO.countTotal();
        } catch (Exception e) {
            showErrorMessage("Error counting games: " + e.getMessage());
            return 0;
        }
    }

    // Helper methods for showing messages
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}