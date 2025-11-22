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
class TwentyEighthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showPopulationOfAllRegions_generatesDynamicMarkdown() throws Exception {

        // DB setup
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // Match SQL execution
        when(statement.executeQuery(startsWith("SELECT c.Region AS Region")))
                .thenReturn(resultSet);

        // Two example regions to mock
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

        // ResultSet.next() pattern
        when(resultSet.next()).thenReturn(true, true, false);

        // Values per row
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

            TwentyEighthReport report = new TwentyEighthReport(connection);

            // Act
            report.showPopulationOfAllRegions();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("28_TwentyEighthReport"),
                    eq("TwentyEighthReport.md"),
                    argThat(md -> {

                        // Header check
                        if (!md.contains("The population of a region"))
                            return false;

                        // Validate rows
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
