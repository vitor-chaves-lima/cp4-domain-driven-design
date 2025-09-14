package br.com.fiap.cp4.app.view.components;

import br.com.fiap.cp4.core.model.entities.Game;

import javax.swing.*;
import java.awt.*;

public class GameCard extends JPanel {
    private JPanel rootPanel;

    public GameCard(Game game) {
        setPreferredSize(new Dimension(150, 100));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());

        JLabel label = new JLabel(game.getTitle(), SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}