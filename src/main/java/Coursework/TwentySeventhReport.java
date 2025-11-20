package Coursework;

import java.sql.*;

public class TwentySeventhReport {
    private final Connection c;

    public TwentySeventhReport(Connection c) {
        this.c = c;
    }

    public void showPopulationOfAllContinents() {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT country.Continent AS Continent, " +
                            "       SUM(country.Population) AS TotalPopulation, " +
                            "       SUM(city.Population) AS CityPopulation " +
                            "FROM country " +
                            "LEFT JOIN city ON country.Code = city.CountryCode " +
                            "GROUP BY country.Continent " +
                            "ORDER BY TotalPopulation DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# Population of Each Continent\n\n");

            md.append("| Continent | Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not In Cities |\n");
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

            ReportManager.writeMarkdown(
                    "27_TwentySeventhReport",
                    "TwentySeventhReport.md",
                    md.toString()
            );

            System.out.println("Twenty-seventh report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate continent population report: " + e.getMessage());
        }
    }
}
