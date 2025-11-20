package Coursework;

import java.sql.*;

public class TwentyEighthReport {
    private final Connection c;

    public TwentyEighthReport(Connection c) {
        this.c = c;
    }

    public void showPopulationOfAllRegions() {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT country.Region AS Region, " +
                            "       SUM(country.Population) AS TotalPopulation, " +
                            "       SUM(city.Population) AS CityPopulation " +
                            "FROM country " +
                            "LEFT JOIN city ON country.Code = city.CountryCode " +
                            "GROUP BY country.Region " +
                            "ORDER BY TotalPopulation DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# Population of Each Region\n\n");

            md.append("| Region | Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not In Cities |\n");
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
                    "28_TwentyEighthReport",
                    "TwentyEighthReport.md",
                    md.toString()
            );

            System.out.println("Twenty-eighth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate region population report: " + e.getMessage());
        }
    }
}
