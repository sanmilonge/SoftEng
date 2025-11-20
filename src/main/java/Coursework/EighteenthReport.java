package Coursework;

import java.sql.*;

public class EighteenthReport {
    private final Connection c;

    public EighteenthReport(Connection c) {
        this.c = c;
    }

    public void showCapitalCitiesInMultipleContinents() {

        String[] continents = {
                "Asia",
                "Europe",
                "Africa",
                "North America",
                "South America",
                "Oceania"
        };

        try {
            Statement stmt = c.getConnection().createStatement();

            StringBuilder md = new StringBuilder();
            md.append("# Capital Cities for Multiple Continents (Population DESC)\n\n");

            for (String continent : continents) {

                md.append("## Continent: ").append(continent).append("\n\n");

                md.append("| City Name | Country | Population |\n");
                md.append("|-----------|----------|-------------|\n");

                String sql =
                        "SELECT city.Name AS CityName, " +
                                "       country.Name AS CountryName, " +
                                "       city.Population AS Population " +
                                "FROM city " +
                                "JOIN country ON city.ID = country.Capital " +
                                "WHERE country.Continent = '" + continent + "' " +
                                "ORDER BY city.Population DESC;";

                ResultSet rset = stmt.executeQuery(sql);

                while (rset.next()) {
                    md.append(String.format("| %s | %s | %d |\n",
                            rset.getString("CityName"),
                            rset.getString("CountryName"),
                            rset.getInt("Population")));
                }

                md.append("\n"); // space between continent sections
            }

            ReportManager.writeMarkdown(
                    "18_EighteenthReport",
                    "EighteenthReport.md",
                    md.toString()
            );

            System.out.println("Eighteenth report completed (multiple continents).");

        } catch (Exception e) {
            System.out.println("Failed to retrieve capital cities: " + e.getMessage());
        }
    }
}
