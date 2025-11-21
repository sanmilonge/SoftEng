// src/test/java/Coursework/TwelfthReportTest.java
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
 * Unit test for TwelfthReport – most populated cities in the world.
 */
@ExtendWith(MockitoExtension.class)
class TwelfthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    PreparedStatement preparedStatement;

    @Mock
    ResultSet resultSet;

    @Test
    void showTopNCitiesITheWorld_generatesCorrectMarkdown() throws Exception {

        // Arrange
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.prepareStatement(startsWith("SELECT city.Name AS City"))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        int n = 2;

        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getString("City")).thenReturn("AlphaCity", "BetaCity");
        when(resultSet.getString("Country")).thenReturn("AlphaLand", "BetaLand");
        when(resultSet.getString("District")).thenReturn("DistrictA", "DistrictB");
        when(resultSet.getInt("Population")).thenReturn(1_000_000, 500_000);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            TwelfthReport report = new TwelfthReport(connection);

            // Act
            report.showTopNCitiesITheWorld(n);

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("12_TwelfthReport"),
                    eq(n + "_Top_Populated_Cities_In_World.md"),
                    argThat(md -> md.contains("# Top 2 populated cities in the world")
                            && md.contains("AlphaCity") && md.contains("AlphaLand") && md.contains("DistrictA")
                            && md.contains("1000000")
                            && md.contains("BetaCity") && md.contains("BetaLand") && md.contains("DistrictB")
                            && md.contains("500000")
                    )
            ));
        }
    }
}
