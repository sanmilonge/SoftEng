/**
 *As a data analyst, I want the system to produce a report of all countries
 * in the world ordered by population (largest to smallest) so that I can
 * compare global population sizes */

package Coursework;

import java.sql.*;

public class FirstReport {
    private final Connection c;
    /**
     *First Report
     * ------------
     * Generates Markdown report of countries by population.
     */
    public FirstReport(Connection c) {
        this.c = c;
    }

    public void showCountriesByPopulation() {
        try {
            Statement stmt = c.getConnection().createStatement();
            String sql = "SELECT country.Code, country.Name AS Country, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                    "FROM country " +
                    "JOIN city " +
                    "ON city.ID = country.Capital " +
                    "ORDER BY country.Population DESC;";
            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# All the countries in the world organised by population\n\n");
            md.append("| Code | Country | Continent | Region | Population | Capital |\n");
            md.append("|------|------|------------|------------|-------------|------------|\n");

            while (rset.next()) {
                md.append(String.format("| %s | %s | %s | %s | %d | %s |\n",
                        rset.getString("Code"),
                        rset.getString("Country"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population"),
                        rset.getString("Capital")));

            }

            ReportManager.writeMarkdown("1_FirstReport", "FirstReport.md", md.toString());
            System.out.println("First report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve countries: " + e.getMessage());
        }
    }
}