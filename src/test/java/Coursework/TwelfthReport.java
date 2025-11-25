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
 * Unit test for TwelfthReport – top cities in the world.
 */
@ExtendWith(MockitoExtension.class)
class TwelfthReportTest {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    PreparedStatement stmt;

    @Mock
    ResultSet rs;

    @Test
    void showTopNCitiesInTheWorld_generatesMarkdown() throws Exception {
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString("City")).thenReturn("GlobalCity");
        when(rs.getString("Country")).thenReturn("WorldLand");
        when(rs.getString("District")).thenReturn("Central");
        when(rs.getInt("Population")).thenReturn(8000000);

        try (MockedStatic<ReportManager> rm = mockStatic(ReportManager.class)) {
            TwelfthReport report = new TwelfthReport(connection);
            report.showTopNCitiesITheWorld(1);

            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("12_TwelfthReport"),
                    eq("1_Top_Populated_Cities_In_World.md"),
                    argThat(md -> md.contains("#") &&
                            md.contains("GlobalCity") &&
                            md.contains("WorldLand") &&
                            md.contains("Central") &&
                            md.contains("8000000"))
            ));
        }
    }
}
