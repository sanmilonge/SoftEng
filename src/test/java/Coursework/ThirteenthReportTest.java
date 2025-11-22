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
 * Unit test for ThirteenthReport – top N cities in a continent.
 */
@ExtendWith(MockitoExtension.class)
class ThirteenthReportTest {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    PreparedStatement preparedStatement;

    @Mock
    ResultSet resultSet;

    @Test
    void showTopNCitiesInContinent_generatesExpectedMarkdown() throws SQLException {
        // Arrange
        String continent = "Asia";
        int topN = 2;

        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Set up fake result set
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("City")).thenReturn("Tokyo", "Delhi");
        when(resultSet.getString("Country")).thenReturn("Japan", "India");
        when(resultSet.getString("District")).thenReturn("Tokyo", "Delhi");
        when(resultSet.getInt("Population")).thenReturn(37_000_000, 31_000_000);

        // Act
        try (MockedStatic<ReportManager> mocked = mockStatic(ReportManager.class)) {
            ThirteenthReport report = new ThirteenthReport(connection);
            report.showTopNCitiesInContinent(topN, continent);

            // Assert: verify markdown content
            mocked.verify(() -> ReportManager.writeMarkdown(
                    eq("13_ThirteenthReport"),
                    eq("2_Top_Populated_Cities_In_Asia.md"),
                    argThat(md ->
                            md.contains("# Top 2 populated cities in Asia") &&
                                    md.contains("| Tokyo | Japan | Tokyo | 37000000 |") &&
                                    md.contains("| Delhi | India | Delhi | 31000000 |")
                    )
            ));
        }
    }
}
