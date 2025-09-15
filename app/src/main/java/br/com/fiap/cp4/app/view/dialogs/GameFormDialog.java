package br.com.fiap.cp4.app.view.dialogs;

import br.com.fiap.cp4.app.controller.GameController;
import br.com.fiap.cp4.core.model.entities.Game;
import br.com.fiap.cp4.core.model.enums.GameGenre;
import br.com.fiap.cp4.core.model.enums.GamePlatform;
import br.com.fiap.cp4.core.model.enums.GameStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;

public class GameFormDialog extends JDialog {
    private final GameController controller;
    private final Game game;
    private final boolean isEditMode;
    private Game resultGame = null;

    private JTextField titleField;
    private JComboBox<GameGenre> genreComboBox;
    private JComboBox<GamePlatform> platformComboBox;
    private JTextField yearField;
    private JComboBox<GameStatus> statusComboBox;

    private static final Color BACKGROUND_COLOR = new Color(67, 76, 94);
    private static final Color TEXT_COLOR = new Color(236, 239, 244);
    private static final Color SECONDARY_TEXT_COLOR = new Color(180, 188, 200);
    private static final Color FIELD_COLOR = new Color(76, 86, 106);
    private static final Color PRIMARY_COLOR = new Color(88, 101, 242);
    private static final Color PRIMARY_HOVER_COLOR = new Color(78, 91, 232);
    private static final Color CANCEL_COLOR = new Color(94, 105, 120);
    private static final Color CANCEL_HOVER_COLOR = new Color(110, 120, 135);

    public GameFormDialog(Frame owner, GameController controller, Game game, boolean isEditMode) {
        super(owner, isEditMode ? "Editar Jogo" : "Adicionar Jogo", true);
        this.controller = controller;
        this.game = game != null ? game : new Game("", null, null, Year.now().getValue(), null, null);
        this.isEditMode = isEditMode;

        initializeComponents();
        layoutComponents();
        setupKeyboardShortcuts();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initializeComponents() {
        titleField = createStyledTextField(game.getTitle(), 20);

        genreComboBox = createStyledComboBox(GameGenre.values());
        if (game.getGenre() != null) {
            genreComboBox.setSelectedItem(game.getGenre());
        }

        platformComboBox = createStyledComboBox(GamePlatform.values());
        if (game.getPlatform() != null) {
            platformComboBox.setSelectedItem(game.getPlatform());
        }

        yearField = createStyledTextField(String.valueOf(game.getReleaseYear()), 4);
        yearField.setHorizontalAlignment(JTextField.CENTER);

        statusComboBox = createStyledComboBox(GameStatus.values());
        if (game.getStatus() != null) {
            statusComboBox.setSelectedItem(game.getStatus());
        }
    }

    private JTextField createStyledTextField(String text, int columns) {
        JTextField field = new JTextField(text, columns);
        field.setBackground(FIELD_COLOR);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(new EmptyBorder(8, 12, 8, 12));
        field.setFont(new Font("Dialog", Font.PLAIN, 14));
        return field;
    }

    private <T> JComboBox<T> createStyledComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setBackground(FIELD_COLOR);
        combo.setForeground(TEXT_COLOR);
        combo.setFont(new Font("Dialog", Font.PLAIN, 14));
        combo.setBorder(new EmptyBorder(4, 8, 4, 8));
        return combo;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Dialog", Font.PLAIN, 14));
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Dialog", Font.PLAIN, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor);
                }
            }
        });

        return button;
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(25, 25, 20, 25));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(createStyledLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 15, 8, 0);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);
        formPanel.add(createStyledLabel("Gênero:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 15, 8, 0);
        formPanel.add(genreComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);
        formPanel.add(createStyledLabel("Plataforma:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 15, 8, 0);
        formPanel.add(platformComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);
        formPanel.add(createStyledLabel("Ano de Lançamento:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 15, 8, 0);
        formPanel.add(yearField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);
        formPanel.add(createStyledLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 15, 8, 0);
        formPanel.add(statusComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton cancelButton = createStyledButton("Cancelar", CANCEL_COLOR, CANCEL_HOVER_COLOR, TEXT_COLOR);
        JButton saveButton = createStyledButton(isEditMode ? "Salvar" : "Adicionar", PRIMARY_COLOR, PRIMARY_HOVER_COLOR, Color.WHITE);

        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveGame());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        getRootPane().setDefaultButton(saveButton);
    }

    private void setupKeyboardShortcuts() {
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private void saveGame() {
        try {
            String title = titleField.getText().trim();
            GameGenre genre = (GameGenre) genreComboBox.getSelectedItem();
            GamePlatform platform = (GamePlatform) platformComboBox.getSelectedItem();
            String yearText = yearField.getText().trim();
            GameStatus status = (GameStatus) statusComboBox.getSelectedItem();

            if (title.isEmpty()) {
                showError("O título não pode estar vazio");
                titleField.requestFocus();
                return;
            }

            if (title.length() > 100) {
                showError("O título não pode ter mais de 100 caracteres");
                titleField.requestFocus();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearText);
                if (year < 1970 || year > Year.now().getValue() + 3) {
                    showError("O ano deve estar entre 1970 e " + (Year.now().getValue() + 3));
                    yearField.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Por favor, insira um ano válido");
                yearField.requestFocus();
                return;
            }

            game.setTitle(title);
            game.setGenre(genre);
            game.setPlatform(platform);
            game.setReleaseYear(year);
            game.setStatus(status);

            resultGame = game;
            dispose();

        } catch (Exception e) {
            showError("Erro ao salvar jogo: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static Game showDialog(Component parent, GameController controller, Game game, boolean isEditMode) {
        Window parentWindow = parent instanceof Window ? (Window) parent : SwingUtilities.getWindowAncestor(parent);
        Frame parentFrame = parentWindow instanceof Frame ? (Frame) parentWindow : null;

        GameFormDialog dialog = new GameFormDialog(parentFrame, controller, game, isEditMode);
        dialog.setVisible(true);
        return dialog.resultGame;
    }
}