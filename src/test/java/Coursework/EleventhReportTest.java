// src/test/java/Coursework/EleventhReportTest.java
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
 * Dynamic unit test for EleventhReport – cities per district.
 */
@ExtendWith(MockitoExtension.class)
class EleventhReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement distinctDistrictStatement;

    @Mock
    ResultSet districtsResult;

    @Mock
    PreparedStatement districtPreparedStatement;

    @Mock
    ResultSet districtCitiesResult;

    @Test
    void showCitiesByDistrict_generatesDynamicMarkdownPerDistrict() throws Exception {

        when(connection.getConnection()).thenReturn(sqlConnection);

        when(sqlConnection.createStatement()).thenReturn(distinctDistrictStatement);
        when(distinctDistrictStatement.executeQuery(startsWith("SELECT DISTINCT District")))
                .thenReturn(districtsResult);

        when(districtsResult.next()).thenReturn(true, false);
        when(districtsResult.getString("District")).thenReturn("TestDistrict");

        when(sqlConnection.prepareStatement(startsWith("SELECT city.Name AS City")))
                .thenReturn(districtPreparedStatement);
        when(districtPreparedStatement.executeQuery()).thenReturn(districtCitiesResult);

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
        rows.add(new Row("AlphaCity", "AlphaLand", "TestDistrict", 1_000_000));

        when(districtCitiesResult.next()).thenReturn(true, false);
        when(districtCitiesResult.getString("City")).thenReturn(rows.get(0).city);
        when(districtCitiesResult.getString("Country")).thenReturn(rows.get(0).country);
        when(districtCitiesResult.getString("District")).thenReturn(rows.get(0).district);
        when(districtCitiesResult.getInt("Population")).thenReturn(rows.get(0).population);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            EleventhReport report = new EleventhReport(connection);

            // Act
            report.showCitiesByDistrict();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    anyString(),
                    anyString(),
                    argThat(md -> {
                        if (!(md.contains("TestDistrict") && md.contains("#"))) return false;
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