package Coursework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *Second Reports
 * ------------
 * Generates per-continent Markdown reports of countries by population.
 */
public class SecondReport {
    private final Connection c;

    public SecondReport(Connection c) {
        this.c = c;
    }

    private List<String> getAllContinents() {
        List<String> continents = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT Continent FROM country;";
            Statement stmt = c.getConnection().createStatement();
            ResultSet rslt = stmt.executeQuery(query);

            while (rslt.next()) {
                String continent = rslt.getString("Continent");
                if (continent != null && !continent.trim().isEmpty()) {
                    continents.add(continent.trim());
                }
            }
            rslt.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Failed to retrieve continents: " + e.getMessage());
        }
        return continents;
    }

    public void showCountriesContinent() {
        List<String> continents = getAllContinents();
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


