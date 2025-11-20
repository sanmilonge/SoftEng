package Coursework;

import java.sql.*;

public class TwentySixthReport {
    private final Connection c;

    public TwentySixthReport(Connection c) {
        this.c = c;
    }

    public void showWorldPopulation() {
        try {
            Statement stmt = c.getConnection().createStatement();

            // Total world population
            String sqlTotal =
                    "SELECT SUM(Population) AS WorldPopulation " +
                            "FROM country;";

            // Total population living in cities
            String sqlCity =
                    "SELECT SUM(city.Population) AS CityPopulation " +
                            "FROM city;";

            ResultSet rsetTotal = stmt.executeQuery(sqlTotal);

            long totalPop = 0;
            if (rsetTotal.next()) {
                totalPop = rsetTotal.getLong("WorldPopulation");
            }

            ResultSet rsetCity = stmt.executeQuery(sqlCity);

            long cityPop = 0;
            if (rsetCity.next()) {
                cityPop = rsetCity.getLong("CityPopulation");
            }

            long nonCityPop = totalPop - cityPop;

            double pctCity = (cityPop * 100.0) / totalPop;
            double pctNonCity = (nonCityPop * 100.0) / totalPop;

            // Build markdown output
            StringBuilder md = new StringBuilder();
            md.append("# World Population Summary\n\n");

            md.append("| Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not in Cities |\n");
            md.append("|------------------|----------------------|-------------|---------------------------|-------------------|\n");

            md.append(String.format(
                    "| %d | %d | %.2f%% | %d | %.2f%% |\n",
                    totalPop, cityPop, pctCity, nonCityPop, pctNonCity
            ));

            ReportManager.writeMarkdown(
                    "26_TwentySixthReport",
                    "TwentySixthReport.md",
                    md.toString()
            );

            System.out.println("Twenty-sixth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate world population report: " + e.getMessage());
        }
    }
}
