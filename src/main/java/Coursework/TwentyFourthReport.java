package Coursework;

import java.sql.*;

public class TwentyFourthReport {
    private final Connection c;

    public TwentyFourthReport(Connection c) {
        this.c = c;
    }

    public void showRegionPopulationSummary() {
        try {
            Statement stmt = c.getConnection().createStatement();

            // Correct SQL to avoid double-counting
            String sql =
                    "SELECT c.Region AS Region, " +
                            "       SUM(c.Population) AS TotalPopulation, " +
                            "       SUM(COALESCE(cc.CityPopulation, 0)) AS CityPopulation " +
                            "FROM country c " +
                            "LEFT JOIN ( " +
                            "    SELECT CountryCode, SUM(Population) AS CityPopulation " +
                            "    FROM city " +
                            "    GROUP BY CountryCode " +
                            ") cc ON c.Code = cc.CountryCode " +
                            "GROUP BY c.Region " +
                            "ORDER BY TotalPopulation DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            // Build markdown
            StringBuilder md = new StringBuilder();
            md.append("# Population Summary for Each Region\n\n");

            md.append("| Region | Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not in Cities |\n");
            md.append("|--------|------------------|-----------------------|-------------|---------------------------|-------------------|\n");

            while (rset.next()) {
                String region = rset.getString("Region");
                long totalPop = rset.getLong("TotalPopulation");
                long cityPop = rset.getLong("CityPopulation");
                long nonCityPop = totalPop - cityPop;

                double pctCity = (cityPop * 100.0) / totalPop;
                double pctNonCity = (nonCityPop * 100.0) / totalPop;

                md.append(String.format(
                        "| %s | %d | %d | %.2f%% | %d | %.2f%% |\n",
                        region,
                        totalPop,
                        cityPop,
                        pctCity,
                        nonCityPop,
                        pctNonCity
                ));
            }

            ReportManager.writeMarkdown(
                    "24_TwentyFourthReport",
                    "TwentyFourthReport.md",
                    md.toString()
            );

            System.out.println("Twenty-fourth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate region population summary: " + e.getMessage());
        }
    }
}
