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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;
/**
 * Unit test for FirstReport using Mockito to mock DB and ReportManager.
 */
@ExtendWith(MockitoExtension.class)
class FirstReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showCountriesByPopulation_buildsMarkdownAndCallsReportManager() throws Exception {
        // Arrange DB mocks
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // Mock query
        when(statement.executeQuery(startsWith("SELECT country.Code"))).thenReturn(resultSet);

        // ---- Dynamic mock data ----
        class Row {
            final String code;
            final String name;
            final String continent;
            final String region;
            final String capital;
            final int population;
            Row(String c, String n, String con, String r, int p, String cap) {
                code = c; name = n; continent = con; region = r; population = p; capital = cap;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("AAA", "CountryA", "TestContinent", "RegionA", 5000000, "CapitalA"));
        rows.add(new Row("BBB", "CountryB", "TestContinent", "RegionB", 3000000, "CapitalB"));

        // Make resultSet.next() return true for each row, then false
        when(resultSet.next()).thenReturn(true, true, false);

        // Dynamic per-row stubbing
        when(resultSet.getString("Code")).thenReturn(
                rows.get(0).code,
                rows.get(1).code
        );
        when(resultSet.getString("Country")).thenReturn(
                rows.get(0).name,
                rows.get(1).name
        );
        when(resultSet.getString("Continent")).thenReturn(
                rows.get(0).continent,
                rows.get(1).continent
        );
        when(resultSet.getString("Region")).thenReturn(
                rows.get(0).region,
                rows.get(1).region
        );
        when(resultSet.getInt("Population")).thenReturn(
                rows.get(0).population,
                rows.get(1).population
        );
        when(resultSet.getString("Capital")).thenReturn(
                rows.get(0).capital,
                rows.get(1).capital
        );



        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            FirstReport report = new FirstReport(connection);

            // Act
            report.showCountriesByPopulation();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("1_FirstReport"),
                    eq("FirstReport.md"),
                    argThat(md -> {
                        // Header check
                        if (!md.contains("# All the countries in the world organised by population")) return false;
                        // Check every mocked row appears
                        for (Row r : rows) {
                            if (!(md.contains(r.code) &&
                                    md.contains(r.name) &&
                                    md.contains(r.continent) &&
                                    md.contains(r.region) &&
                                    md.contains(String.valueOf(r.population)) &&
                                    md.contains(r.capital))) {
                                return false;
                            }
                        }
                        return true;
                            }
                    )
            ));
        }
    }
}
