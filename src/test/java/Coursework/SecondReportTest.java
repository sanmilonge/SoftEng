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
 * Unit test for SecondReport using Mockito.
 * Verifies that per-continent markdown is generated and passed to ReportManager.
 */
@ExtendWith(MockitoExtension.class)
class SecondReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement distinctContinentStatement;

    @Mock
    ResultSet continentsResult;

    @Mock
    PreparedStatement continentPreparedStatement;

    @Mock
    ResultSet continentCountriesResult;

    @Test
    void showCountriesContinent_generatesMarkdownPerContinent() throws Exception {
        // Arrange DB mocks
        when(connection.getConnection()).thenReturn(sqlConnection);

        // Distinct continents
        when(sqlConnection.createStatement()).thenReturn(distinctContinentStatement);
        when(distinctContinentStatement.executeQuery(startsWith("SELECT DISTINCT Continent")))
                .thenReturn(continentsResult);

        when(continentsResult.next()).thenReturn(true, false);
        when(continentsResult.getString("Continent")).thenReturn("Europe");

        // Per-continent countries
        when(sqlConnection.prepareStatement(startsWith("SELECT Code, Name, Region, Population")))
                .thenReturn(continentPreparedStatement);
        when(continentPreparedStatement.executeQuery()).thenReturn(continentCountriesResult);

        when(continentCountriesResult.next()).thenReturn(true, true, false);
        when(continentCountriesResult.getString("Code")).thenReturn("GBR", "FRA");
        when(continentCountriesResult.getString("Name")).thenReturn("United Kingdom", "France");
        when(continentCountriesResult.getString("Region")).thenReturn("British Islands", "Western Europe");
        when(continentCountriesResult.getInt("Population")).thenReturn(60000000, 65000000);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            SecondReport report = new SecondReport(connection);

            // Act
            report.showCountriesContinent();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("2_SecondReport"),
                    eq("Europe.md"),
                    argThat(md ->
                            md.contains("# Countries in Europe") &&
                                    md.contains("GBR") &&
                                    md.contains("United Kingdom") &&
                                    md.contains("FRA") &&
                                    md.contains("France")
                    )
            ));
        }
    }
}
