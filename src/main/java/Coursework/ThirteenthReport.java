/**
 As a policy maker, I want the system to produce a report of the most populated
 cities in a continent, based on a number I specify, so that I can plan continent-wide
 urban strategies */


package Coursework;

import java.sql.*;

public class ThirteenthReport {
    private final Connection c;

    public ThirteenthReport(Connection c) {
        this.c = c;
    }

    public void showTopNCitiesInContinent(int n, String continent) {
        String sql =
                "SELECT city.Name AS City, " +
                        "country.Name AS Country, " +
                        "city.District, " +
                        "city.Population " +
                        "FROM city " +
                        "JOIN country ON city.CountryCode = country.Code " +
                        "WHERE country.Continent = ? " +
                        "ORDER BY city.Population DESC " +
                        "LIMIT ?;";

        try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, continent);
            pstmt.setInt(2, n);

            ResultSet rset = pstmt.executeQuery();

            StringBuilder md = new StringBuilder();
            md.append("# Top ").append(n)
                    .append(" populated cities in ").append(continent).append("\n\n");
            md.append("| City | Country | District | Population |\n");
            md.append("|------|---------|-----------|--------|\n");

            while (rset.next()) {
                md.append(String.format(
                        "| %s | %s | %s | %d |\n",
                        rset.getString("City"),
                        rset.getString("Country"),
                        rset.getString("District"),
                        rset.getInt("Population")));}

            ReportManager.writeMarkdown(
                    "13_ThirteenthReport",
                    n + "_Top_Populated_Cities_In_" +continent+ ".md",
                    md.toString()
            );

            System.out.println("Thirteenth report completed.");
        } catch (SQLException e) {
            System.out.println("Failed to retrieve the " + n +
                    " most populated cities in " + continent + ":" + e.getMessage());
            e.printStackTrace();
        }
    }

}