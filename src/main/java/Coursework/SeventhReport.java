/**
 * As a data analyst, I want the system to produce a report of all
 * cities in the world ordered by population from largest to smallest
 * so that I can understand global urbanization patterns.
 */

package Coursework;

import java.sql.*;

public class SeventhReport {
    private final Connection c;

    public SeventhReport(Connection c) {
        this.c = c;
    }

    /**
     * Query: show all the cities in the world ordered by population
     * (largest to smallest).
     */
    public void showCitiesByPopulation() {
        try {
            Statement stmt = c.getConnection().createStatement();
            String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                ORDER BY city.Population DESC;
            """;

            ResultSet rset = stmt.executeQuery(sql);

            System.out.println("\nAll cities in the world (largest to smallest population):\n");
            System.out.printf("%-45s %-40s %-40s %-15s%n",
                    "City", "Country", "District", "Population");
            System.out.println("=".repeat(150));

            // Build Markdown file
            StringBuilder md = new StringBuilder();
            md.append("# All the cities in the world organised by largest population to smallest\n\n");
            md.append("| City | Country | District | Population |\n");
            md.append("|------|----------|-----------|-------------|\n");

            while (rset.next()) {
                String city = rset.getString("City");
                String country = rset.getString("Country");
                String district = rset.getString("District");
                int population = rset.getInt("Population");

                // Print to console
                System.out.printf("%-45s %-40s %-25s %-15d%n",
                        city, country, district, population);

                // Add to Markdown
                md.append(String.format("| %s | %s | %s | %d |\n",
                        city, country, district, population));
            }

            // Write markdown file
            ReportManager.writeMarkdown("SeventhReport.md", md.toString());

        } catch (Exception e) {
            System.out.println(" Failed to retrieve cities: " + e.getMessage());
        }
    }
}
