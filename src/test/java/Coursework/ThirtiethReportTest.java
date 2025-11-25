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
class ThirtiethReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showPopulationOfAllDistricts_generatesDynamicMarkdown() throws Exception {

        // DB setup
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // Match SQL pattern for this report
        when(statement.executeQuery(startsWith("SELECT city.District AS District")))
                .thenReturn(resultSet);

        // Example mock rows
        class Row {
            final String district;
            final long totalPop;
            Row(String district, long totalPop) {
                this.district = district;
                this.totalPop = totalPop;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Tokyo-to", 14_000_000L));
        rows.add(new Row("Nairobi District", 3_500_000L));

        // ResultSet iteration
        when(resultSet.next()).thenReturn(true, true, false);

        // Provide values for each row
        when(resultSet.getString("District")).thenReturn(
                rows.get(0).district,
                rows.get(1).district
        );

        when(resultSet.getLong("TotalPopulation")).thenReturn(
                rows.get(0).totalPop,
                rows.get(1).totalPop
        );

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            ThirtiethReport report = new ThirtiethReport(connection);

            // Act
            report.showPopulationOfAllDistricts();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("30_ThirtiethReport"),
                    eq("ThirtiethReport.md"),
                    argThat(md -> {

                        // Check header
                        if (!md.contains("The population of a district"))
                            return false;

                        // Validate each district row
                        for (Row r : rows) {

                            long cityPop = r.totalPop; // always 100%
                            long nonCity = 0;
                            double pctCity = 100.0;
                            double pctNonCity = 0.0;

                            if (!(md.contains(r.district)
                                    && md.contains(String.valueOf(r.totalPop))
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
