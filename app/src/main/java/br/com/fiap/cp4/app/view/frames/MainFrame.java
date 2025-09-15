package br.com.fiap.cp4.app.view.frames;

import br.com.fiap.cp4.app.view.pages.LibraryPage;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;

    public MainFrame() {
        createComponents();
        setupFrame();
        showLibraryPage();
    }

    private void createComponents() {
        // Menu lateral
        JPanel sidebar = createSidebar();

        // Painel de conteúdo principal
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(new LibraryPage().getRootPanel(), "library");

        // Layout principal
        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 52, 64));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Título do aplicativo
        JLabel titleLabel = new JLabel("Game Manager");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botões do menu
        JButton libraryButton = createMenuButton("Biblioteca", true);
        JButton favoritesButton = createMenuButton("Favoritos", false);
        JButton finishedButton = createMenuButton("Finalizados", false);
        JButton categoriesButton = createMenuButton("Categorias", false);

        // Configurar listeners
        setupMenuListeners(libraryButton, favoritesButton, finishedButton, categoriesButton);

        // Adicionar componentes ao sidebar
        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(30));
        sidebar.add(libraryButton);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(favoritesButton);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(finishedButton);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(categoriesButton);
        sidebar.add(Box.createVerticalGlue()); // Empurra tudo para cima

        return sidebar;
    }

    private JButton createMenuButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(170, 40));
        button.setPreferredSize(new Dimension(170, 40));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));

        if (selected) {
            button.setBackground(new Color(88, 101, 242));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(45, 52, 64));
            button.setForeground(new Color(200, 200, 200));
        }

        // Efeitos de hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!selected) {
                    button.setBackground(new Color(76, 86, 106));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!selected) {
                    button.setBackground(new Color(45, 52, 64));
                }
            }
        });

        return button;
    }

    private void setupMenuListeners(JButton library, JButton favorites, JButton finished, JButton categories) {
        library.addActionListener(e -> {
            showLibraryPage();
            updateSelectedButton(library, favorites, finished, categories);
        });

        favorites.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Carregando jogos favoritos...",
                    "Jogos Favoritos", JOptionPane.INFORMATION_MESSAGE);
            updateSelectedButton(favorites, library, finished, categories);
        });

        finished.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Carregando jogos finalizados...",
                    "Jogos Finalizados", JOptionPane.INFORMATION_MESSAGE);
            updateSelectedButton(finished, library, favorites, categories);
        });

        categories.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Carregando categorias...",
                    "Categorias", JOptionPane.INFORMATION_MESSAGE);
            updateSelectedButton(categories, library, favorites, finished);
        });
    }

    private void updateSelectedButton(JButton selected, JButton... others) {
        // Destacar botão selecionado
        selected.setBackground(new Color(88, 101, 242));
        selected.setForeground(Color.WHITE);

        // Resetar outros botões
        for (JButton button : others) {
            button.setBackground(new Color(45, 52, 64));
            button.setForeground(new Color(200, 200, 200));
        }
    }

    private void setupFrame() {
        setTitle("Games Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void showLibraryPage() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "library");
        setTitle("Games Management System - Biblioteca");
    }

    public void showWindow() {
        setVisible(true);
        toFront();
        requestFocus();
    }
}