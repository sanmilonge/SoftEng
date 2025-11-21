package Coursework;

import java.sql.*;

public class TwentyNinthReport {
    private final Connection c;

    public TwentyNinthReport(Connection c) {
        this.c = c;
    }

    public void showPopulationOfAllCountries() {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT country.Name AS CountryName, " +
                            "       country.Population AS TotalPopulation, " +
                            "       IFNULL(SUM(city.Population), 0) AS CityPopulation " +
                            "FROM country " +
                            "LEFT JOIN city ON country.Code = city.CountryCode " +
                            "GROUP BY country.Code, country.Name, country.Population " +
                            "ORDER BY TotalPopulation DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("The population of a country\n\n");

            md.append("| Country | Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not In Cities |\n");
            md.append("|---------|------------------|-----------------------|-------------|---------------------------|-------------------|\n");

            while (rset.next()) {
                String name = rset.getString("CountryName");
                long totalPop = rset.getLong("TotalPopulation");
                long cityPop = rset.getLong("CityPopulation");
                long nonCityPop = totalPop - cityPop;

                double pctCity = (cityPop * 100.0) / totalPop;
                double pctNonCity = (nonCityPop * 100.0) / totalPop;

                md.append(String.format(
                        "| %s | %d | %d | %.2f%% | %d | %.2f%% |\n",
                        name,
                        totalPop,
                        cityPop,
                        pctCity,
                        nonCityPop,
                        pctNonCity
                ));
            }

            ReportManager.writeMarkdown(
                    "29_TwentyNinthReport",
                    "TwentyNinthReport.md",
                    md.toString()
            );

            System.out.println("Twenty-ninth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate country population report: " + e.getMessage());
        }
    }
}
