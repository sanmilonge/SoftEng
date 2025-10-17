package Coursework;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Query: show all cities in the world ordered by population
 * (largest to smallest).
 */
public class SecondReport {
    App app = new App();

    public void showCitiesByPopulation() {
        try {
            Statement stmt = app.con.createStatement();
            String sql =
                    "SHOW TABLES;";

            ResultSet rset = stmt.executeQuery(sql);

            System.out.println("\nAll cities in the world (largest to smallest population):\n");
            System.out.printf("%-35s %-35s %-25s %-15s%n",
                    "City", "Country", "District", "Population");
            System.out.println("=".repeat(115));

            while (rset.next()) {
                System.out.printf("%-35s %-35s %-25s %-15d%n",
                        rset.getString("City"),
                        rset.getString("Country"),
                        rset.getString("District"),
                        rset.getInt("Population"));
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve cities: " + e.getMessage());
        }
    }
}
