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
                    "SELECT country.Code AS Code, " +
                            "       country.Name AS CountryName, " +
                            "       country.Continent AS Continent, " +
                            "       country.Region AS Region, " +
                            "       country.Population AS TotalPopulation, " +
                            "       IFNULL(SUM(city.Population), 0) AS CityPopulation " +
                            "FROM country " +
                            "LEFT JOIN city ON country.Code = city.CountryCode " +
                            "GROUP BY country.Code, country.Name, country.Continent, country.Region, country.Population " +
                            "ORDER BY TotalPopulation DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# Population of Each Country\n\n");
            md.append("| Country Code | Country | Continent | Region | Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not In Cities |\n");
            md.append("|--------------|----------|-----------|--------|------------------|-----------------------|-------------|---------------------------|-------------------|\n");

            while (rset.next()) {
                String code = rset.getString("Code");
                String name = rset.getString("CountryName");
                String continent = rset.getString("Continent");
                String region = rset.getString("Region");
                long totalPop = rset.getLong("TotalPopulation");
                long cityPop = rset.getLong("CityPopulation");
                long nonCityPop = totalPop - cityPop;

                double pctCity = (cityPop * 100.0) / totalPop;
                double pctNonCity = (nonCityPop * 100.0) / totalPop;

                md.append(String.format(
                        "| %s | %s | %s | %s | %d | %d | %.2f%% | %d | %.2f%% |\n",
                        code,
                        name,
                        continent,
                        region,
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
