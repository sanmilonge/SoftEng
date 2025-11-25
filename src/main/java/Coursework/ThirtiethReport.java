package Coursework;

import java.sql.*;

public class ThirtiethReport {
    private final Connection c;

    public ThirtiethReport(Connection c) {
        this.c = c;
    }

    public void showPopulationOfAllDistricts() {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT city.District AS District, " +
                            "       SUM(city.Population) AS TotalPopulation " +
                            "FROM city " +
                            "GROUP BY city.District " +
                            "ORDER BY TotalPopulation DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("The population of a district\n\n");

            md.append("| District | Total Population | Population in Cities | % In Cities | Population Not in Cities | % Not In Cities |\n");
            md.append("|----------|------------------|-----------------------|-------------|---------------------------|-------------------|\n");

            while (rset.next()) {
                String district = rset.getString("District");
                long totalPop = rset.getLong("TotalPopulation");

                long cityPop = totalPop;       // districts only contain cities
                long nonCityPop = 0;

                double pctCity = 100.0;
                double pctNonCity = 0.0;

                md.append(String.format(
                        "| %s | %d | %d | %.2f%% | %d | %.2f%% |\n",
                        district,
                        totalPop,
                        cityPop,
                        pctCity,
                        nonCityPop,
                        pctNonCity
                ));
            }

            ReportManager.writeMarkdown(
                    "30_ThirtiethReport",
                    "ThirtiethReport.md",
                    md.toString()
            );

            System.out.println("Thirtieth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate district population report: " + e.getMessage());
        }
    }
}
