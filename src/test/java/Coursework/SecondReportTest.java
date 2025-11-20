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
 * Updated to work with the new SQL including Country, Continent,
 * Region, Population, and Capital.
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

        // Per-continent SQL:
        // SELECT country.Code, country.Name AS Country, country.Continent,
        //        country.Region, country.Population, city.Name AS Capital ...
        when(sqlConnection.prepareStatement(startsWith("SELECT country.Code")))
                .thenReturn(continentPreparedStatement);

        when(continentPreparedStatement.executeQuery()).thenReturn(continentCountriesResult);

        // Mock data rows
        when(continentCountriesResult.next()).thenReturn(true, true, false);

        when(continentCountriesResult.getString("Code")).thenReturn("GBR", "FRA");
        when(continentCountriesResult.getString("Country")).thenReturn("United Kingdom", "France");
        when(continentCountriesResult.getString("Continent")).thenReturn("Europe", "Europe");
        when(continentCountriesResult.getString("Region")).thenReturn("British Islands", "Western Europe");
        when(continentCountriesResult.getInt("Population")).thenReturn(67000000, 65000000);
        when(continentCountriesResult.getString("Capital")).thenReturn("London", "Paris");

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            SecondReport report = new SecondReport(connection);

            // Act
            report.showCountriesContinent();

            // Assert: markdown contains all required fields
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("2_SecondReport"),
                    eq("Europe.md"),
                    argThat(md ->
                            md.contains("# Countries in Europe") &&
                                    md.contains("GBR") &&
                                    md.contains("United Kingdom") &&
                                    md.contains("Europe") &&
                                    md.contains("British Islands") &&
                                    md.contains("67000000") &&
                                    md.contains("London") &&      // Capital
                                    md.contains("FRA") &&
                                    md.contains("France") &&
                                    md.contains("Western Europe") &&
                                    md.contains("65000000") &&
                                    md.contains("Paris")          // Capital
                    )
            ));
        }
    }
}
