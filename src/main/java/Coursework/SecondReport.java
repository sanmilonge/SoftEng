/**
 *As a data analyst, I want the system to produce a report of all countries
 * in a selected continent ordered by population (largest to smallest) so
 * that I can analyse population distribution within that continent*/

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
            md.append("| Code | Country | Continent | Region | Population | Capital |\n");
            md.append("|------|------|------------|------------|-------------|------------|\n");

            try {
                String query =     "SELECT country.Code, country.Name AS Country, country.Continent, country.Region, " +
                        "country.Population, city.Name AS Capital " +
                        "FROM country " +
                        "LEFT JOIN city ON country.Capital = city.ID WHERE Continent = ? " +
                        "ORDER BY country.Population DESC;";
                PreparedStatement pstmt = c.getConnection().prepareStatement(query);
                pstmt.setString(1, continent);
                ResultSet rset = pstmt.executeQuery();

                while (rset.next()) {
                    md.append(String.format("| %s | %s | %s | %s | %d | %s |\n",
                            rset.getString("Code"),
                            rset.getString("Country"),
                            rset.getString("Continent"),
                            rset.getString("Region"),
                            rset.getInt("Population"),
                            rset.getString("Capital")
                    ));
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


