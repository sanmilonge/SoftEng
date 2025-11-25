// src/test/java/Coursework/ConnectionTest.java
package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests the behaviour of Coursework.Connection without using a real database.
 * DriverManager is mocked so connect() can be tested safely.
 */
@ExtendWith(MockitoExtension.class)
class ConnectionTest {

    /**
     * Ensures connect() succeeds on the first attempt when
     * DriverManager.getConnection() returns a valid connection.
     */
    @Test
    void connect_successfullyEstablishesConnection() throws Exception {

        Connection mockSqlCon = mock(Connection.class);

        // Mock static DriverManager.getConnection()
        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {

            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockSqlCon);

            Coursework.Connection con = new Coursework.Connection();

            con.connect("localhost:33060", 0);   // delay 0 = avoid Thread.sleep()

            assert con.getConnection() == mockSqlCon;
        }
    }

    /**
     * Verifies retry logic:
     * - first two connection attempts throw SQLException
     * - third attempt returns a valid connection
     */
    @Test
    void connect_retriesOnFailureThenSucceeds() throws Exception {

        Connection mockSqlCon = mock(Connection.class);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {

            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenAnswer(new org.mockito.stubbing.Answer<Object>() {
                        int calls = 0;

                        @Override
                        public Object answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
                            if (calls < 2) {
                                calls++;
                                throw new SQLException("fail " + calls);
                            }
                            return mockSqlCon;
                        }
                    });

            Coursework.Connection con = new Coursework.Connection();

            con.connect("localhost:33060", 0);

            assert con.getConnection() == mockSqlCon;
        }
    }

    /**
     * Ensures disconnect() closes the wrapped JDBC connection.
     * Reflection injects a mock into the private "con" field.
     */
    @Test
    void disconnect_closesConnection() throws Exception {

        Connection mockSqlCon = mock(Connection.class);

        Coursework.Connection con = new Coursework.Connection();

        // Inject mock connection into private field
        var conField = Coursework.Connection.class.getDeclaredField("con");
        conField.setAccessible(true);
        conField.set(con, mockSqlCon);

        con.disconnect();

        verify(mockSqlCon, times(1)).close();
        assert con.getConnection() == null;
    }
}
