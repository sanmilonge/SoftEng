/**
 * As a data analyst, I want the system to produce a report of all cities in a continent
 * ordered by population from largest to smallest so that I can study urban concentration
 * at the continental level */

package Coursework;

import java.sql.*;
import java.util.List;

/**
 *Eighth Report
 * ------------
 * Generates per-continent Markdown reports of cities by population.
 */
public class EighthReport {
    private final Connection c;

    public EighthReport(Connection c) {
        this.c = c;
    }


    public void showCitiesContinent() {
        GetAll helper = new GetAll(c);
        List<String> continents = helper.getAllContinents();
        String subfolder = "8_EighthReport";

        for (String continent : continents) {
            StringBuilder md = new StringBuilder();
            md.append("# Cities in ").append(continent).append("\n\n");
            md.append("| City | Country | District | Population |\n");
            md.append("|------|----------|-----------|-------------|\n");

            try {
                String query = """
                                    SELECT city.Name AS City, 
                                      country.Name AS Country, 
                                      city.District, 
                                      city.Population 
                                     FROM city 
                                     JOIN country 
                                     ON city.CountryCode =country.Code
                                     WHERE country.Continent = ? 
                                     ORDER BY city.Population DESC;""";
                PreparedStatement pstmt = c.getConnection().prepareStatement(query);
                pstmt.setString(1, continent);
                ResultSet rset = pstmt.executeQuery();

                while (rset.next()) {
                    md.append(String.format("| %s | %s | %s | %d |\n",
                            rset.getString("City"),
                            rset.getString("Country"),
                            rset.getString("District"),
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

        System.out.println("All reports for cities grouped by continents have been generated successfully in " + subfolder);
    }
}


