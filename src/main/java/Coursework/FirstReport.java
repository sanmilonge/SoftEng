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
     * Generatess Markdown report of countries by population.
     */
    public FirstReport(Connection c) {
        this.c = c;
    }

    public void showCountriesByPopulation() {
        try {
            Statement stmt = c.getConnection().createStatement();
            String sql = "SELECT Code, Name, Continent, Region, Population " +
                    "FROM country ORDER BY Population DESC;";
            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# All the countries in the world organised by population\n\n");
            md.append("| Code | Name | Continent | Region | Population |\n");
            md.append("|------|------|------------|---------|-------------|\n");

            while (rset.next()) {
                md.append(String.format("| %s | %s | %s | %s | %d |\n",
                        rset.getString("Code"),
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population")));
            }

            ReportManager.writeMarkdown("1_FirstReport", "FirstReport.md", md.toString());
            System.out.println("First report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve countries: " + e.getMessage());
        }
    }
}