/**
 * As a Data Analyst I want the system to produce a report of all cities in a
 * region ordered by population from largest to smallest so that I can study
 * urban concentration at the regional level.*/

package Coursework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NinthReport
 * ------------
 * Generates per-region Markdown reports of cities by population.
 */
public class NinthReport {
    private final Connection c;

    public NinthReport(Connection c) {
        this.c = c;
    }

    public void showCitiesByRegion() {
        GetAll helper = new GetAll(c);
        List<String> regions = helper.getAllRegions();
        String subfolder = "9_NinthReport";

        for (String region : regions) {
            StringBuilder md = new StringBuilder();
            md.append("# Cities in ").append(region).append("\n\n");
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
                                     WHERE country.Region = ?
                                     ORDER BY city.Population DESC;""";
                PreparedStatement pstmt = c.getConnection().prepareStatement(query);
                pstmt.setString(1, region);
                ResultSet rset = pstmt.executeQuery();

                while (rset.next()) {
                    md.append(String.format("| %s | %s | %s | %d |\n",
                            rset.getString("City"),
                            rset.getString("Country"),
                            rset.getString("District"),
                            rset.getInt("Population")));
                }

                String safeRegionName = region.replaceAll("[^a-zA-Z0-9\\-_ ]", "_");
                ReportManager.writeMarkdown(subfolder, safeRegionName + ".md", md.toString());

                System.out.println("Report saved for region: " + region);

                rset.close();
                pstmt.close();

            } catch (SQLException e) {
                System.out.println("Failed to create report for region " + region + ": " + e.getMessage());
            }
        }

        System.out.println("All regional reports generated successfully in " + subfolder);
    }
}