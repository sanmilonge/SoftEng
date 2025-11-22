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
class TwentySeventhReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showPopulationOfAllContinents_generatesDynamicMarkdown() throws Exception {

        // DB setup
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // SQL detection
        when(statement.executeQuery(startsWith("SELECT c.Continent AS Continent")))
                .thenReturn(resultSet);

        // Mock data for two continents
        class Row {
            final String continent;
            final long totalPop;
            final long cityPop;

            Row(String c, long t, long cp) {
                continent = c;
                totalPop = t;
                cityPop = cp;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Asia", 4_600_000_000L, 2_300_000_000L));
        rows.add(new Row("Africa", 1_300_000_000L, 600_000_000L));

        // ResultSet.next() call pattern
        when(resultSet.next()).thenReturn(true, true, false);

        // Mock values returned for each row
        when(resultSet.getString("Continent")).thenReturn(
                rows.get(0).continent,
                rows.get(1).continent
        );

        when(resultSet.getLong("TotalPopulation")).thenReturn(
                rows.get(0).totalPop,
                rows.get(1).totalPop
        );

        when(resultSet.getLong("CityPopulation")).thenReturn(
                rows.get(0).cityPop,
                rows.get(1).cityPop
        );

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            TwentySeventhReport report = new TwentySeventhReport(connection);

            // Act
            report.showPopulationOfAllContinents();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("27_TwentySeventhReport"),
                    eq("TwentySeventhReport.md"),
                    argThat(md -> {

                        // Header check
                        if (!md.contains("The population of a continent"))
                            return false;

                        // Validate each continent row
                        for (Row r : rows) {
                            long nonCity = r.totalPop - r.cityPop;
                            double pctCity = (r.cityPop * 100.0) / r.totalPop;
                            double pctNonCity = (nonCity * 100.0) / r.totalPop;

                            if (!(md.contains(r.continent)
                                    && md.contains(String.valueOf(r.totalPop))
                                    && md.contains(String.valueOf(r.cityPop))
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
