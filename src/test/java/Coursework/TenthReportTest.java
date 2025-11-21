// src/test/java/Coursework/TenthReportTest.java
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
 * Dynamic unit test for TenthReport – cities per country.
 */
@ExtendWith(MockitoExtension.class)
class TenthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement countryStatement;

    @Mock
    ResultSet countriesResult;

    @Mock
    PreparedStatement countryPreparedStatement;

    @Mock
    ResultSet countryCitiesResult;

    @Test
    void showCitiesByCountry_generatesDynamicMarkdownPerCountry() throws Exception {

        when(connection.getConnection()).thenReturn(sqlConnection);

        when(sqlConnection.createStatement()).thenReturn(countryStatement);
        when(countryStatement.executeQuery(startsWith("SELECT Name AS Country")))
                .thenReturn(countriesResult);

        when(countriesResult.next()).thenReturn(true, false);
        when(countriesResult.getString("Country")).thenReturn("TestCountry");

        when(sqlConnection.prepareStatement(startsWith("SELECT city.Name AS City")))
                .thenReturn(countryPreparedStatement);
        when(countryPreparedStatement.executeQuery()).thenReturn(countryCitiesResult);

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
        rows.add(new Row("AlphaCity", "TestCountry", "DistrictA", 1_000_000));

        when(countryCitiesResult.next()).thenReturn(true, false);
        when(countryCitiesResult.getString("City")).thenReturn(rows.get(0).city);
        when(countryCitiesResult.getString("Country")).thenReturn(rows.get(0).country);
        when(countryCitiesResult.getString("District")).thenReturn(rows.get(0).district);
        when(countryCitiesResult.getInt("Population")).thenReturn(rows.get(0).population);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            TenthReport report = new TenthReport(connection);

            // Act
            report.showCitiesByCountry();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    anyString(),
                    anyString(),
                    argThat(md -> {
                        if (!(md.contains("TestCountry") && md.contains("#"))) return false;
                        Row r = rows.get(0);
                        return md.contains(r.city)
                                && md.contains(r.country)
                                && md.contains(r.district)
                                && md.contains(String.valueOf(r.population));
                    })
            ));
        }
    }
}