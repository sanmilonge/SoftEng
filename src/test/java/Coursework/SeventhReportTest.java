// src/test/java/Coursework/SeventhReportTest.java
package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Dynamic unit test for SeventhReport (cities in world by population).
 */
@ExtendWith(MockitoExtension.class)
class SeventhReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showCitiesByPopulation_generatesDynamicMarkdown() throws Exception {

        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(startsWith("SELECT"))).thenReturn(resultSet);

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

        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getString("City")).thenReturn(rows.get(0).city, rows.get(1).city);
        when(resultSet.getString("Country")).thenReturn(rows.get(0).country, rows.get(1).country);
        when(resultSet.getString("District")).thenReturn(rows.get(0).district, rows.get(1).district);
        when(resultSet.getInt("Population")).thenReturn(rows.get(0).population, rows.get(1).population);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            SeventhReport report = new SeventhReport(connection);

            // Act
            report.showCitiesByPopulation();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    anyString(),
                    anyString(),
                    argThat(md -> {
                        if (!md.contains("#")) return false;
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