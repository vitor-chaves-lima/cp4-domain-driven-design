package br.com.fiap.cp4;

import br.com.fiap.cp4.core.database.ConnectionFactory;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Games Management System ===");

        if (!ConnectionFactory.testConnection()) {
            System.err.println("Cannot connect to database. Check your configuration.");
            System.err.println("Make sure PostgreSQL is running and database.properties is correct.");
            return;
        }

        System.out.println("Database connection: OK");

        System.out.println("\n=== Application finished ===");
    }
}