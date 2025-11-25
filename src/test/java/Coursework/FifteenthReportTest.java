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
 * Unit test for FifteenthReport – top cities in a country.
 */
@ExtendWith(MockitoExtension.class)
class FifteenthReportTest {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    PreparedStatement stmt;

    @Mock
    ResultSet rs;

    @Test
    void showTopNCitiesInCountry_generatesMarkdown() throws Exception {
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString("City")).thenReturn("Tokyo");
        when(rs.getString("Country")).thenReturn("Japan");
        when(rs.getString("District")).thenReturn("Tokyo");
        when(rs.getInt("Population")).thenReturn(13900000);

        try (MockedStatic<ReportManager> rm = mockStatic(ReportManager.class)) {
            FifteenthReport report = new FifteenthReport(connection);
            report.showTopNCitiesInCountry(1, "Japan");

            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("15_FifteenthReport"),
                    eq("1_Top_Populated_Cities_In_Japan.md"),
                    argThat(md -> md.contains("Tokyo") &&
                            md.contains("Japan") &&
                            md.contains("13900000") &&
                            md.contains("# Top 1 populated cities in Japan"))
            ));
        }
    }
}
