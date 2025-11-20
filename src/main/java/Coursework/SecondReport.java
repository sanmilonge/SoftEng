/**
 * As a data analyst, I want the system to produce a report of all countries
 * in a specific region ordered by population from largest to smallest so that
 * I can identify the most and least populated regions*/

package Coursework;

import java.sql.*;
import java.util.List;


/**
 *Second Reports
 * ------------
 * Generates per-continent Markdown report of countries by population.
 */
public class SecondReport {
    private final Connection c;

    public SecondReport(Connection c) {
        this.c = c;
    }

    public void showCountriesContinent() {
        GetAll helper = new GetAll(c);
        List<String> continents = helper.getAllContinents();
        String subfolder = "2_SecondReport";

        for (String continent : continents) {
            StringBuilder md = new StringBuilder();
            md.append("# Countries in ").append(continent).append("\n\n");
            md.append("| Code | Country | Region | Population |\n");
            md.append("|------|------|------------|-------------|\n");

            try {
                String query = "SELECT Code, Name, Region, Population " +
                        "FROM country WHERE Continent = ? ORDER BY Population DESC;";
                PreparedStatement pstmt = c.getConnection().prepareStatement(query);
                pstmt.setString(1, continent);
                ResultSet rset = pstmt.executeQuery();

                while (rset.next()) {
                    md.append(String.format("| %s | %s | %s | %d |\n",
                            rset.getString("Code"),
                            rset.getString("Name"),
                            rset.getString("Region"),
                            rset.getInt("Population")));
                }

                String safeContinentName = continent.replaceAll("[^a-zA-Z0-9\\-_ ]", "_");
                ReportManager.writeMarkdown(subfolder, safeContinentName + ".md", md.toString());

                System.out.println("Report saved for continent: " + continent);

                rset.close();
                pstmt.close();

            } catch (SQLException e) {
                System.out.println("Failed to create report for continent " + continent + ": " + e.getMessage());
            }
        }

        System.out.println("All reports for countries grouped by continents have been generated successfully in " + subfolder);
    }
}


