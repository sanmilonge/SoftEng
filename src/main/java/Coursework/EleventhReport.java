/**
 * As a Data Analyst I want the system to produce a report of all cities in a
 * district ordered by population from largest to smallest so that I can view
 * local-level demographic data.
 */

package Coursework;

import java.sql.*;
import java.util.List;

/**
 * EleventhReport
 * ---------------
 * Generates per-district Markdown reports of cities by population.
 */
public class EleventhReport {
    private final Connection c;

    public EleventhReport(Connection c) {
        this.c = c;
    }

    public void showCitiesByDistrict() {

        // Helper to obtain distinct districts
        GetAll helper = new GetAll(c);
        List<String> districts = helper.getAllDistricts();

        String subfolder = "11_EleventhReport";

        for (String district : districts) {

            // Build markdown header + table structure
            StringBuilder md = new StringBuilder();
            md.append("# Cities in ").append(district).append("\n\n");
            md.append("| City | Country | District | Population |\n");
            md.append("|------|----------|-----------|-------------|\n");

            try {
                // SQL: Select all cities in this district ordered by population
                String query = """
                        SELECT city.Name AS City,
                               country.Name AS Country,
                               city.District,
                               city.Population
                        FROM city
                        JOIN country
                          ON city.CountryCode = country.Code
                        WHERE city.District = ?
                        ORDER BY city.Population DESC;
                        """;

                PreparedStatement pstmt = c.getConnection().prepareStatement(query);
                pstmt.setString(1, district);

                ResultSet rset = pstmt.executeQuery();

                // Populate table rows
                while (rset.next()) {
                    md.append(String.format("| %s | %s | %s | %d |\n",
                            rset.getString("City"),
                            rset.getString("Country"),
                            rset.getString("District"),
                            rset.getInt("Population")));
                }

                // Clean filename (remove invalid characters)
                String safeDistrictName = district.replaceAll("[^a-zA-Z0-9\\-_ ]", "_");

                // Save generated markdown
                ReportManager.writeMarkdown(subfolder, safeDistrictName + ".md", md.toString());

                System.out.println("Report saved for district: " + district);

                rset.close();
                pstmt.close();

            } catch (SQLException e) {
                System.out.println("Failed to create report for district " + district + ": " + e.getMessage());
            }
        }

        System.out.println("All district reports generated successfully in " + subfolder);
    }
}
