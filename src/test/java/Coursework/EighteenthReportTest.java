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
class EighteenthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showCapitalCitiesInMultipleContinents_generatesDynamicMarkdown() throws Exception {

        // Continents expected by the report
        String[] continents = {
                "Asia",
                "Europe",
                "Africa",
                "North America",
                "South America",
                "Oceania"
        };

        // Mock DB connection + statement
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // Every continent query returns the same resultSet mock
        when(statement.executeQuery(startsWith("SELECT city.Name AS CityName")))
                .thenReturn(resultSet);

        // Mock 2 rows for each continent
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

        Row row1 = new Row("CapitalA", "CountryA", 1_000_000);
        Row row2 = new Row("CapitalB", "CountryB", 500_000);

        // Each executeQuery call must iterate through the same 2 rows
        when(resultSet.next()).thenReturn(
                true, true, false,     // For Asia
                true, true, false,     // For Europe
                true, true, false,     // For Africa
                true, true, false,     // For North America
                true, true, false,     // For South America
                true, true, false      // For Oceania
        );

        // Return row values in pairs for each continent
        when(resultSet.getString("CityName")).thenReturn(
                row1.city, row2.city,   // Asia
                row1.city, row2.city,   // Europe
                row1.city, row2.city,   // Africa
                row1.city, row2.city,   // North America
                row1.city, row2.city,   // South America
                row1.city, row2.city    // Oceania
        );

        when(resultSet.getString("CountryName")).thenReturn(
                row1.country, row2.country,
                row1.country, row2.country,
                row1.country, row2.country,
                row1.country, row2.country,
                row1.country, row2.country,
                row1.country, row2.country
        );

        when(resultSet.getInt("Population")).thenReturn(
                row1.population, row2.population,
                row1.population, row2.population,
                row1.population, row2.population,
                row1.population, row2.population,
                row1.population, row2.population,
                row1.population, row2.population
        );

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            EighteenthReport report = new EighteenthReport(connection);

            // Act
            report.showCapitalCitiesInMultipleContinents();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("18_EighteenthReport"),
                    eq("EighteenthReport.md"),
                    argThat(md -> {

                        // Check global header
                        if (!md.contains("All the capital cities in a continent organised by largest population to smallest"))
                            return false;

                        // Check each continent section + rows
                        for (String continent : continents) {
                            if (!md.contains("## Continent: " + continent))
                                return false;

                            if (!(md.contains(row1.city)
                                    && md.contains(row1.country)
                                    && md.contains(String.valueOf(row1.population))
                                    && md.contains(row2.city)
                                    && md.contains(row2.country)
                                    && md.contains(String.valueOf(row2.population)))) {
                                return false;
                            }
                        }

                        return true;
                    })
            ));
        }
    }
}
