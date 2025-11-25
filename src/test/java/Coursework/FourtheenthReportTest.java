package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for FourteenthReport – top cities in a region.
 */
@ExtendWith(MockitoExtension.class)
class FourteenthReportTest {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    PreparedStatement stmt;

    @Mock
    ResultSet rs;

    @Test
    void showTopNCitiesInRegion_generatesMarkdown() throws Exception {
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString("City")).thenReturn("Munich");
        when(rs.getString("Country")).thenReturn("Germany");
        when(rs.getString("District")).thenReturn("Bavaria");
        when(rs.getInt("Population")).thenReturn(1400000);

        try (MockedStatic<ReportManager> rm = mockStatic(ReportManager.class)) {
            FourteenthReport report = new FourteenthReport(connection);
            report.showTopNCitiesInRegion(1, "Western Europe");

            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("14_FourteenthReport"),
                    eq("1_Top_Populated_Cities_In_Western Europe.md"),
                    argThat(md -> md.contains("Munich") &&
                            md.contains("Germany") &&
                            md.contains("1400000") &&
                            md.contains("# Top 1 populated cities in Western Europe"))
            ));
        }
    }
}
