package Coursework;

import java.sql.ResultSet;
import java.sql.Statement;


/**
 * Query: show all the countries in the world ordered by population
 * (largest to smallest).
 */

public class FirstReport{
    App app=new App();
public void showCountriesByPopulation() {
    try {
        Statement stmt = app.con.createStatement();
        String sql =
                "SELECT Code, Name, Continent, Region, Population " +
                        "FROM country " +
                        "ORDER BY Population DESC;";

        ResultSet rset = stmt.executeQuery(sql);

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