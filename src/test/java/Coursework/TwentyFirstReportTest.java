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
class TwentyFirstReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showTopNCapitalCitiesInContinent_generatesDynamicMarkdown() throws Exception {

        String continent = "Asia";
        int n = 2;

        // Connection + statement setup
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // ResultSet returned whenever SELECT for this report is executed
        when(statement.executeQuery(startsWith("SELECT city.Name AS CityName")))
                .thenReturn(resultSet);

        // Mock rows
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

        Row row1 = new Row("CapitalA", "CountryA", 1_200_000);
        Row row2 = new Row("CapitalB", "CountryB", 800_000);

        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getString("CityName")).thenReturn(row1.city, row2.city);
        when(resultSet.getString("CountryName")).thenReturn(row1.country, row2.country);
        when(resultSet.getInt("Population")).thenReturn(row1.population, row2.population);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            TwentyFirstReport report = new TwentyFirstReport(connection);

            // Act
            report.showTopNCapitalCitiesInContinent(continent, n);

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("21_TwentyFirstReport"),
                    eq("TwentyFirstReport.md"),
                    argThat(md -> {

                        // Header check
                        if (!md.contains("# Top " + n + " Populated Capital Cities in " + continent))
                            return false;

                        // Row checks
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
