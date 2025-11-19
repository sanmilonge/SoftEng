package Coursework;

import java.sql.*;

public class TwentyThirdReport {
    private final Connection c;

    public TwentyThirdReport(Connection c) {
        this.c = c;
    }

    public void showContinentPopulationSummary() {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT " +
                            "    country.Continent AS Continent, " +
                            "    SUM(country.Population) AS TotalPopulation, " +
                            "    SUM(city.Population) AS CityPopulation, " +
                            "    (SUM(country.Population) - SUM(city.Population)) AS NonCityPopulation " +
                            "FROM country " +
                            "LEFT JOIN city ON country.Code = city.CountryCode " +
                            "GROUP BY country.Continent " +
                            "ORDER BY TotalPopulation DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# Population Summary for Each Continent\n\n");
            md.append("| Continent | Total Population | Population in Cities | Population Not in Cities |\n");
            md.append("|-----------|------------------|-----------------------|----------------------------|\n");

            while (rset.next()) {
                md.append(String.format("| %s | %d | %d | %d |\n",
                        rset.getString("Continent"),
                        rset.getLong("TotalPopulation"),
                        rset.getLong("CityPopulation"),
                        rset.getLong("NonCityPopulation")));
            }

            ReportManager.writeMarkdown("23_TwentyThirdReport",
                    "TwentyThirdReport.md",
                    md.toString());

            System.out.println("Twenty-third report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate continent population summary: " + e.getMessage());
        }
    }
}
