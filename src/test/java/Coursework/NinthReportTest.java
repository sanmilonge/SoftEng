// src/test/java/Coursework/NinthReportTest.java
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
 * Dynamic unit test for NinthReport – cities per region.
 */
@ExtendWith(MockitoExtension.class)
class NinthReportTest extends ReportTestSupport {

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
    ResultSet regionCitiesResult;

    @Test
    void showCitiesByRegion_generatesDynamicMarkdownPerRegion() throws Exception {

        when(connection.getConnection()).thenReturn(sqlConnection);

        when(sqlConnection.createStatement()).thenReturn(distinctRegionStatement);
        when(distinctRegionStatement.executeQuery(startsWith("SELECT DISTINCT Region")))
                .thenReturn(regionsResult);

        when(regionsResult.next()).thenReturn(true, false);
        when(regionsResult.getString("Region")).thenReturn("TestRegion");

        when(sqlConnection.prepareStatement(startsWith("SELECT city.Name AS City")))
                .thenReturn(regionPreparedStatement);
        when(regionPreparedStatement.executeQuery()).thenReturn(regionCitiesResult);

        class Row {
            String city, country, district;
            int population;
            Row(String city, String country, String district, int population) {
                this.city = city;
                this.country = country;
                this.district = district;
                this.population = population;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("AlphaCity", "AlphaLand", "DistrictA", 1_000_000));
        rows.add(new Row("BetaCity", "BetaLand", "DistrictB", 500_000));

        when(regionCitiesResult.next()).thenReturn(true, true, false);
        when(regionCitiesResult.getString("City")).thenReturn(rows.get(0).city, rows.get(1).city);
        when(regionCitiesResult.getString("Country")).thenReturn(rows.get(0).country, rows.get(1).country);
        when(regionCitiesResult.getString("District")).thenReturn(rows.get(0).district, rows.get(1).district);
        when(regionCitiesResult.getInt("Population")).thenReturn(rows.get(0).population, rows.get(1).population);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            NinthReport report = new NinthReport(connection);

            // Act
            report.showCitiesByRegion();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    anyString(),
                    anyString(),
                    argThat(md -> {
                        if (!(md.contains("TestRegion") && md.contains("#"))) return false;
                        for (Row r : rows) {
                            if (!(md.contains(r.city)
                                    && md.contains(r.country)
                                    && md.contains(r.district)
                                    && md.contains(String.valueOf(r.population)))) {
                                return false;
                            }
                        }
                        return true;
                    })
            ));
        }
    }
}