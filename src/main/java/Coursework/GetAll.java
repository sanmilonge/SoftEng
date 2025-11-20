/**
 * Basic 'get' SQL functions that reports can import*/


package Coursework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetAll {

    private final Connection c;

    // Constructor receives the connection used by reports
    public GetAll(Connection c) {
        this.c = c;
    }

    public List<String> getAllContinents() {
        List<String> continents = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT Continent FROM country;";
            Statement stmt = c.getConnection().createStatement();
            ResultSet rslt = stmt.executeQuery(query);

            while (rslt.next()) {
                String continent = rslt.getString("Continent");
                if (continent != null && !continent.isEmpty()) {
                    continents.add(continent.trim());
                }
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println("Failed to retrieve continents: " + e.getMessage());
        }
        return continents;
    }

    public List<String> getAllRegions() {
        List<String> regions = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT Region FROM country;";
            Statement stmt = c.getConnection().createStatement();
            ResultSet rslt = stmt.executeQuery(query);

            while (rslt.next()) {
                String region = rslt.getString("Region");
                if (region != null && !region.isEmpty()) {
                    regions.add(region.trim());
                }
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println("Failed to retrieve regions: " + e.getMessage());
        }
        return regions;
    }
    public List<String> getAllCountries() {
        List<String> countries = new ArrayList<>();
        try {
            String query = "SELECT Name AS Country  FROM country;";
            Statement stmt = c.getConnection().createStatement();
            ResultSet rslt = stmt.executeQuery(query);

            while (rslt.next()) {
                String country = rslt.getString("Country");
                if (country != null && !country.isEmpty()) {
                    countries.add(country.trim());
                }
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println("Failed to retrieve countries: " + e.getMessage());
        }
        return countries;
    }

    public List<String> getAllDistricts() {
        List<String> districts = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT District  FROM city;";
            Statement stmt = c.getConnection().createStatement();
            ResultSet rslt = stmt.executeQuery(query);

            while (rslt.next()) {
                String district = rslt.getString("District");
                if (district != null && !district.isEmpty()) {
                    districts.add(district.trim());
                }
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println("Failed to retrieve districts: " + e.getMessage());
        }
        return districts;
    }
}
