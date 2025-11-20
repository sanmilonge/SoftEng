package Coursework;

import java.sql.*;

public class EighteenthReport {
    private final Connection c;

    public EighteenthReport(Connection c) {
        this.c = c;
    }

    public void showCapitalCitiesInContinent(String continent) {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT city.Name AS CityName, " +
                            "       country.Name AS CountryName, " +
                            "       city.District AS District, " +
                            "       city.Population AS Population " +
                            "FROM city " +
                            "JOIN country ON city.ID = country.Capital " +
                            "WHERE country.Continent = '" + continent + "' " +
                            "ORDER BY city.Population DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            // Markdown output
            StringBuilder md = new StringBuilder();
            md.append("# Capital Cities in ").append(continent)
                    .append(" (Largest to Smallest Population)\n\n");

            md.append("| City Name | Country | District | Population |\n");
            md.append("|-----------|----------|-----------|-------------|\n");

            while (rset.next()) {
                md.append(String.format("| %s | %s | %s | %d |\n",
                        rset.getString("CityName"),
                        rset.getString("CountryName"),
                        rset.getString("District"),
                        rset.getInt("Population")));
            }

            ReportManager.writeMarkdown(
                    "18_EighteenthReport",
                    "EighteenthReport.md",
                    md.toString()
            );

            System.out.println("Eighteenth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve capital cities: " + e.getMessage());
        }
    }
}
