/**
 * Helper class providing reusable SQL queries for reports.
 * Each method retrieves data from the world database using
 * the shared Coursework.Connection wrapper.
 */

package Coursework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public record GetAll(Connection c) {

    /**
     * Returns list of ALL continents from the database.
     * Uses SELECT DISTINCT since continents repeat across rows.
     */
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

    /**
     * Returns list of ALL regions.
     */
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

    /**
     * Returns list of ALL country names.
     */
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

    /**
     * Returns list of ALL districts from the city table.
     */
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
     * Counts countries optionally filtered by:
     * - continent
     * - region
     * <p>
     * If both null → count ALL countries.
     */
    public int totalNumberOfCountries(String continent, String region) {
        int total = 0;

        boolean hasRegion = region != null && !region.trim().isEmpty();
        boolean hasContinent = continent != null && !continent.trim().isEmpty();

        try {
            if (hasRegion) {
                // Regional filtering uses PreparedStatement
                String sql = "SELECT COUNT(*) AS Total FROM country WHERE Region = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, region);
                    ResultSet rset = pstmt.executeQuery();
                    if (rset.next()) total = rset.getInt("Total");
                }

            } else if (hasContinent) {
                String sql = "SELECT COUNT(*) AS Total FROM country WHERE Continent = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, continent);
                    ResultSet rset = pstmt.executeQuery();
                    if (rset.next()) total = rset.getInt("Total");
                }

            } else {
                // No filter → use normal Statement
                String sql = "SELECT COUNT(*) AS Total FROM country;";
                try (Statement stmt = c.getConnection().createStatement();
                     ResultSet rset = stmt.executeQuery(sql)) {
                    if (rset.next()) total = rset.getInt("Total");
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to count countries: " + e.getMessage());
        }

        return total;
    }

    /**
     * Counts cities filtered by:
     * - continent
     * - region
     * - country
     * - district
     * <p>
     * If all null → count ALL cities.
     */
    public int totalNumberOfCities(String continent, String region, String country, String district) {
        int total = 0;

        boolean hasRegion = region != null && !region.trim().isEmpty();
        boolean hasContinent = continent != null && !continent.trim().isEmpty();
        boolean hasCountry = country != null && !country.trim().isEmpty();
        boolean hasDistrict = district != null && !district.trim().isEmpty();

        try {
            if (hasRegion) {
                String sql =
                        "SELECT COUNT(*) AS Total " +
                                "FROM city JOIN country ON city.CountryCode = country.Code " +
                                "WHERE country.Region = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, region);
                    ResultSet rset = pstmt.executeQuery();
                    if (rset.next()) total = rset.getInt("Total");
                }

            } else if (hasContinent) {
                String sql =
                        "SELECT COUNT(*) AS Total " +
                                "FROM city JOIN country ON city.CountryCode = country.Code " +
                                "WHERE country.Continent = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, continent);
                    ResultSet rset = pstmt.executeQuery();
                    if (rset.next()) total = rset.getInt("Total");
                }

            } else if (hasCountry) {
                String sql =
                        "SELECT COUNT(*) AS Total " +
                                "FROM city JOIN country ON city.CountryCode = country.Code " +
                                "WHERE country.Name = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, country);
                    ResultSet rset = pstmt.executeQuery();
                    if (rset.next()) total = rset.getInt("Total");
                }

            } else if (hasDistrict) {
                String sql = "SELECT COUNT(*) AS Total FROM city WHERE District = ?;";
                try (PreparedStatement pstmt = c.getConnection().prepareStatement(sql)) {
                    pstmt.setString(1, district);
                    ResultSet rset = pstmt.executeQuery();
                    if (rset.next()) total = rset.getInt("Total");
                }

            } else {
                String sql = "SELECT COUNT(*) AS Total FROM city;";
                try (Statement stmt = c.getConnection().createStatement();
                     ResultSet rset = stmt.executeQuery(sql)) {
                    if (rset.next()) total = rset.getInt("Total");
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to count countries: " + e.getMessage());
        }

        return total;
    }
}
