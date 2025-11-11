package Coursework;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AppIntegrationTest
 * ------------------
 * This class runs integration tests that verify the database connection
 * and some basic data integrity in the "world" sample database.
 *
 * These tests require:
 * - A running MySQL container (world-db)
 * - The database being reachable at localhost:33060
 *
 * The goal is to confirm:
 * 1. That the custom Coursework.Connection class can successfully connect.
 * 2. That the connection remains open and functional.
 * 3. That the world dataset contains the expected number of countries and cities.
 */
public class AppIntegrationTest {

    // Custom wrapper class for handling DB connections in the coursework
    static Coursework.Connection db;

    /**
     * @BeforeAll — runs once before all test methods.
     * Sets up a real database connection using your Connection wrapper.
     */
    @BeforeAll
    static void setup() {
        db = new Coursework.Connection();

        // Try to connect to the world-db container
        // (port 33060 matches docker-compose / CI workflow config)
        db.connect();

        // Assert the underlying Java SQL Connection object is actually set up
        assertNotNull(db.getConnection(), "DB connection should be established in @BeforeAll");
    }

    /**
     * @AfterAll — runs once after all test methods.
     * Cleans up resources and closes the connection to avoid leaks.
     */
    @AfterAll
    static void teardown() {
        if (db != null) db.disconnect();
    }

    /**
     * Test 1 — Check that the connection is valid and open.
     *
     * Purpose:
     * - Ensures db.connect() created a non-null, live connection.
     * - Prevents follow-up tests from running on a broken or closed connection.
     */
    @Test
    void connectionIsOpen() throws SQLException {
        Connection c = db.getConnection();              // grab the raw JDBC connection
        assertNotNull(c, "Connection should not be null");
        assertFalse(c.isClosed(), "Connection should be open");
    }

    /**
     * Test 2 — Validate that the world.country table has the expected record count (239).
     *
     * Purpose:
     * - Verifies that the world-db container has loaded correctly.
     * - Confirms your app can execute SELECT queries successfully.
     */
    @Test
    void worldCountryCountIs239() throws Exception {
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM country")) {

            // The result set should have exactly one row (the count)
            assertTrue(rs.next(), "COUNT query should return one row");

            // Verify the row count matches the known dataset (239 countries)
            assertEquals(239, rs.getInt(1), "Expected 239 rows in world.country");
        }
    }

    /**
     * Test 3 — Confirm that the city table contains data (non-empty).
     *
     * Purpose:
     * - Ensures city data was imported into the database.
     * - Confirms that SELECT queries work correctly for another table.
     */
    @Test
    void cityTableHasData() throws Exception {
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM city")) {

            // The query should return one row with a count
            assertTrue(rs.next(), "COUNT query should return one row");

            // The number of rows should be greater than zero (table not empty)
            assertTrue(rs.getInt(1) > 0, "world.city should not be empty");
        }
    }
}
