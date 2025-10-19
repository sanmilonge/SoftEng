package Coursework;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ConnectionTest
 * ---------------
 * This class tests the Connection class to make sure that connecting
 * and disconnecting from the database works properly.
 *
 * These tests check:
 * 1. That the connect() method doesn’t crash or throw an error.
 * 2. That the connection starts as null before connecting to the database.
 */

public class ConnectionTest
{
    /**
     * Test 1 - Check connect() and disconnect().
     *
     * What it does:
     * - Creates a new Connection object.
     * - Calls connect() to try connecting to the database.
     * - Uses assertDoesNotThrow() to make sure it doesn’t crash or throw errors.
     * - Then calls disconnect() to close the connection safely.
     *
     * Why it matters:
     * - This test makes sure your connection setup is stable and doesn’t break
     *   the program, even if the database isn’t available.
     */


    @Test
    void testConnectAndDisconnect()
    {
        Connection con = new Connection();
        assertDoesNotThrow(con::connect, "Connection.connect() should not throw an exception");
        con.disconnect();
    }

    /**
     * Test 2 - Check if the connection is null before connecting.
     *
     * What it does:
     * - Creates a new Connection object.
     * - Checks that getConnection() initially returns null.
     *
     * Why it matters:
     * - This ensures that the connection only exists after connect() is called.
     *   Before connecting, it should be empty (null), proving that your code
     *   correctly waits to establish the connection.
     */

    @Test
    void testGetConnectionInitiallyNull()
    {
        Connection con = new Connection();
        assertNull(con.getConnection(), "Before connecting, connection should be null");
    }
}
