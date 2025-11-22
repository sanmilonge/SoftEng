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
class TwentySixthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSetTotal;

    @Mock
    ResultSet resultSetCity;

    @Test
    void showWorldPopulation_generatesDynamicMarkdown() throws Exception {

        // Mock DB setup
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // Two separate SQL queries:
        // 1. SUM(Population) from country
        // 2. SUM(Population) from city

        when(statement.executeQuery(startsWith("SELECT SUM(Population) AS WorldPopulation")))
                .thenReturn(resultSetTotal);

        when(statement.executeQuery(startsWith("SELECT SUM(city.Population) AS CityPopulation")))
                .thenReturn(resultSetCity);

        // Sample values
        long totalPop = 8_000_000_000L;   // Example world population
        long cityPop = 4_200_000_000L;    // Example city population

        when(resultSetTotal.next()).thenReturn(true);
        when(resultSetTotal.getLong("WorldPopulation")).thenReturn(totalPop);

        when(resultSetCity.next()).thenReturn(true);
        when(resultSetCity.getLong("CityPopulation")).thenReturn(cityPop);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            TwentySixthReport report = new TwentySixthReport(connection);

            // Act
            report.showWorldPopulation();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("26_TwentySixthReport"),
                    eq("TwentySixthReport.md"),
                    argThat(md -> {

                        long nonCity = totalPop - cityPop;
                        double pctCity = (cityPop * 100.0) / totalPop;
                        double pctNonCity = (nonCity * 100.0) / totalPop;

                        // Header check
                        if (!md.contains("The population of the world"))
                            return false;

                        // Value checks
                        return md.contains(String.valueOf(totalPop))
                                && md.contains(String.valueOf(cityPop))
                                && md.contains(String.format("%.2f%%", pctCity))
                                && md.contains(String.valueOf(nonCity))
                                && md.contains(String.format("%.2f%%", pctNonCity));
                    })
            ));
        }
    }
}
