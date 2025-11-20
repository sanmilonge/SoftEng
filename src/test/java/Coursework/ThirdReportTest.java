package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for ThirdReport using Mockito.
 * Verifies per-region reports are generated and passed to ReportManager.
 */
@ExtendWith(MockitoExtension.class)
class ThirdReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement distinctRegionStatement;

    @Mock
    ResultSet regionsResult;

    @Mock
    PreparedStatement regionPreparedStatement;

    @Mock
    ResultSet regionCountriesResult;

    @Test
    void showCountriesByRegion_generatesMarkdownPerRegion() throws Exception {
        // Arrange DB mocks
        when(connection.getConnection()).thenReturn(sqlConnection);

        // Distinct regions
        when(sqlConnection.createStatement()).thenReturn(distinctRegionStatement);
        when(distinctRegionStatement.executeQuery(startsWith("SELECT DISTINCT Region")))
                .thenReturn(regionsResult);

        when(regionsResult.next()).thenReturn(true, false);
        when(regionsResult.getString("Region")).thenReturn("Western Europe");

        // Per-region countries
        when(sqlConnection.prepareStatement(startsWith("SELECT Code, Name, Continent, Population")))
                .thenReturn(regionPreparedStatement);
        when(regionPreparedStatement.executeQuery()).thenReturn(regionCountriesResult);

        when(regionCountriesResult.next()).thenReturn(true, false);
        when(regionCountriesResult.getString("Code")).thenReturn("GBR");
        when(regionCountriesResult.getString("Name")).thenReturn("United Kingdom");
        when(regionCountriesResult.getString("Continent")).thenReturn("Europe");
        when(regionCountriesResult.getInt("Population")).thenReturn(60000000);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            ThirdReport report = new ThirdReport(connection);

            // Act
            report.showCountriesByRegion();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("3_ThirdReport"),
                    eq("Western Europe.md"),
                    argThat(md ->
                            md.contains("# Countries in Western Europe") &&
                                    md.contains("GBR") &&
                                    md.contains("United Kingdom") &&
                                    md.contains("Europe")
                    )
            ));
        }
    }
}
