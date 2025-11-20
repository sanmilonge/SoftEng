/**
 * As a data analyst, I want the system to produce a report of all countries
 * in a specific region ordered by population from largest to smallest so that
 * I can identify the most and least populated regions */

package Coursework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ThirdReport
 * ------------
 * Generates per-region Markdown reports of countries by population.
 * Uses ReportManager to correctly handle normal and test report paths.
 */
public class ThirdReport {
    private final Connection c;

    public ThirdReport(Connection c) {
        this.c = c;
    }

    public void showCountriesByRegion() {
        GetAll helper = new GetAll(c);
        List<String> regions = helper.getAllRegions();
        String subfolder = "3_ThirdReport";

        for (String region : regions) {
            StringBuilder md = new StringBuilder();
            md.append("# Countries in ").append(region).append("\n\n");
            md.append("| Code | Name | Continent | Population |\n");
            md.append("|------|------|------------|-------------|\n");

            try {
                String query = "SELECT Code, Name, Continent, Population " +
                        "FROM country WHERE Region = ? ORDER BY Population DESC;";
                PreparedStatement pstmt = c.getConnection().prepareStatement(query);
                pstmt.setString(1, region);
                ResultSet rset = pstmt.executeQuery();

                while (rset.next()) {
                    md.append(String.format("| %s | %s | %s | %d |\n",
                            rset.getString("Code"),
                            rset.getString("Name"),
                            rset.getString("Continent"),
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