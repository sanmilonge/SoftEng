package Coursework;

import java.sql.*;

public class FirstReport{
    private final Connection c;

    public FirstReport (Connection c){
        this.c = c;
    }
  /**
     * Query: show all the countries in the world ordered by population
     * (largest to smallest).
     */
    public void showCountriesByPopulation() {
        try {
            ResultSet rset;
            try (Statement stmt = c.getConnection().createStatement()) {
                String sql =
                        "SELECT Code, Name, Continent, Region, Population " +
                                "FROM country " +
                                "ORDER BY Population DESC;";

                rset = stmt.executeQuery(sql);
            }

            System.out.println("\nAll countries in the world (largest to smallest population):\n");
            System.out.printf("%-5s %-45s %-20s %-25s %-15s%n",
                    "Code", "Name", "Continent", "Region", "Population");
            System.out.println("=".repeat(115));

            while (rset.next()) {
                System.out.printf("%-5s %-45s %-20s %-25s %-15d%n",
                        rset.getString("Code"),
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population"));
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve countries: " + e.getMessage());
        }
    }}
