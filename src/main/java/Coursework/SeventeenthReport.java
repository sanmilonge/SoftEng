package Coursework;

import java.sql.*;

public class SeventeenthReport {
    private final Connection c;

    public SeventeenthReport(Connection c) {
        this.c = c;
    }

    public void showCapitalCitiesInWorld() {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql =
                    "SELECT city.Name AS CityName, " +
                            "       country.Name AS CountryName, " +
                            "       city.District AS District, " +
                            "       city.Population AS Population " +
                            "FROM city " +
                            "JOIN country ON city.ID = country.Capital " +
                            "ORDER BY city.Population DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# All Capital Cities in the World by Population\n\n");
            md.append("| City Name | Country | District | Population |\n");
            md.append("|-----------|----------|-----------|-------------|\n");

            while (rset.next()) {
                md.append(String.format("| %s | %s | %s | %d |\n",
                        rset.getString("CityName"),
                        rset.getString("CountryName"),
                        rset.getString("District"),
                        rset.getInt("Population")));
            }

            ReportManager.writeMarkdown(
                    "17_SeventeenthReport",
                    "SeventeenthReport.md",
                    md.toString()
            );

            System.out.println("Seventeenth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve capital cities: " + e.getMessage());
        }
    }
}
