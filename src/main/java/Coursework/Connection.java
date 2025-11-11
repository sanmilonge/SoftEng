package Coursework;

import java.sql.*;

public class Connection {
    private java.sql.Connection con = null;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            e.printStackTrace();
            return;
        }

        // Determine database host automatically
        String host = System.getenv("DB_HOST"); // environment override
        if (host == null || host.isEmpty()) {
            // Detect environment: inside Docker/CI, use container hostname
            if (System.getenv("GITHUB_ACTIONS") != null) {
                host = "world";  // GitHub Actions / Docker container hostname
                System.out.println("[Connection] Detected GitHub Actions → Using host: world-db");
            } else {
                host = "localhost"; // local IntelliJ / laptop run
                System.out.println("[Connection] Running locally → Using host: localhost");
            }
        } else {
            System.out.println("[Connection] Using host from environment: " + host);
        }

        String url = String.format(
                "jdbc:mysql://%s:3306/world?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                host
        );

        int retries = 20;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database (attempt " + (i + 1) + "/" + retries + ")...");
            try {
                Thread.sleep(5000);
                con = DriverManager.getConnection(url, "root", "example");
                System.out.println("Successfully connected to database at " + host + ":3306");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect: " + sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }

        if (con == null) {
            System.out.println("Could not establish a database connection after retries.");
        }
    }

    public java.sql.Connection getConnection() {
        return con;
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("[Connection] 🔌 Disconnected from database.");
            } catch (Exception e) {
                System.out.println("Error closing connection to database.");
            } finally {
                con = null;
            }
        }
    }
}