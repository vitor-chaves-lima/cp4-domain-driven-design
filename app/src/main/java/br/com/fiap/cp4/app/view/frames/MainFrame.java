package br.com.fiap.cp4.app.view.frames;

import br.com.fiap.cp4.app.view.pages.LibraryPage;
import br.com.fiap.cp4.app.view.pages.FavoritesPage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private List<JButton> menuButtons;
    private JButton selectedButton;
    private LibraryPage libraryPage;
    private FavoritesPage favoritesPage;

    private static final Color SIDEBAR_COLOR = new Color(45, 52, 64);
    private static final Color SELECTED_COLOR = new Color(88, 101, 242);
    private static final Color HOVER_COLOR = new Color(76, 86, 106);
    private static final Color TEXT_NORMAL = new Color(200, 200, 200);
    private static final Color TEXT_SELECTED = Color.WHITE;

    public MainFrame() {
        menuButtons = new ArrayList<>();
        createComponents();
        setupFrame();
        showLibraryPage();
    }

    private void createComponents() {
        JPanel sidebar = createSidebar();

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        libraryPage = new LibraryPage();
        favoritesPage = new FavoritesPage();

        contentPanel.add(libraryPage.getRootPanel(), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel titleLabel = new JLabel("Game Manager");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton libraryButton = createMenuButton("Biblioteca", "library");
        JButton favoritesButton = createMenuButton("Favoritos", "favorites");
        JButton finishedButton = createMenuButton("Finalizados", "finished");

        menuButtons.add(libraryButton);
        menuButtons.add(favoritesButton);
        menuButtons.add(finishedButton);

        setSelectedButton(libraryButton);

        setupMenuListeners(libraryButton, favoritesButton, finishedButton);

        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(30));
        sidebar.add(libraryButton);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(favoritesButton);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(finishedButton);
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createMenuButton(String text, String actionCommand) {
        JButton button = new JButton(text);
        button.setActionCommand(actionCommand);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(170, 40));
        button.setPreferredSize(new Dimension(170, 40));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));

        setButtonUnselected(button);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button != selectedButton) {
                    button.setBackground(HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button != selectedButton) {
                    setButtonUnselected(button);
                }
            }
        });

        return button;
    }

    private void setupMenuListeners(JButton library, JButton favorites, JButton finished) {
        ActionListener menuListener = new MenuActionListener();

        library.addActionListener(menuListener);
        favorites.addActionListener(menuListener);
        finished.addActionListener(menuListener);
    }

    private class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String action = clickedButton.getActionCommand();

            setSelectedButton(clickedButton);

            switch (action) {
                case "library":
                    showLibraryPage();
                    break;
                case "favorites":
                    showFavoritesPage();
                    break;
                case "finished":
                    showFinishedPage();
                    break;
            }
        }
    }

    private void setSelectedButton(JButton button) {
        if (selectedButton != null) {
            setButtonUnselected(selectedButton);
        }

        selectedButton = button;
        setButtonSelected(button);
    }

    private void setButtonSelected(JButton button) {
        button.setBackground(SELECTED_COLOR);
        button.setForeground(TEXT_SELECTED);
    }

    private void setButtonUnselected(JButton button) {
        button.setBackground(SIDEBAR_COLOR);
        button.setForeground(TEXT_NORMAL);
    }

    private void setupFrame() {
        setTitle("Games Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void showLibraryPage() {
        contentPanel.removeAll();
        contentPanel.add(libraryPage.getRootPanel(), BorderLayout.CENTER);
        libraryPage.refreshGames();
        contentPanel.revalidate();
        contentPanel.repaint();
        setTitle("Games Management System - Biblioteca");
    }

    private void showFavoritesPage() {
        contentPanel.removeAll();
        contentPanel.add(favoritesPage.getRootPanel(), BorderLayout.CENTER);
        favoritesPage.refreshFavorites();
        contentPanel.revalidate();
        contentPanel.repaint();
        setTitle("Games Management System - Favoritos");
    }

    private void showFinishedPage() {
        JOptionPane.showMessageDialog(this, "Carregando jogos finalizados...",
                "Jogos Finalizados", JOptionPane.INFORMATION_MESSAGE);
        setTitle("Games Management System - Finalizados");
    }

    public void showWindow() {
        setVisible(true);
        toFront();
        requestFocus();
    }
}