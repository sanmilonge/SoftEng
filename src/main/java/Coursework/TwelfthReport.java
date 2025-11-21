/**
 * As a policy maker, I want the system to produce a report of the most
 * populated cities in the world, based on a number I specify, so that I
 * can identify major global population centers*/


package Coursework;

import java.sql.*;

public class TwelfthReport {
    private final Connection c;

    public TwelfthReport(Connection c) {
        this.c = c;
    }

    public void showTopNCitiesITheWorld(int n) {
        String sql =
                "SELECT city.Name AS City, " +
                        "country.Name AS Country, " +
                        "city.District, " +
                        "city.Population " +
                        "FROM city " +
                        "JOIN country ON city.CountryCode = country.Code " +
                        "ORDER BY city.Population DESC " +
                        "LIMIT ?;";

        try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, n);

            ResultSet rset = pstmt.executeQuery();

            StringBuilder md = new StringBuilder();
            md.append("# Top ").append(n)
                    .append(" populated cities in the world").append("\n\n");
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
                    "12_TwelfthReport",
                    n + "_Top_Populated_Cities_In_World" + ".md",
                    md.toString()
            );

            System.out.println("Twelfth report completed.");
        } catch (SQLException e) {
            System.out.println("Failed to retrieve the " + n +
                    " most populated cities in the world: " + e.getMessage());
            e.printStackTrace();
        }
    }

}