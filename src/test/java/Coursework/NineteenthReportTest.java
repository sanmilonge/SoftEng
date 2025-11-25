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
class NineteenthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showCapitalCitiesForMultipleRegions_generatesDynamicMarkdown() throws Exception {

        // Regions in the report
        String[] regions = {
                "Caribbean",
                "Western Europe",
                "Middle East",
                "Southern Africa"
        };

        // DB connection setup
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // Each region's SQL returns the same resultSet
        when(statement.executeQuery(startsWith("SELECT city.Name AS CityName")))
                .thenReturn(resultSet);

        // Sample rows used for all regions
        class Row {
            final String city;
            final String country;
            final int population;
            Row(String c, String n, int p) {
                city = c; country = n; population = p;
            }
        }

        Row row1 = new Row("CapitalA", "CountryA", 900_000);
        Row row2 = new Row("CapitalB", "CountryB", 400_000);

        // ResultSet.next() returns 2 rows per region, then false
        when(resultSet.next()).thenReturn(
                true, true, false,   // Caribbean
                true, true, false,   // Western Europe
                true, true, false,   // Middle East
                true, true, false    // Southern Africa
        );

        // Populate ResultSet values (same two rows for all regions)
        when(resultSet.getString("CityName")).thenReturn(
                row1.city, row2.city,
                row1.city, row2.city,
                row1.city, row2.city,
                row1.city, row2.city
        );

        when(resultSet.getString("CountryName")).thenReturn(
                row1.country, row2.country,
                row1.country, row2.country,
                row1.country, row2.country,
                row1.country, row2.country
        );

        when(resultSet.getInt("Population")).thenReturn(
                row1.population, row2.population,
                row1.population, row2.population,
                row1.population, row2.population,
                row1.population, row2.population
        );

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            NineteenthReport report = new NineteenthReport(connection);

            // Act
            report.showCapitalCitiesForMultipleRegions();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("19_NineteenthReport"),
                    eq("NineteenthReport.md"),
                    argThat(md -> {

                        // Main header check
                        if (!md.contains("All the capital cities in a region organised by largest to smallest"))
                            return false;

                        // Check each region title + row content
                        for (String region : regions) {
                            if (!md.contains("## Region: " + region))
                                return false;

                            if (!(md.contains(row1.city)
                                    && md.contains(row1.country)
                                    && md.contains(String.valueOf(row1.population))
                                    && md.contains(row2.city)
                                    && md.contains(row2.country)
                                    && md.contains(String.valueOf(row2.population))
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
