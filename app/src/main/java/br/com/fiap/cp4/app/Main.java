package br.com.fiap.cp4.app;

import br.com.fiap.cp4.app.view.frames.SplashScreen;

import javax.swing.*;

import br.com.fiap.cp4.core.database.ConnectionFactory;
import com.formdev.flatlaf.FlatDarkLaf;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Games Management System ===");

        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing database connection");
            ConnectionFactory.shutdown();
        }));

        SwingUtilities.invokeLater(SplashScreen::new);
    }
}
