package br.com.fiap.cp4.app.view.frames;

import javax.swing.*;

public class MainScreen extends JFrame {
    private JPanel mainPanel;
    private JButton libraryListButton;
    private JButton favoriteListButton;
    private JButton finishedGamesListButton;
    private JButton categoriesButton;
    private JTree list;

    public MainScreen() {
        setupMainScreen();
        setupComponents();
    }

    private void setupMainScreen() {
        setTitle("Games Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setContentPane(mainPanel);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void setupComponents() {
        setupButtonListeners();
        setupTree();
    }

    private void setupButtonListeners() {
        libraryListButton.addActionListener(e -> {
            System.out.println("Mostrando todos os jogos");
            JOptionPane.showMessageDialog(this, "Carregando lista de todos os jogos...",
                    "Todos os Jogos", JOptionPane.INFORMATION_MESSAGE);
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

    private void setupTree() {
        if (list != null) {
            list.setRootVisible(true);
            list.setShowsRootHandles(true);

            // Expandir todos os nós por padrão
            for (int i = 0; i < list.getRowCount(); i++) {
                list.expandRow(i);
            }

            // Adicionar listener para cliques na árvore
            list.addTreeSelectionListener(e -> {
                if (list.getLastSelectedPathComponent() != null) {
                    System.out.println("Selecionado: " + list.getLastSelectedPathComponent());
                }
            });
        }
    }

    public void showWindow() {
        setVisible(true);
        toFront();
        requestFocus();
    }
}