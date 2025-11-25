/**As a policy maker, I want the system to produce a report of the most
 * populated countries in a region, based on a number I specify, so that
 * I can make regionally targeted decisions*/

package Coursework;

import java.sql.*;

public class FifthReport {
    private final Connection c;

    public FifthReport(Connection c) {
        this.c = c;
    }

    public void showTopNCountriesInContinent(int n, String continent) {
        String sql =
                "SELECT country.Code, " +
                        "country.Name AS Country, " +
                        "country.Continent, " +
                        "country.Region, " +
                        "country.Population, " +
                        "city.Name AS Capital " +
                        "FROM country " +
                        "JOIN city ON city.ID = country.Capital " +
                        "WHERE country.Continent = ? " +
                        "ORDER BY country.Population DESC " +
                        "LIMIT ?;";

        try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, continent);
            pstmt.setInt(2, n);

            ResultSet rset = pstmt.executeQuery();

            StringBuilder md = new StringBuilder();
            md.append("# ").append(n)
                    .append(" top populated countries in ").append(continent).append("\n\n");
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
                    "5_FifthReport",
                    n + "_Top_Populated_Countries_In_" + continent.replace(" ", "_") + ".md",
                    md.toString()
            );

            System.out.println("Fifth report completed.");
        } catch (SQLException e) {
            System.out.println("Failed to retrieve the " + n +
                    " most populated countries in " + continent + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}