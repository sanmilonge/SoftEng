// src/test/java/Coursework/SecondReportTest.java
package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Dynamic unit test for SecondReport.
 * Per-continent report; uses fake continent and countries.
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
    void showCountriesContinent_generatesDynamicMarkdownPerContinent() throws Exception {

        // Arrange
        when(connection.getConnection()).thenReturn(sqlConnection);

        // Distinct continents (via GetAll or direct SELECT DISTINCT)
        when(sqlConnection.createStatement()).thenReturn(distinctContinentStatement);
        when(distinctContinentStatement.executeQuery(startsWith("SELECT DISTINCT Continent")))
                .thenReturn(continentsResult);

        when(continentsResult.next()).thenReturn(true, false);
        when(continentsResult.getString("Continent")).thenReturn("TestContinent");

        // Per-continent query
        when(sqlConnection.prepareStatement(startsWith("SELECT"))).thenReturn(continentPreparedStatement);
        when(continentPreparedStatement.executeQuery()).thenReturn(continentCountriesResult);

        class Row {
            String code, country, continent, region, capital;
            int population;
            Row(String code, String country, String continent, String region,
                int population, String capital) {
                this.code = code;
                this.country = country;
                this.continent = continent;
                this.region = region;
                this.population = population;
                this.capital = capital;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("AAA", "AlphaLand", "TestContinent", "RegionA", 5_000_000, "AlphaCity"));
        rows.add(new Row("BBB", "BetaLand", "TestContinent", "RegionB", 3_000_000, "BetaCity"));

        when(continentCountriesResult.next()).thenReturn(true, true, false);

        when(continentCountriesResult.getString("Code")).thenReturn(
                rows.get(0).code,
                rows.get(1).code
        );
        when(continentCountriesResult.getString("Country")).thenReturn(
                rows.get(0).country,
                rows.get(1).country
        );
        when(continentCountriesResult.getString("Continent")).thenReturn(
                rows.get(0).continent,
                rows.get(1).continent
        );
        when(continentCountriesResult.getString("Region")).thenReturn(
                rows.get(0).region,
                rows.get(1).region
        );
        when(continentCountriesResult.getInt("Population")).thenReturn(
                rows.get(0).population,
                rows.get(1).population
        );
        when(continentCountriesResult.getString("Capital")).thenReturn(
                rows.get(0).capital,
                rows.get(1).capital
        );

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            SecondReport report = new SecondReport(connection);

            // Act
            report.showCountriesContinent();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    anyString(),
                    anyString(),
                    argThat(md -> {
                        // Contains continent name and header marker
                        if (!(md.contains("TestContinent") && md.contains("#"))) return false;

                        for (Row r : rows) {
                            if (!(md.contains(r.code)
                                    && md.contains(r.country)
                                    && md.contains(r.continent)
                                    && md.contains(r.region)
                                    && md.contains(String.valueOf(r.population))
                                    && md.contains(r.capital))) {
                                return false;
                            }
                        }
                        return true;
                    })
            ));
        }
    }
}