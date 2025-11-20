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
                            "       country.Name AS CountryName, " +
                            "       city.District AS District, " +
                            "       city.Population AS Population " +
                            "FROM city " +
                            "JOIN country ON city.CountryCode = country.Code " +
                            "ORDER BY city.Population DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# Population Summary for Each City\n\n");

            md.append("| City Name | Country | District | Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not In Cities |\n");
            md.append("|-----------|----------|-----------|------------------|-----------------------|-------------|---------------------------|-------------------|\n");

            while (rset.next()) {
                String cityName = rset.getString("CityName");
                String countryName = rset.getString("CountryName");
                String district = rset.getString("District");
                long totalPop = rset.getLong("Population");

                long cityPop = totalPop;     // entire population of a city is urban
                long nonCityPop = 0;

                double pctCity = 100.0;
                double pctNonCity = 0.0;

                md.append(String.format(
                        "| %s | %s | %s | %d | %d | %.2f%% | %d | %.2f%% |\n",
                        cityName,
                        countryName,
                        district,
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
