package br.com.fiap.cp4.app.view.frames;

import br.com.fiap.cp4.app.view.pages.LibraryPage;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel rootPanel;
    private JButton libraryListButton;
    private JButton favoriteListButton;
    private JButton finishedGamesListButton;
    private JButton categoriesButton;
    private JPanel contentPanel;

    public MainFrame() {
        setupMainScreen();
        setupComponents();
        loadInitialPage();
    }

    private void setupMainScreen() {
        setContentPane(rootPanel);
        setTitle("Games Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void setupComponents() {
        setupContentPanel();
        setupButtonListeners();
    }

    private void setupContentPanel() {
        LibraryPage libraryPage = new LibraryPage();
        contentPanel.add(libraryPage.getRootPanel(), "library");
    }

    private void setupButtonListeners() {
        libraryListButton.addActionListener(e -> {
            System.out.println("Mostrando todos os jogos");
            showLibraryPage();
        });

        favoriteListButton.addActionListener(e -> {
            System.out.println("Mostrando jogos favoritos");
            JOptionPane.showMessageDialog(this, "Carregando jogos favoritos...",
                    "Jogos Favoritos", JOptionPane.INFORMATION_MESSAGE);
        });

        finishedGamesListButton.addActionListener(e -> {
            System.out.println("Mostrando jogos finalizados");
            JOptionPane.showMessageDialog(this, "Carregando jogos finalizados...",
                    "Jogos Finalizados", JOptionPane.INFORMATION_MESSAGE);
        });

        categoriesButton.addActionListener(e -> {
            System.out.println("Mostrando categorias");
            JOptionPane.showMessageDialog(this, "Carregando categorias...",
                    "Categorias", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void loadInitialPage() {
        showLibraryPage();
    }

    private void showLibraryPage() {
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "library");
        setTitle("Games Management System - Biblioteca");
    }

    public void showWindow() {
        setVisible(true);
        toFront();
        requestFocus();
    }
}