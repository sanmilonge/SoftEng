package Coursework;

import java.sql.*;

public class NineteenthReport {
    private final Connection c;

    public NineteenthReport(Connection c) {
        this.c = c;
    }

    public void showCapitalCitiesForMultipleRegions() {
        String[] regions = {
                "Caribbean",
                "Western Europe",
                "Middle East",
                "Southern Africa"
        };

        try {
            Statement stmt = c.getConnection().createStatement();

            StringBuilder md = new StringBuilder();
            md.append("# Capital Cities for Multiple Regions (Population DESC)\n\n");

            for (String region : regions) {
                md.append("## Region: ").append(region).append("\n\n");

                md.append("| City ID | City Name | Country | Region | Population |\n");
                md.append("|---------|------------|----------|---------|-------------|\n");

                String sql =
                        "SELECT city.ID, city.Name, country.Name AS CountryName, country.Region, city.Population " +
                                "FROM city " +
                                "JOIN country ON city.ID = country.Capital " +
                                "WHERE country.Region = '" + region + "' " +
                                "ORDER BY city.Population DESC;";

                ResultSet rset = stmt.executeQuery(sql);

                while (rset.next()) {
                    md.append(String.format("| %d | %s | %s | %s | %d |\n",
                            rset.getInt("ID"),
                            rset.getString("Name"),
                            rset.getString("CountryName"),
                            rset.getString("Region"),
                            rset.getInt("Population")));
                }

                md.append("\n"); // space between regions
            }

            ReportManager.writeMarkdown("19_NineteenthReport",
                    "NineteenthReport.md",
                    md.toString());

            System.out.println("Combined region report (19th) completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve capital cities: " + e.getMessage());
        }
    }
}
