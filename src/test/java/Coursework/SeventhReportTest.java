package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit test for SeventhReport using Mockito.
 * Verifies that the city markdown is generated and passed to ReportManager.
 */
@ExtendWith(MockitoExtension.class)
class SeventhReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showCitiesByPopulation_generatesMarkdown() throws Exception {
        // Arrange DB mocks
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("City")).thenReturn("Tokyo", "Lagos");
        when(resultSet.getString("Country")).thenReturn("Japan", "Nigeria");
        when(resultSet.getString("District")).thenReturn("Tokyo-to", "Lagos");
        when(resultSet.getInt("Population")).thenReturn(37400068, 14000000);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            SeventhReport report = new SeventhReport(connection);

            // Act
            report.showCitiesByPopulation();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("7_SeventhReport"),
                    eq("SeventhReport.md"),
                    argThat(md ->
                            md.contains("# All cities in the world ordered by population") &&
                                    md.contains("Tokyo") &&
                                    md.contains("Japan") &&
                                    md.contains("Lagos") &&
                                    md.contains("Nigeria")
                    )
            ));
        }
    }
}
