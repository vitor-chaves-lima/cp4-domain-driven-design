package br.com.fiap.cp4.app.view.frames;

import br.com.fiap.cp4.core.database.ConnectionFactory;

import javax.swing.*;
import java.util.List;

public class SplashScreen extends JFrame {
    private JProgressBar progressBar;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;

    public SplashScreen() {
        setupSplashScreen();
        SwingUtilities.invokeLater(this::testDatabaseConnection);
    }

    private void setupSplashScreen() {
        setTitle("Games Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setAlwaysOnTop(true);
        setContentPane(mainPanel);
        setSize(550, 300);
        setLocationRelativeTo(null);

        progressBar.setStringPainted(true);
        progressBar.setString("Carregando...");
        progressBar.setIndeterminate(true);

        setVisible(true);
    }

    private void testDatabaseConnection() {
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                publish("Testando conexão com banco de dados...");

                boolean connected = ConnectionFactory.testConnection();

                if (!connected) {
                    publish("Falha na conexão com banco de dados!");
                    return false;
                }

                publish("Verificando estrutura do banco...");
                boolean tablesExist = ConnectionFactory.tableExists("games");

                if (!tablesExist) {
                    publish("Criando tabelas do banco...");
                    ConnectionFactory.executeSchemaInitialization();
                }

                publish("Sistema pronto para uso!");
                return true;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String status : chunks) {
                    progressBar.setString(status);
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(100);

                    if (success) {
                        Timer timer = new Timer(1000, e -> {
                            openMainSystem();
                            // Fecha a splash screen após abrir a main screen
                            SwingUtilities.invokeLater(() -> dispose());
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        showConnectionError();
                    }
                } catch (Exception e) {
                    progressBar.setString("Erro na inicialização");
                    System.err.println("Erro durante inicialização: " + e.getMessage());
                    showConnectionError();
                }
            }
        };

        worker.execute();
    }

    private void showConnectionError() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Não foi possível conectar ao banco de dados.\n" +
                        "Deseja tentar novamente?",
                "Erro de Conexão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            progressBar.setIndeterminate(true);
            progressBar.setString("Reconectando...");

            Timer retryTimer = new Timer(1000, e -> testDatabaseConnection());
            retryTimer.setRepeats(false);
            retryTimer.start();
        } else {
            System.exit(0);
        }
    }

    private void openMainSystem() {
        try {
            MainScreen mainScreen = new MainScreen();
            mainScreen.showWindow();
        } catch (Exception e) {
            System.err.println("Error creating main screen: " + e.getMessage());
        }
    }
}