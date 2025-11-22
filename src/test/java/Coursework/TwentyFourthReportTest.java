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
class TwentyFourthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showRegionPopulationSummary_generatesDynamicMarkdown() throws Exception {

        // Set up DB connection and statement mock
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // SQL detection
        when(statement.executeQuery(startsWith("SELECT c.Region")))
                .thenReturn(resultSet);

        // Mock region result rows
        class Row {
            final String region;
            final long totalPop;
            final long cityPop;

            Row(String region, long totalPop, long cityPop) {
                this.region = region;
                this.totalPop = totalPop;
                this.cityPop = cityPop;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Western Europe", 400_000_000L, 310_000_000L));
        rows.add(new Row("Eastern Africa", 270_000_000L, 80_000_000L));

        // next() behavior
        when(resultSet.next()).thenReturn(true, true, false);

        // Value mappings
        when(resultSet.getString("Region")).thenReturn(
                rows.get(0).region,
                rows.get(1).region
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

            TwentyFourthReport report = new TwentyFourthReport(connection);

            // Act
            report.showRegionPopulationSummary();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("24_TwentyFourthReport"),
                    eq("TwentyFourthReport.md"),
                    argThat(md -> {

                        // Check header
                        if (!md.contains("The population of people, people living in cities, and people not living in cities in each region"))
                            return false;

                        // Check each row’s output
                        for (Row r : rows) {
                            long nonCity = r.totalPop - r.cityPop;

                            double pctCity = (r.cityPop * 100.0) / r.totalPop;
                            double pctNonCity = (nonCity * 100.0) / r.totalPop;

                            if (!(md.contains(r.region)
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
