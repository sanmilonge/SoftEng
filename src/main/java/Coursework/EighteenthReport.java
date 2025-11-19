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
                    "SELECT city.ID, city.Name, country.Name AS CountryName, country.Continent, city.Population " +
                            "FROM city " +
                            "JOIN country ON city.ID = country.Capital " +
                            "WHERE country.Continent = '" + continent + "' " +
                            "ORDER BY city.Population DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            // Build Markdown output
            StringBuilder md = new StringBuilder();
            md.append("# All the capital cities in ")
                    .append(continent)
                    .append(" organised by largest population to smallest.\n\n");

            md.append("| City ID | City Name | Country | Continent | Population |\n");
            md.append("|---------|------------|----------|------------|-------------|\n");

            while (rset.next()) {
                md.append(String.format("| %d | %s | %s | %s | %d |\n",
                        rset.getInt("ID"),
                        rset.getString("Name"),
                        rset.getString("CountryName"),
                        rset.getString("Continent"),
                        rset.getInt("Population")));
            }

            ReportManager.writeMarkdown("18_EighteenthReport",
                    "EighteenthReport.md",
                    md.toString());

            System.out.println("Eighteenth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve capital cities: " + e.getMessage());
        }
    }
}
