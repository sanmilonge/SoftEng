package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwentySecondReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showTopNCapitalCitiesInRegion_generatesDynamicMarkdown() throws Exception {

        String region = "Caribbean";
        int n = 2;

        // DB connection + statement mocking
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // SQL query detection
        when(statement.executeQuery(startsWith("SELECT city.Name AS CityName")))
                .thenReturn(resultSet);

        // Sample rows
        class Row {
            final String city;
            final String country;
            final int population;

            Row(String city, String country, int pop) {
                this.city = city;
                this.country = country;
                this.population = pop;
            }
        }

        Row row1 = new Row("CapitalA", "CountryA", 1_500_000);
        Row row2 = new Row("CapitalB", "CountryB", 700_000);

        // Two rows → then no more
        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getString("CityName")).thenReturn(row1.city, row2.city);
        when(resultSet.getString("CountryName")).thenReturn(row1.country, row2.country);
        when(resultSet.getInt("Population")).thenReturn(row1.population, row2.population);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            TwentySecondReport report = new TwentySecondReport(connection);

            // Act
            report.showTopNCapitalCitiesInRegion(region, n);

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("22_TwentySecondReport"),
                    eq("TwentySecondReport.md"),
                    argThat(md -> {

                        // Header check
                        if (!md.contains("# Top " + n +
                                " Top Populated Capital Cities in Region: " + region))
                            return false;

                        // Row content check
                        return md.contains(row1.city)
                                && md.contains(row1.country)
                                && md.contains(String.valueOf(row1.population))
                                && md.contains(row2.city)
                                && md.contains(row2.country)
                                && md.contains(String.valueOf(row2.population));
                    })
            ));
        }
    }
}
