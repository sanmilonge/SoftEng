// src/test/java/Coursework/ThirdReportTest.java
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
 * Dynamic unit test for ThirdReport – per-region countries.
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
    void showCountriesByRegion_generatesDynamicMarkdownPerRegion() throws Exception {

        // Arrange DB mocks
        when(connection.getConnection()).thenReturn(sqlConnection);

        // Distinct regions list
        when(sqlConnection.createStatement()).thenReturn(distinctRegionStatement);
        when(distinctRegionStatement.executeQuery(startsWith("SELECT DISTINCT Region")))
                .thenReturn(regionsResult);

        when(regionsResult.next()).thenReturn(true, false);
        when(regionsResult.getString("Region")).thenReturn("TestRegion");

        // Per-region query
        when(sqlConnection.prepareStatement(startsWith("SELECT"))).thenReturn(regionPreparedStatement);
        when(regionPreparedStatement.executeQuery()).thenReturn(regionCountriesResult);

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
        rows.add(new Row("AAA", "AlphaLand", "TestContinent", "TestRegion", 5_000_000, "AlphaCity"));
        rows.add(new Row("BBB", "BetaLand", "TestContinent", "TestRegion", 3_000_000, "BetaCity"));

        when(regionCountriesResult.next()).thenReturn(true, true, false);

        when(regionCountriesResult.getString("Code")).thenReturn(rows.get(0).code, rows.get(1).code);
        when(regionCountriesResult.getString("Country")).thenReturn(rows.get(0).country, rows.get(1).country);
        when(regionCountriesResult.getString("Continent")).thenReturn(rows.get(0).continent, rows.get(1).continent);
        when(regionCountriesResult.getString("Region")).thenReturn(rows.get(0).region, rows.get(1).region);
        when(regionCountriesResult.getInt("Population")).thenReturn(rows.get(0).population, rows.get(1).population);
        when(regionCountriesResult.getString("Capital")).thenReturn(rows.get(0).capital, rows.get(1).capital);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            ThirdReport report = new ThirdReport(connection);

            // Act
            report.showCountriesByRegion();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    anyString(),
                    anyString(),
                    argThat(md -> {
                        if (!(md.contains("TestRegion") && md.contains("#"))) return false;

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