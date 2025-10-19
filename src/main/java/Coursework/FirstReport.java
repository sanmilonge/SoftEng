package Coursework;

import java.sql.*;

public class FirstReport {
    private final Connection c;

    public FirstReport(Connection c) {
        this.c = c;
    }

    /**
     * Query: show all the countries in the world ordered by population
     * (largest to smallest).
     */
    public void showCountriesByPopulation() {
        try {
            Statement stmt = c.getConnection().createStatement();
            String sql = "SELECT Code, Name, Continent, Region, Population " +
                    "FROM country ORDER BY Population DESC;";
            ResultSet rset = stmt.executeQuery(sql);

            // Print header in console
            System.out.println("\nAll countries in the world (largest to smallest population):\n");
            System.out.printf("%-5s %-45s %-20s %-25s %-15s%n",
                    "Code", "Name", "Continent", "Region", "Population");
            System.out.println("=".repeat(115));

            // Create a StringBuilder for Markdown output
            StringBuilder md = new StringBuilder();
            md.append("# All the countries in the world organised by largest population to smallest\n\n");
            md.append("| Code | Name | Continent | Region | Population |\n");
            md.append("|------|------|------------|---------|-------------|\n");

            // Loop through results
            while (rset.next()) {
                String code = rset.getString("Code");
                String name = rset.getString("Name");
                String continent = rset.getString("Continent");
                String region = rset.getString("Region");
                int population = rset.getInt("Population");

                // Print to console
                System.out.printf("%-5s %-45s %-20s %-25s %-15d%n",
                        code, name, continent, region, population);

                // Add to markdown
                md.append(String.format("| %s | %s | %s | %s | %d |\n",
                        code, name, continent, region, population));
            }

            // Save to file using ReportManager
            ReportManager.writeMarkdown("FirstReport.md", md.toString());

        } catch (Exception e) {
            System.out.println(" Failed to retrieve countries: " + e.getMessage());
        }
    }
}
