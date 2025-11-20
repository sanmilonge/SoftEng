package Coursework;

import java.sql.*;

public class TwentiethReport {
    private final Connection c;

    public TwentiethReport(Connection c) {
        this.c = c;
    }

    public void showTopNCapitalCities(int n) {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT city.Name AS CityName, " +
                            "       country.Name AS CountryName, " +
                            "       city.Population AS Population " +
                            "FROM city " +
                            "JOIN country ON city.ID = country.Capital " +
                            "ORDER BY city.Population DESC " +
                            "LIMIT " + n + ";";

            ResultSet rset = stmt.executeQuery(sql);

            // Markdown output
            StringBuilder md = new StringBuilder();
            md.append("# Top ").append(n)
                    .append(" Populated Capital Cities in the World\n\n");

            md.append("| City Name | Country | Population |\n");
            md.append("|-----------|----------|-------------|\n");

            while (rset.next()) {
                md.append(String.format("| %s | %s | %d |\n",
                        rset.getString("CityName"),
                        rset.getString("CountryName"),
                        rset.getInt("Population")));
            }

            ReportManager.writeMarkdown(
                    "20_TwentiethReport",
                    "TwentiethReport.md",
                    md.toString()
            );

            System.out.println("Twentieth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve top N capital cities: " + e.getMessage());
        }
    }
}
