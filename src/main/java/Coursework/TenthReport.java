/**
 * As a data analyst, I want the system to produce a report of all cities 
 * in a country ordered by population from largest to smallest so that I 
 * can analyze national population distribution*/

package Coursework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TenthReport
 * ------------
 * Generates per-country Markdown reports of cities by population.
 */
public class TenthReport {
    private final Connection c;

    public TenthReport(Connection c) {
        this.c = c;
    }

    public void showCitiesByCountry() {
        GetAll helper = new GetAll(c);
        List<String> countries = helper.getAllCountries();
        String subfolder = "10_TenthReport";

        for (String country : countries) {
            StringBuilder md = new StringBuilder();
            md.append("# Cities in ").append(country).append("\n\n");
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
                                     WHERE country.Name = ?
                                     ORDER BY city.Population DESC;""";
                PreparedStatement pstmt = c.getConnection().prepareStatement(query);
                pstmt.setString(1, country);
                ResultSet rset = pstmt.executeQuery();

                while (rset.next()) {
                    md.append(String.format("| %s | %s | %s | %d |\n",
                            rset.getString("City"),
                            rset.getString("Country"),
                            rset.getString("District"),
                            rset.getInt("Population")));
                }

                String safeRegionName = country.replaceAll("[^a-zA-Z0-9\\-_ ]", "_");
                ReportManager.writeMarkdown(subfolder, safeRegionName + ".md", md.toString());

                System.out.println("Report saved for country: " + country);

                rset.close();
                pstmt.close();

            } catch (SQLException e) {
                System.out.println("Failed to create report for country " + country + ": " + e.getMessage());
            }
        }

        System.out.println("All country reports generated successfully in " + subfolder);
    }
}
