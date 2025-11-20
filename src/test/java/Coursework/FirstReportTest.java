package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for FirstReport using Mockito to mock DB and ReportManager.
 */
@ExtendWith(MockitoExtension.class)
class FirstReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showCountriesByPopulation_buildsMarkdownAndCallsReportManager() throws Exception {
        // Arrange DB mocks
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        // Two rows then end
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("Code")).thenReturn("GBR", "NGA");
        when(resultSet.getString("Name")).thenReturn("United Kingdom", "Nigeria");
        when(resultSet.getString("Continent")).thenReturn("Europe", "Africa");
        when(resultSet.getString("Region")).thenReturn("British Islands", "Western Africa");
        when(resultSet.getInt("Population")).thenReturn(60000000, 200000000);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            FirstReport report = new FirstReport(connection);

            // Act
            report.showCountriesByPopulation();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("1_FirstReport"),
                    eq("FirstReport.md"),
                    argThat(md ->
                            md.contains("# All the countries in the world organised by population") &&
                                    md.contains("GBR") &&
                                    md.contains("United Kingdom") &&
                                    md.contains("NGA") &&
                                    md.contains("Nigeria")
                    )
            ));
        }
    }
}
