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
class TwentyThirdReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showContinentPopulationSummary_generatesDynamicMarkdown() throws Exception {

        // DB connection mocking
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // Return ResultSet when the large SQL starts with SELECT c.Continent
        when(statement.executeQuery(startsWith("SELECT c.Continent")))
                .thenReturn(resultSet);

        // Mock 2 continents
        class Row {
            final String continent;
            final long totalPopulation;
            final long cityPopulation;
            Row(String continent, long totalPop, long cityPop) {
                this.continent = continent;
                this.totalPopulation = totalPop;
                this.cityPopulation = cityPop;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Asia", 4_600_000_000L, 2_300_000_000L));
        rows.add(new Row("Europe", 750_000_000L, 550_000_000L));

        // next() calls
        when(resultSet.next()).thenReturn(true, true, false);

        // Mock returned row values
        when(resultSet.getString("Continent")).thenReturn(
                rows.get(0).continent,
                rows.get(1).continent
        );

        when(resultSet.getLong("TotalPopulation")).thenReturn(
                rows.get(0).totalPopulation,
                rows.get(1).totalPopulation
        );

        when(resultSet.getLong("CityPopulation")).thenReturn(
                rows.get(0).cityPopulation,
                rows.get(1).cityPopulation
        );

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            TwentyThirdReport report = new TwentyThirdReport(connection);

            // Act
            report.showContinentPopulationSummary();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("23_TwentyThirdReport"),
                    eq("TwentyThirdReport.md"),
                    argThat(md -> {

                        // Header check
                        if (!md.contains("The population of people, people living in cities, and people not living in cities in each continent"))
                            return false;

                        // Check each row’s values + computed values
                        for (Row r : rows) {

                            long nonCity = r.totalPopulation - r.cityPopulation;
                            double pctCity = (r.cityPopulation * 100.0) / r.totalPopulation;
                            double pctNonCity = (nonCity * 100.0) / r.totalPopulation;

                            if (!(md.contains(r.continent)
                                    && md.contains(String.valueOf(r.totalPopulation))
                                    && md.contains(String.valueOf(r.cityPopulation))
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
