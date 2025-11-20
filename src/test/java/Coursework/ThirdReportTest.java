package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

/**
 * Unit test for ThirdReport using Mockito.
 * Updated to match new SQL fields:
 * Code, Country, Continent, Region, Population, Capital
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
        // Arrange
        when(connection.getConnection()).thenReturn(sqlConnection);

        // Mock distinct regions
        when(sqlConnection.createStatement()).thenReturn(distinctRegionStatement);
        when(distinctRegionStatement.executeQuery(startsWith("SELECT DISTINCT Region")))
                .thenReturn(regionsResult);

        when(regionsResult.next()).thenReturn(true, false);
        when(regionsResult.getString("Region")).thenReturn("Western Europe");

        // Mock per-region SQL:
        // SELECT country.Code, country.Name AS Country, country.Continent,
        //        country.Region, country.Population, city.Name AS Capital ...
        when(sqlConnection.prepareStatement(startsWith("SELECT country.Code")))
                .thenReturn(regionPreparedStatement);

        when(regionPreparedStatement.executeQuery()).thenReturn(regionCountriesResult);

        // Fake result rows
        when(regionCountriesResult.next()).thenReturn(true, false);

        when(regionCountriesResult.getString("Code")).thenReturn("GBR");
        when(regionCountriesResult.getString("Country")).thenReturn("United Kingdom");
        when(regionCountriesResult.getString("Continent")).thenReturn("Europe");
        when(regionCountriesResult.getString("Region")).thenReturn("Western Europe");
        when(regionCountriesResult.getInt("Population")).thenReturn(67000000);
        when(regionCountriesResult.getString("Capital")).thenReturn("London");

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            ThirdReport report = new ThirdReport(connection);

            // Act
            report.showCountriesByRegion();

            // Assert markdown output
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("3_ThirdReport"),
                    eq("Western Europe.md"),
                    argThat(md ->
                            md.contains("# Countries in Western Europe") &&
                                    md.contains("GBR") &&
                                    md.contains("United Kingdom") &&
                                    md.contains("Europe") &&
                                    md.contains("Western Europe") &&
                                    md.contains("67000000") &&
                                    md.contains("London")           // capital
                    )
            ));
        }
    }
}
