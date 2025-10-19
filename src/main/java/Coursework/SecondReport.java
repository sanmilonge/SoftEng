/**
 * As a data analyst, I want the system to produce a report of all
 * cities in the world ordered by population from largest to smallest
 * so that I can understand global urbanization patterns.
 */

package Coursework;

import java.sql.*;

public class SecondReport{
    private final Connection c;

    public SecondReport (Connection c){
        this.c = c;
    }
    /**
     * Query: show all the cities in the world ordered by population
     * (largest to smallest).
     */
    public void showCitiesByPopulation() {
        try {
            ResultSet rset;
            Statement stmt = c.getConnection().createStatement();
            String sql = """
            SELECT city.Name AS City,
                   country.Name AS Country,
                   city.District,
                   city.Population
            FROM city
            JOIN country ON city.CountryCode = country.Code
            ORDER BY city.Population DESC;
        """;


            rset = stmt.executeQuery(sql);


            System.out.println("\nAll cities in the world (largest to smallest population):\n");
            System.out.printf("%-45s %-40s %-40s %-15s%n",
                    "City", "Country", "District", "Population");
            System.out.println("=".repeat(150));

            while (rset.next()) {
                System.out.printf("%-45s %-40s %-25s %-15d%n",
                        rset.getString("City"),
                        rset.getString("Country"),
                        rset.getString("District"),
                        rset.getInt("Population"));
            }// ID Name CountryCode District Population

        } catch (Exception e) {
            System.out.println("Failed to retrieve cities: " + e.getMessage());
        }
    }}
