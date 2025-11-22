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
 * Unit test for ThirteenthReport – top cities in a continent.
 */
@ExtendWith(MockitoExtension.class)
class ThirteenthReportTest {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    PreparedStatement stmt;

    @Mock
    ResultSet rs;

    @Test
    void showTopNCitiesInContinent_generatesMarkdown() throws Exception {
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString("City")).thenReturn("Cairo");
        when(rs.getString("Country")).thenReturn("Egypt");
        when(rs.getString("District")).thenReturn("Cairo");
        when(rs.getInt("Population")).thenReturn(9500000);

        try (MockedStatic<ReportManager> rm = mockStatic(ReportManager.class)) {
            ThirteenthReport report = new ThirteenthReport(connection);
            report.showTopNCitiesInContinent(1, "Africa");

            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("13_ThirteenthReport"),
                    eq("1_Top_Populated_Cities_In_Africa.md"),
                    argThat(md -> md.contains("Cairo") &&
                            md.contains("Egypt") &&
                            md.contains("9500000") &&
                            md.contains("# Top 1 populated cities in Africa"))
            ));
        }
    }
}
