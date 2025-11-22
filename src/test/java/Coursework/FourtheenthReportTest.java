package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * Unit test for FourteenthReport – top N cities in a region.
 */
@ExtendWith(MockitoExtension.class)
class FourteenthReportTest {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    PreparedStatement preparedStatement;

    @Mock
    ResultSet resultSet;

    @Test
    void showTopNCitiesInRegion_generatesExpectedMarkdown() throws SQLException {
        // Arrange
        String region = "Western Europe";
        int topN = 2;

        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Mock two result rows
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("City")).thenReturn("Paris", "Berlin");
        when(resultSet.getString("Country")).thenReturn("France", "Germany");
        when(resultSet.getString("District")).thenReturn("Île-de-France", "Berlin");
        when(resultSet.getInt("Population")).thenReturn(2_100_000, 3_500_000);

        try (MockedStatic<ReportManager> mocked = mockStatic(ReportManager.class)) {
            // Act
            FourteenthReport report = new FourteenthReport(connection);
            report.showTopNCitiesInRegion(topN, region);

            // Assert
            mocked.verify(() -> ReportManager.writeMarkdown(
                    eq("14_FourteenthReport"),
                    eq("2_Top_Populated_Cities_In_Western Europe.md"),
                    argThat(md ->
                            md.contains("# Top 2 populated cities in Western Europe") &&
                                    md.contains("| Paris | France | Île-de-France | 2100000 |") &&
                                    md.contains("| Berlin | Germany | Berlin | 3500000 |")
                    )
            ));
        }
    }
}
