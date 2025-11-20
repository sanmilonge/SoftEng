/**
 * As a data analyst, I want the system to produce a report of all
 * cities in the world ordered by population from largest to smallest
 * so that I can understand global urbanization patterns*/

package Coursework;

import java.sql.*;

/**
 * SeventhReport
 * ------------
 * Generates Markdown report of cities by population.
 * Uses ReportManager to correctly handle normal and test report paths.
 */

public class SeventhReport {
    private final Connection c;

    public SeventhReport(Connection c) {
        this.c = c;
    }

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

            StringBuilder md = new StringBuilder();
            md.append("# All cities in the world ordered by population\n\n");
            md.append("| City | Country | District | Population |\n");
            md.append("|------|----------|-----------|-------------|\n");

            while (rset.next()) {
                md.append(String.format("| %s | %s | %s | %d |\n",
                        rset.getString("City"),
                        rset.getString("Country"),
                        rset.getString("District"),
                        rset.getInt("Population")));
            }

            ReportManager.writeMarkdown("7_SeventhReport", "SeventhReport.md", md.toString());
            System.out.println("Seventh report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve cities: " + e.getMessage());
        }
    }
}