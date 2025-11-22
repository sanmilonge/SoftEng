/**
 * Basic 'get' SQL functions that reports can import
 */

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
    /** Returns list of continents in db */
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

    /** Returns list of regions in db */
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

    /** Returns list of countries in db */
    public List<String> getAllCountries() {
        List<String> countries = new ArrayList<>();
        try {
            String query = "SELECT Name AS Country FROM country;";
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

    /** Returns list of districts in db */
    public List<String> getAllDistricts() {
        List<String> districts = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT District FROM city;";
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

    /**
     * Returns total number of countries.
     *
     * Behaviour:
     *  - If region is provided (non-null/non-empty) → count countries in that region
     *  - Else if continent is provided → count countries in that continent
     *  - Else → count all countries in the world
     */
    public int totalNumberOfCountries(String continent, String region) {
        int total = 0;

        boolean hasRegion = region != null && !region.trim().isEmpty();
        boolean hasContinent = continent != null && !continent.trim().isEmpty();

        try {
            if (hasRegion) {
                // Filter by region
                String sql = "SELECT COUNT(*) AS Total FROM country WHERE Region = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, region);
                    try (ResultSet rset = pstmt.executeQuery()) {
                        if (rset.next()) {
                            total = rset.getInt("Total");
                        }
                    }
                }
            } else if (hasContinent) {
                // Filter by continent
                String sql = "SELECT COUNT(*) AS Total FROM country WHERE Continent = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, continent);
                    try (ResultSet rset = pstmt.executeQuery()) {
                        if (rset.next()) {
                            total = rset.getInt("Total");
                        }
                    }
                }
            } else {
                // No filters → whole world
                String sql = "SELECT COUNT(*) AS Total FROM country;";
                try (Statement stmt = c.getConnection().createStatement();
                     ResultSet rset = stmt.executeQuery(sql)) {
                    if (rset.next()) {
                        total = rset.getInt("Total");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to count countries: " + e.getMessage());
        }

        return total;
    }

    public int totalNumberOfCities(String continent, String region, String country, String district) {
        int total = 0;

        boolean hasRegion = region != null && !region.trim().isEmpty();
        boolean hasContinent = continent != null && !continent.trim().isEmpty();
        boolean hasCountry = country != null && !country.trim().isEmpty();
        boolean hasDistrict = district != null && !district.trim().isEmpty();

        try {
            if (hasRegion) {
                // Filter by region
                String sql = "SELECT COUNT(*) AS Total " +
                        "FROM city JOIN country ON city.CountryCode = country.Code " +
                        "WHERE country.Region = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, region);
                    try (ResultSet rset = pstmt.executeQuery()) {
                        if (rset.next()) {
                            total = rset.getInt("Total");
                        }
                    }
                }
            } else if (hasContinent) {
                // Filter by continent
                String sql = "SELECT COUNT(*) AS Total " +
                        "FROM city JOIN country ON city.CountryCode = country.Code " +
                        "WHERE country.Continent = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, continent);
                    try (ResultSet rset = pstmt.executeQuery()) {
                        if (rset.next()) {
                            total = rset.getInt("Total");
                        }
                    }
                }
            } else if (hasCountry){
                // Filter by country
                String sql = "SELECT COUNT(*) AS Total " +
                        "FROM city JOIN country ON city.CountryCode = country.Code " +
                        "WHERE country.Name = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, continent);
                    try (ResultSet rset = pstmt.executeQuery()) {
                        if (rset.next()) {
                            total = rset.getInt("Total");
                        }
                    }
                }
            }
            else if (hasDistrict){
                // Filter by country
                String sql = "SELECT COUNT(*) AS Total FROM city WHERE District = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, continent);
                    try (ResultSet rset = pstmt.executeQuery()) {
                        if (rset.next()) {
                            total = rset.getInt("Total");
                        }
                    }
                }
            }
            else {
                // No filters → whole world
                String sql = "SELECT COUNT(*) AS Total FROM city;";
                try (Statement stmt = c.getConnection().createStatement();
                     ResultSet rset = stmt.executeQuery(sql)) {
                    if (rset.next()) {
                        total = rset.getInt("Total");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to count countries: " + e.getMessage());
        }

        return total;
    }

}