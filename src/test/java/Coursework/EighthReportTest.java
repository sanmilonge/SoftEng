// src/test/java/Coursework/EighthReportTest.java
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
 * Dynamic unit test for EighthReport – cities grouped by continent.
 */
@ExtendWith(MockitoExtension.class)
class EighthReportTest extends ReportTestSupport {

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
    ResultSet continentCitiesResult;

    @Test
    void showCitiesContinent_generatesDynamicMarkdownPerContinent() throws Exception {

        when(connection.getConnection()).thenReturn(sqlConnection);

        when(sqlConnection.createStatement()).thenReturn(distinctContinentStatement);
        when(distinctContinentStatement.executeQuery(startsWith("SELECT DISTINCT Continent")))
                .thenReturn(continentsResult);

        when(continentsResult.next()).thenReturn(true, false);
        when(continentsResult.getString("Continent")).thenReturn("TestContinent");

        when(sqlConnection.prepareStatement(startsWith("SELECT"))).thenReturn(continentPreparedStatement);
        when(continentPreparedStatement.executeQuery()).thenReturn(continentCitiesResult);

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

        when(continentCitiesResult.next()).thenReturn(true, true, false);
        when(continentCitiesResult.getString("City")).thenReturn(rows.get(0).city, rows.get(1).city);
        when(continentCitiesResult.getString("Country")).thenReturn(rows.get(0).country, rows.get(1).country);
        when(continentCitiesResult.getString("District")).thenReturn(rows.get(0).district, rows.get(1).district);
        when(continentCitiesResult.getInt("Population")).thenReturn(rows.get(0).population, rows.get(1).population);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            EighthReport report = new EighthReport(connection);

            // Act
            report.showCitiesContinent();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    anyString(),
                    anyString(),
                    argThat(md -> {
                        if (!(md.contains("TestContinent") && md.contains("#"))) return false;
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