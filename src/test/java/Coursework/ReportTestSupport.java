package Coursework;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Shared base class for ALL report tests.
 *
 * Includes:
 *  - Filesystem isolation for TestReports
 *  - Helpers for verifying report files
 *  - Static ReportManager mocking helper
 *  - Reusable Mockito DB mocking utilities
 *
 * Clean, safe, and does NOT auto-mock ReportManager.
 */
public abstract class ReportTestSupport {

    /* ---------------------------------------------------------
       FILESYSTEM TEST HELPERS
       --------------------------------------------------------- */

    protected static final Path TEST_REPORT_ROOT =
            Paths.get("src", "main", "resources", "reports", "TestReports");

    @BeforeEach
    void initReportFolder() throws IOException {

        // Direct ReportManager to write into TestReports
        System.setProperty("report.folder", TEST_REPORT_ROOT.toString());

        // Clean TestReports folder
        if (Files.exists(TEST_REPORT_ROOT)) {
            Files.walk(TEST_REPORT_ROOT)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) { }
                    });
        }

        // Recreate empty folder
        Files.createDirectories(TEST_REPORT_ROOT);
    }

    protected Path resolveSubfolder(String subfolder) {
        if (subfolder == null || subfolder.isEmpty())
            return TEST_REPORT_ROOT;

        return TEST_REPORT_ROOT.resolve(subfolder);
    }

    protected boolean existsReportFile(String subfolder, String baseNamePrefix) throws IOException {
        Path root = resolveSubfolder(subfolder);
        if (!Files.exists(root)) return false;

        try (var walk = Files.walk(root)) {
            return walk
                    .filter(Files::isRegularFile)
                    .anyMatch(p -> p.getFileName().toString().startsWith(baseNamePrefix));
        }
    }

    protected long countMarkdownFiles(String subfolder) throws IOException {
        Path root = resolveSubfolder(subfolder);
        if (!Files.exists(root)) return 0;

        try (var walk = Files.walk(root)) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".md"))
                    .count();
        }
    }

    /** Tests call this — support class does NOT auto-mock static methods */
    public MockedStatic<ReportManager> mockReportManagerStatic() {
        return Mockito.mockStatic(ReportManager.class);
    }

    protected void assertDirectoryExists(Path dir, String message) {
        assertTrue(Files.exists(dir) && Files.isDirectory(dir), message);
    }


    /* ---------------------------------------------------------
       MOCKITO DB UTILITIES
       --------------------------------------------------------- */

    /** Container object holding a set of DB mocks */
    protected static class DBMock {
        public final Coursework.Connection appConn;
        public final java.sql.Connection sqlConn;
        public final Statement stmt;
        public final PreparedStatement pstmt;
        public final ResultSet rs;

        DBMock(Coursework.Connection appConn,
               java.sql.Connection sqlConn,
               Statement stmt,
               PreparedStatement pstmt,
               ResultSet rs) {

            this.appConn = appConn;
            this.sqlConn = sqlConn;
            this.stmt = stmt;
            this.pstmt = pstmt;
            this.rs = rs;
        }
    }

    /**
     * Creates a complete DB mock with:
     * - Connection
     * - SQL connection
     * - Statement
     * - PreparedStatement
     * - ResultSet
     *
     * BUT does NOT mock ReportManager.
     */
    protected DBMock mockDBEnvironment() throws Exception {
        Coursework.Connection appConn = mock(Coursework.Connection.class);
        java.sql.Connection sqlConn = mock(java.sql.Connection.class);
        Statement stmt = mock(Statement.class);
        PreparedStatement pstmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(appConn.getConnection()).thenReturn(sqlConn);
        when(sqlConn.createStatement()).thenReturn(stmt);
        when(sqlConn.prepareStatement(anyString())).thenReturn(pstmt);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(pstmt.executeQuery()).thenReturn(rs);

        return new DBMock(appConn, sqlConn, stmt, pstmt, rs);
    }

    /**
     * Builds rows for a mocked ResultSet.
     *
     * Example use:
     * mockRows(rs, new Object[][] {
     *     row(col("Name", "Europe"), col("Population", 500)),
     *     row(col("Name", "Asia"), col("Population", 700))
     * });
     */
    protected void mockRows(ResultSet rs, Object[][] rows) throws Exception {
        Boolean[] nextVals = new Boolean[rows.length + 1];

        // true for each row, then false at end
        for (int i = 0; i < rows.length; i++) nextVals[i] = true;
        nextVals[rows.length] = false;

        when(rs.next()).thenReturn(
                nextVals[0],
                java.util.Arrays.copyOfRange(nextVals, 1, nextVals.length)
        );

        // mock getString/getInt for each row
        for (Object[] row : rows) {
            for (Object colObj : row) {
                Object[] col = (Object[]) colObj;
                String column = (String) col[0];
                Object value = col[1];

                if (value instanceof Integer i)
                    when(rs.getInt(column)).thenReturn(i);
                else
                    when(rs.getString(column)).thenReturn((String) value);
            }
        }
    }

    protected Object[] row(Object[]... cols) {
        return cols;
    }

    protected Object[] col(String name, Object value) {
        return new Object[]{name, value};
    }
}
