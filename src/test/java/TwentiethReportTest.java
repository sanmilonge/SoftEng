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
class TwentiethReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

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
