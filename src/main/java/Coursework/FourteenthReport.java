/**
 * As a policy maker, I want the system to produce a report of the
 * most populated cities in a region, based on a number I specify,
 * so that I can allocate funding to major regional hubs*/


package Coursework;

import java.sql.*;

public class FourteenthReport {
    private final Connection c;

    public FourteenthReport(Connection c) {
        this.c = c;
    }

    public void showTopNCitiesInRegion(int n, String region) {
        String sql =
                "SELECT city.Name AS City, " +
                        "country.Name AS Country, " +
                        "city.District, " +
                        "city.Population " +
                        "FROM city " +
                        "JOIN country ON city.CountryCode = country.Code " +
                        "WHERE country.Region = ? " +
                        "ORDER BY city.Population DESC " +
                        "LIMIT ?;";

        try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, region);
            pstmt.setInt(2, n);

            ResultSet rset = pstmt.executeQuery();

            StringBuilder md = new StringBuilder();
            md.append("# Top ").append(n)
                    .append(" populated cities in ").append(region).append("\n\n");
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
                    "14_FourteenthReport",
                    n + "_Top_Populated_Cities_In_" + region + ".md",
                    md.toString()
            );

            System.out.println("Fourteenth report completed.");
        } catch (SQLException e) {
            System.out.println("Failed to retrieve the " + n +
                    " most populated cities in " + region + ":" + e.getMessage());
            e.printStackTrace();
        }
    }

}