/**
 * As a Data Analyst I want the system to produce a report of all
 * countries in a specific region ordered by population from largest
 * to smallest so that I can identify the most and least populated
 * regions.
 */

package Coursework;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThirdReport {
    private final Connection c;

    public ThirdReport(Connection c) {
        this.c = c;
    }

    // Get all unique regions from the country table
    private List<String> getAllRegions() {
        List<String> regions = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT Region FROM country;";
            Statement stmt = c.getConnection().createStatement();
            ResultSet rslt = stmt.executeQuery(query);

            while (rslt.next()) {
                String region = rslt.getString("Region");
                if (region != null && !region.trim().isEmpty()) {
                    regions.add(region.trim());
                }
            }

            rslt.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Failed to retrieve regions: " + e.getMessage());
        }
        return regions;
    }

    // Generate one Markdown report per region
    public void showCountriesByRegion() {
        List<String> regions = getAllRegions();

        // Define the ThirdReport folder
        Path reportsDir = Paths.get("src/main/resources/reports/3_ThirdReport");
        try {
            Files.createDirectories(reportsDir);
            System.out.println("ThirdReport folder ready: " + reportsDir.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Could not create ThirdReport folder: " + e.getMessage());
            return;
        }

        // Loop through each region and create one Markdown report
        for (String region : regions) {
            String safeRegionName = region.replaceAll("[^a-zA-Z0-9\\-_ ]", "_");
            Path filePath = reportsDir.resolve(safeRegionName + ".md");

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

                // Write to Markdown file
                try (FileWriter writer = new FileWriter(filePath.toFile())) {
                    writer.write(md.toString());
                }

                System.out.println(" Report saved for region: " + region);

                rset.close();
                pstmt.close();

            } catch (SQLException e) {
                System.out.println("Failed to create report for region " + region + ": " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Could not write file for region " + region + ": " + e.getMessage());
            }
        }

        System.out.println("All regional reports generated successfully in " + reportsDir);
    }
}