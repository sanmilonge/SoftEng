/**As a policy maker, I want the system to produce a report of the
 * most populated cities in a district, based on a number I specify,
 * so that I can manage local infrastructure planning*/


package Coursework;

import java.sql.*;

public class SixteenthReport {
    private final Connection c;

    public SixteenthReport(Connection c) {
        this.c = c;
    }

    public void showTopNCitiesInDistrict(int n, String district) {
        String sql =
                "SELECT city.Name AS City, " +
                        "country.Name AS Country, " +
                        "city.District, " +
                        "city.Population " +
                        "FROM city " +
                        "JOIN country ON city.CountryCode = country.Code " +
                        "WHERE District = ? " +
                        "ORDER BY city.Population DESC " +
                        "LIMIT ?;";

        try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, district);
            pstmt.setInt(2, n);

            ResultSet rset = pstmt.executeQuery();

            StringBuilder md = new StringBuilder();
            md.append("# Top ").append(n)
                    .append(" populated cities in ").append(district).append("\n\n");
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
                    "16_SixteenthReport",
                    n + "_Top_Populated_Cities_In_" + district + ".md",
                    md.toString()
            );

            System.out.println("Sixteenth report completed.");
        } catch (SQLException e) {
            System.out.println("Failed to retrieve the " + n +
                    " most populated cities in " + district + ":" + e.getMessage());
            e.printStackTrace();
        }
    }

}