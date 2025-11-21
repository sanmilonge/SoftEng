/**As a policy maker, I want the system to produce a report of the
 * most populated countries in a continent, based on a number I
 * specify, so that I can prioritize policy efforts within that continent*/



package Coursework;

import java.sql.*;

public class FifthReport {
    private final Connection c;

    public FifthReport(Connection c) {
        this.c = c;
    }

    public void showTopNCountriesInContinent(int n, String continent) {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql = "SELECT country.Code, " +
                                "country.Name AS Country, " +
                                "country.Continent, " +
                                "country.Region, " +
                                "country.Population, " +
                                "city.Name AS Capital " +
                        "FROM country " +
                       "JOIN city ON city.ID = country.Capital " +
                       "WHERE Region = " + continent +
                      " ORDER BY country.Population DESC " +
                       "LIMIT " + n + ";";


            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# ").append(n).append(" top populated countries in ").append(continent).append("\n\n");
            md.append("| Code | Country | Continent | Region | Population | Capital |\n");
            md.append("|------|------|------------|------------|-------------|------------|\n");

            while (rset.next()) {
                md.append(String.format("| %s | %s | %s | %s | %d | %s |\n",
                        rset.getString("Code"),
                        rset.getString("Country"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population"),
                        rset.getString("Capital")));
            }


            ReportManager.writeMarkdown(
                    "5_FifthReport",
                    n + "_Top_Populated_Countries_In_" + continent + ".md",
                    md.toString()
            );

            System.out.println("Fifth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve the " + n + " most populated countries in " + continent + e.getMessage());
        }
    }
}
