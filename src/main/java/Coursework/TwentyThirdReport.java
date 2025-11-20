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

            // Correct SQL: avoid double-counting by aggregating cities per country first
            String sql =
                    "SELECT c.Continent AS Continent, " +
                            "       SUM(c.Population) AS TotalPopulation, " +
                            "       SUM(COALESCE(cc.CityPopulation, 0)) AS CityPopulation " +
                            "FROM country c " +
                            "LEFT JOIN ( " +
                            "    SELECT CountryCode, SUM(Population) AS CityPopulation " +
                            "    FROM city " +
                            "    GROUP BY CountryCode " +
                            ") cc ON c.Code = cc.CountryCode " +
                            "GROUP BY c.Continent " +
                            "ORDER BY TotalPopulation DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            // Build markdown output
            StringBuilder md = new StringBuilder();
            md.append("# Population Summary for Each Continent\n\n");

            md.append("| Continent | Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not in Cities |\n");
            md.append("|-----------|------------------|-----------------------|-------------|---------------------------|-------------------|\n");

            while (rset.next()) {
                String continent = rset.getString("Continent");
                long totalPop = rset.getLong("TotalPopulation");
                long cityPop = rset.getLong("CityPopulation");
                long nonCityPop = totalPop - cityPop;

                double pctCity = (cityPop * 100.0) / totalPop;
                double pctNonCity = (nonCityPop * 100.0) / totalPop;

                md.append(String.format(
                        "| %s | %d | %d | %.2f%% | %d | %.2f%% |\n",
                        continent,
                        totalPop,
                        cityPop,
                        pctCity,
                        nonCityPop,
                        pctNonCity
                ));
            }

            // Write markdown file
            ReportManager.writeMarkdown(
                    "23_TwentyThirdReport",
                    "TwentyThirdReport.md",
                    md.toString()
            );

            System.out.println("Twenty-third report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate continent population summary: " + e.getMessage());
        }
    }
}
