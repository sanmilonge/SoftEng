/**
 * As a policy maker, I want the system to produce a report of the most
 * populated countries in the world, based on a number I specify, so that
 * I can focus analysis on key global nations*/

package Coursework;

import java.sql.*;

public class FourthReport {
    private final Connection c;

    public FourthReport(Connection c) {
        this.c = c;
    }

    public void showTopNCountries(int n) {
        try {
            Statement stmt = c.getConnection().createStatement();

            String sql = """
    SELECT country.Code,
           country.Name AS Country,
           country.Continent,
           country.Region,
           country.Population,
           city.Name AS Capital
    FROM country
    JOIN city ON city.ID = country.Capital
    ORDER BY country.Population DESC
    LIMIT %d
    """.formatted(n);


            ResultSet rset = stmt.executeQuery(sql);

            StringBuilder md = new StringBuilder();
            md.append("# ").append(n).append(" top populated countries in the world").append("\n\n");
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
                    "4_FourthReport",
                    n + "_Top_Populated_Countries_In_The_World.md",
                    md.toString()
            );

            System.out.println("Fourth report completed.");

        } catch (Exception e) {
            System.out.println("Failed to retrieve the " + n + " most populated countries: " + e.getMessage());
        }
    }
}
