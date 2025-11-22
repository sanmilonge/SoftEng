/**As a policy maker, I want the system to produce a report of
 * the most populated cities in a country, based on a number I
 * specify, so that I can support national urban development*/


package Coursework;

import java.sql.*;

public class FifteenthReport {
    private final Connection c;

    public FifteenthReport(Connection c) {
        this.c = c;
    }

    public void showTopNCitiesInCountry(int n, String country) {
        String sql =
                "SELECT city.Name AS City, " +
                        "country.Name AS Country, " +
                        "city.District, " +
                        "city.Population " +
                        "FROM city " +
                        "JOIN country ON city.CountryCode = country.Code " +
                        "WHERE Country = ? " +
                        "ORDER BY city.Population DESC " +
                        "LIMIT ?;";

        try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, country);
            pstmt.setInt(2, n);

            ResultSet rset = pstmt.executeQuery();

            StringBuilder md = new StringBuilder();
            md.append("# Top ").append(n)
                    .append(" populated cities in ").append(country).append("\n\n");
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
                    "15_FifteenthReport",
                    n + "_Top_Populated_Cities_In_" + country + ".md",
                    md.toString()
            );

            System.out.println("Fifteenth report completed.");
        } catch (SQLException e) {
            System.out.println("Failed to retrieve the " + n +
                    " most populated cities in " + country + ":" + e.getMessage());
            e.printStackTrace();
        }
    }

}