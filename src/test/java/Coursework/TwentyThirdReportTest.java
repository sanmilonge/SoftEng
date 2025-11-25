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

    @ExtendWith(MockitoExtension.class)
    static
    class TwentiethReportTest extends ReportTestSupport {

        @Mock
        Connection connection;

        @Mock
        java.sql.Connection sqlConnection;

        @Mock
        Statement statement;

        @Mock
        ResultSet resultSet;

        @Test
        void showTopNCapitalCities_generatesDynamicMarkdown() throws Exception {

            int n = 2;

            // DB mocking
            when(connection.getConnection()).thenReturn(sqlConnection);
            when(sqlConnection.createStatement()).thenReturn(statement);

            // SQL detection (starts with the SELECT in the report)
            when(statement.executeQuery(startsWith("SELECT city.Name AS CityName")))
                    .thenReturn(resultSet);

            // Mock 2 return rows
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

            when(resultSet.next()).thenReturn(true, true, false);
            when(resultSet.getString("CityName")).thenReturn(row1.city, row2.city);
            when(resultSet.getString("CountryName")).thenReturn(row1.country, row2.country);
            when(resultSet.getInt("Population")).thenReturn(row1.population, row2.population);

            try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

                TwentiethReport report = new TwentiethReport(connection);

                // Act
                report.showTopNCapitalCities(n);

                // Assert
                rm.verify(() -> ReportManager.writeMarkdown(
                        eq("20_TwentiethReport"),
                        eq("TwentiethReport.md"),
                        argThat(md -> {

                            // Header check
                            if (!md.contains("# Top " + n + " Populated Capital Cities in the World"))
                                return false;

                            // Row check
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

    @ExtendWith(MockitoExtension.class)
    static
    class TwentySecondReportTest extends ReportTestSupport {

        @Mock
        Connection connection;

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
}
