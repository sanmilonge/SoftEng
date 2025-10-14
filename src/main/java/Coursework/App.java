package Coursework;


import java.sql.*;

/**
 * Main class for the Coursework application.
 * Connects to MySQL database and prints country population info.
 */
public class App {
    private Connection con = null;

    /**
     * Method to connect java program to database
     */
    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(5000); // shorter wait
                // Connect to the world database
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/world?allowPublicKeyRetrieval=true&useSSL=false", //Changed localhost to db and to correct world database
                        "root",
                        "example"
                );
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println(" Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnects server after query execution
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected from database.");
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Query: show all the countries in the world ordered by population
     * (largest to smallest).
     */
    public void showCountriesByPopulation() {
        try {
            Statement stmt = con.createStatement();
            String sql =
                    "SELECT Code, Name, Continent, Region, Population " +
                            "FROM country " +
                            "ORDER BY Population DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            System.out.println("\nAll countries in the world (largest to smallest population):\n");
            System.out.printf("%-5s %-45s %-20s %-25s %-15s%n",
                    "Code", "Name", "Continent", "Region", "Population");
            System.out.println("=".repeat(115));

            while (rset.next()) {
                System.out.printf("%-5s %-45s %-20s %-25s %-15d%n",
                        rset.getString("Code"),
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population"));
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve countries: " + e.getMessage());
        }
    }

    /**
     * Main entry point.
     * @param args command line arguments (not used).
     */
    public static void main(String[] args) {
        App a = new App();
        a.connect();
        a.showCountriesByPopulation();
        a.disconnect();
    }
}