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

@ExtendWith(MockitoExtension.class)
class ThirtyFirstReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showPopulationOfAllCities_generatesDynamicMarkdown() throws Exception {

        // DB mock setup
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // SQL check
        when(statement.executeQuery(startsWith("SELECT city.Name AS CityName")))
                .thenReturn(resultSet);

        // Sample city data
        class Row {
            final String name;
            final long population;

            Row(String name, long population) {
                this.name = name;
                this.population = population;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Tokyo", 37_000_000L));
        rows.add(new Row("Delhi", 30_000_000L));

        // next() sequence
        when(resultSet.next()).thenReturn(true, true, false);

        // City data returned
        when(resultSet.getString("CityName")).thenReturn(
                rows.get(0).name,
                rows.get(1).name
        );

        when(resultSet.getLong("Population")).thenReturn(
                rows.get(0).population,
                rows.get(1).population
        );

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            ThirtyFirstReport report = new ThirtyFirstReport(connection);

            // Act
            report.showPopulationOfAllCities();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("31_ThirtyFirstReport"),
                    eq("ThirtyFirstReport.md"),
                    argThat(md -> {

                        // Header check
                        if (!md.contains("The population of a city"))
                            return false;

                        // Validate rows
                        for (Row r : rows) {
                            long totalPop = r.population;
                            long cityPop = totalPop; // always 100%
                            long nonCity = 0;

                            double pctCity = 100.0;
                            double pctNonCity = 0.0;

                            if (!(md.contains(r.name)
                                    && md.contains(String.valueOf(totalPop))
                                    && md.contains(String.valueOf(cityPop))
                                    && md.contains(String.format("%.2f%%", pctCity))
                                    && md.contains(String.valueOf(nonCity))
                                    && md.contains(String.format("%.2f%%", pctNonCity))
                            )) {
                                return false;
                            }
                        }

                        return true;
                    })
            ));
        }
    }
}
