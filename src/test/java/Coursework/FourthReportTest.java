// src/test/java/Coursework/FourthReportTest.java
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
 * Dynamic unit test for FourthReport (top N countries in world).
 */
@ExtendWith(MockitoExtension.class)
class FourthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showTopNCountries_generatesDynamicMarkdown() throws Exception {

        // Arrange
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // Fourth report builds a SELECT with LIMIT n
        when(statement.executeQuery(startsWith("SELECT"))).thenReturn(resultSet);

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

        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getString("Code")).thenReturn(rows.get(0).code, rows.get(1).code);
        when(resultSet.getString("Country")).thenReturn(rows.get(0).country, rows.get(1).country);
        when(resultSet.getString("Continent")).thenReturn(rows.get(0).continent, rows.get(1).continent);
        when(resultSet.getString("Region")).thenReturn(rows.get(0).region, rows.get(1).region);
        when(resultSet.getInt("Population")).thenReturn(rows.get(0).population, rows.get(1).population);
        when(resultSet.getString("Capital")).thenReturn(rows.get(0).capital, rows.get(1).capital);

        int n = 2;

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            FourthReport report = new FourthReport(connection);

            // Act
            report.showTopNCountries(n);

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    anyString(),
                    anyString(),
                    argThat(md -> {
                        if (!md.contains("# " + n + " top populated countries")) {
                            return false;
                        }
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