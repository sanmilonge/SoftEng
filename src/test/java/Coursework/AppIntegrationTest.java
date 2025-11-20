package Coursework;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AppIntegrationTest
 * ------------------
 * Verifies that the MySQL database is reachable and core tables
 * contain the expected data.
 *
 * Requires the world database to be running on localhost:33060.
 */
public class AppIntegrationTest {

    private static Coursework.Connection db;

    @BeforeAll
    static void setup() {
        // Keep any report generation during tests inside TestReports
        System.setProperty("report.folder", "src/main/resources/reports/TestReports");

        db = new Coursework.Connection();
        db.connect("localhost:33060", 500);

        assertNotNull(db.getConnection(), "DB connection should be established in @BeforeAll");
        assertDoesNotThrow(() -> db.getConnection().getCatalog(),
                "Connection should be alive before tests start");
    }

    @AfterAll
    static void teardown() {
        if (db != null) {
            db.disconnect();
        }
    }

    @Test
    void connectionIsOpen() throws Exception {
        Connection c = db.getConnection();
        assertNotNull(c, "Connection should not be null");
        assertFalse(c.isClosed(), "Connection should be open");
    }

    @Test
    void worldCountryCountIs239() throws Exception {
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM country")) {

            assertTrue(rs.next(), "COUNT query should return one row");
            int count = rs.getInt(1);
            assertEquals(239, count, "Expected 239 rows in world.country but got " + count);
        }
    }

    @Test
    void cityTableHasData() throws Exception {
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM city")) {

            assertTrue(rs.next(), "COUNT query should return one row");
            int count = rs.getInt(1);
            assertTrue(count > 0, "world.city should not be empty");
        }
    }
}
