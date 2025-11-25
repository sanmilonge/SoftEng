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
class TwentyNinthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showPopulationOfAllCountries_generatesDynamicMarkdown() throws Exception {

        // Mock DB connection + statement
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // Detect SQL execution
        when(statement.executeQuery(startsWith("SELECT country.Name AS CountryName")))
                .thenReturn(resultSet);

        // Mock sample rows (2 example countries)
        class Row {
            final String name;
            final long totalPop;
            final long cityPop;

            Row(String name, long totalPop, long cityPop) {
                this.name = name;
                this.totalPop = totalPop;
                this.cityPop = cityPop;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Japan", 125_000_000L, 90_000_000L));
        rows.add(new Row("Kenya", 55_000_000L, 15_000_000L));

        // next() pattern
        when(resultSet.next()).thenReturn(true, true, false);

        // Field mocks
        when(resultSet.getString("CountryName")).thenReturn(
                rows.get(0).name,
                rows.get(1).name
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

            TwentyNinthReport report = new TwentyNinthReport(connection);

            // Act
            report.showPopulationOfAllCountries();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("29_TwentyNinthReport"),
                    eq("TwentyNinthReport.md"),
                    argThat(md -> {

                        // Header check
                        if (!md.contains("The population of a country"))
                            return false;

                        // Validate each country row
                        for (Row r : rows) {
                            long nonCity = r.totalPop - r.cityPop;

                            double pctCity = (r.cityPop * 100.0) / r.totalPop;
                            double pctNonCity = (nonCity * 100.0) / r.totalPop;

                            if (!(md.contains(r.name)
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
