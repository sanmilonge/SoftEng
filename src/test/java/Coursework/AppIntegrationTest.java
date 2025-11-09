package Coursework;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {
    static Coursework.Connection db;  // your wrapper

    @BeforeAll
    static void setup() {
        db = new Coursework.Connection();
        db.connect("localhost:33060", 1000); // world-db from docker-compose
        assertNotNull(db.getConnection(), "DB connection should be established in @BeforeAll");
    }

    @AfterAll
    static void teardown() {
        if (db != null) db.disconnect();
    }

    @Test
    void connectionIsOpen() throws SQLException {
        Connection c = db.getConnection();
        assertNotNull(c, "Connection should not be null");
        assertFalse(c.isClosed(), "Connection should be open");
    }

    @Test
    void worldCountryCountIs239() throws Exception {
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM country")) {
            assertTrue(rs.next(), "COUNT query should return one row");
            assertEquals(239, rs.getInt(1), "Expected 239 rows in world.country");
        }
    }

    @Test
    void cityTableHasData() throws Exception {
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM city")) {
            assertTrue(rs.next());
            assertTrue(rs.getInt(1) > 0, "world.city should not be empty");
        }
    }
}
