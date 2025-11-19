package Coursework;

import java.sql.*;

public class TwentySecondReport {
    private final Connection c;

    public TwentySecondReport(Connection c) {
        this.c = c;
    }

    public void showTopNCapitalCitiesInRegion(String region, int n) {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT city.ID, city.Name, country.Name AS CountryName, country.Region, city.Population " +
                            "FROM city " +
                            "JOIN country ON city.ID = country.Capital " +
                            "WHERE country.Region = '" + region + "' " +
                            "ORDER BY city.Population DESC " +
                            "LIMIT " + n + ";";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# Top ").append(n)
                    .append(" Populated Capital Cities in Region: ")
                    .append(region)
                    .append("\n\n");

            md.append("| City ID | City Name | Country | Region | Population |\n");
            md.append("|---------|------------|----------|---------|-------------|\n");

            while (rset.next()) {
                md.append(String.format("| %d | %s | %s | %s | %d |\n",
                        rset.getInt("ID"),
                        rset.getString("Name"),
                        rset.getString("CountryName"),
                        rset.getString("Region"),
                        rset.getInt("Population")));
            }

            ReportManager.writeMarkdown("22_TwentySecondReport",
                    "TwentySecondReport.md",
                    md.toString());

            System.out.println("Twenty-second report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve top N capital cities in region: " + e.getMessage());
        }
    }
}
