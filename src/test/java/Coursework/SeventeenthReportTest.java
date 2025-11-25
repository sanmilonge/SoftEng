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
class SeventeenthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showCapitalCitiesInWorld_generatesDynamicMarkdown() throws Exception {

        // Connection + SQL statement mocking
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(startsWith("SELECT city.Name AS CityName")))
                .thenReturn(resultSet);

        // Row model to mirror DB results
        class Row {
            final String city;
            final String country;
            final int population;

            Row(String city, String country, int population) {
                this.city = city;
                this.country = country;
                this.population = population;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("CapitalA", "CountryA", 1_000_000));
        rows.add(new Row("CapitalB", "CountryB", 500_000));

        // ResultSet mock behaviour
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("CityName")).thenReturn(rows.get(0).city, rows.get(1).city);
        when(resultSet.getString("CountryName")).thenReturn(rows.get(0).country, rows.get(1).country);
        when(resultSet.getInt("Population")).thenReturn(rows.get(0).population, rows.get(1).population);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            SeventeenthReport report = new SeventeenthReport(connection);

            // Act
            report.showCapitalCitiesInWorld();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("17_SeventeenthReport"),
                    eq("SeventeenthReport.md"),
                    argThat(md -> {
                        // Header check
                        if (!md.contains("All the capital cities in the world organised by largest population to smallest"))
                            return false;

                        // Each row check
                        for (Row r : rows) {
                            if (!(md.contains(r.city)
                                    && md.contains(r.country)
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
