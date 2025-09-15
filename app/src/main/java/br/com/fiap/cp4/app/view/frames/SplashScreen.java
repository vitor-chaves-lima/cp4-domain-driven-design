package br.com.fiap.cp4.app.view.frames;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    public SplashScreen() {
        setupSplashScreen();
        showMainFrame();
    }

    private void setupSplashScreen() {
        setTitle("Game Manager - Carregando...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 52, 64));

        JLabel label = new JLabel("Carregando Game Manager...", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(label, BorderLayout.CENTER);
        setContentPane(panel);
    }

    private void showMainFrame() {
        Timer timer = new Timer(1000, e -> {
            setVisible(false);
            dispose();

            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.showWindow();
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void show() {
        setVisible(true);
    }
}