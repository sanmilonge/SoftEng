package Coursework;

import java.sql.*;

public class ThirtyFirstReport {
    private final Connection c;

    public ThirtyFirstReport(Connection c) {
        this.c = c;
    }

    public void showPopulationOfAllCities() {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT city.Name AS CityName, " +
                            "       city.Population AS Population " +
                            "FROM city " +
                            "ORDER BY city.Population DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# Population Summary for Each City\n\n");

            md.append("| City Name | Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not In Cities |\n");
            md.append("|-----------|------------------|-----------------------|-------------|---------------------------|-------------------|\n");

            while (rset.next()) {
                String cityName = rset.getString("CityName");
                long totalPop = rset.getLong("Population");

                long cityPop = totalPop;  // Entire population is urban
                long nonCityPop = 0;

                double pctCity = 100.0;
                double pctNonCity = 0.0;

                md.append(String.format(
                        "| %s | %d | %d | %.2f%% | %d | %.2f%% |\n",
                        cityName,
                        totalPop,
                        cityPop,
                        pctCity,
                        nonCityPop,
                        pctNonCity
                ));
            }

            ReportManager.writeMarkdown(
                    "31_ThirtyFirstReport",
                    "ThirtyFirstReport.md",
                    md.toString()
            );

            System.out.println("Thirty-first report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate city population summary: " + e.getMessage());
        }
    }
}
