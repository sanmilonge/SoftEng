/**As a policy maker, I want the system to produce a report of the most
 * populated countries in a region, based on a number I specify, so that
 * I can make regionally targeted decisions*/

package Coursework;

import java.sql.*;

public class SixthReport {
    private final Connection c;

    public SixthReport(Connection c) {
        this.c = c;
    }

    public void showTopNCountriesInRegion(int n, String region) {
        String sql =
                "SELECT country.Code, " +
                        "country.Name AS Country, " +
                        "country.Continent, " +
                        "country.Region, " +
                        "country.Population, " +
                        "city.Name AS Capital " +
                        "FROM country " +
                        "JOIN city ON city.ID = country.Capital " +
                        "WHERE country.Region = ? " +
                        "ORDER BY country.Population DESC " +
                        "LIMIT ?;";

        try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, region);
            pstmt.setInt(2, n);

            ResultSet rset = pstmt.executeQuery();

            StringBuilder md = new StringBuilder();
            md.append("# ").append(n)
                    .append(" top populated countries in ").append(region).append("\n\n");
            md.append("| Code | Country | Continent | Region | Population | Capital |\n");
            md.append("|------|---------|-----------|--------|------------|---------|\n");

            while (rset.next()) {
                md.append(String.format(
                        "| %s | %s | %s | %s | %d | %s |\n",
                        rset.getString("Code"),
                        rset.getString("Country"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population"),
                        rset.getString("Capital")
                ));
            }

            ReportManager.writeMarkdown(
                    "6_SixthReport",
                    n + "_Top_Populated_Countries_In_" + region.replace(" ", "_") + ".md",
                    md.toString()
            );

            System.out.println("Sixth report completed.");
        } catch (SQLException e) {
            System.out.println("Failed to retrieve the " + n +
                    " most populated countries in " + region + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}