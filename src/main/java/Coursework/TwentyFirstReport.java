package Coursework;

import java.sql.*;

public class TwentyFirstReport {
    private final Connection c;

    public TwentyFirstReport(Connection c) {
        this.c = c;
    }

    public void showTopNCapitalCitiesInContinent(String continent, int n) {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT city.Name AS CityName, " +
                            "       country.Name AS CountryName, " +
                            "       city.Population AS Population " +
                            "FROM city " +
                            "JOIN country ON city.ID = country.Capital " +
                            "WHERE country.Continent = '" + continent + "' " +
                            "ORDER BY city.Population DESC " +
                            "LIMIT " + n + ";";

            ResultSet rset = stmt.executeQuery(sql);

            // Markdown output
            StringBuilder md = new StringBuilder();
            md.append("# Top ").append(n)
                    .append(" Populated Capital Cities in ")
                    .append(continent)
                    .append("\n\n");

            md.append("| City Name | Country | Population |\n");
            md.append("|-----------|----------|-------------|\n");

            while (rset.next()) {
                md.append(String.format("| %s | %s | %d |\n",
                        rset.getString("CityName"),
                        rset.getString("CountryName"),
                        rset.getInt("Population")));
            }

            ReportManager.writeMarkdown(
                    "21_TwentyFirstReport",
                    "TwentyFirstReport.md",
                    md.toString()
            );

            System.out.println("Twenty-first report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve top N capital cities in a continent: " + e.getMessage());
        }
    }
}
