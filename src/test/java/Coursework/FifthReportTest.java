package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Dynamic unit test for FifthReport – top countries by continent.
 */
@ExtendWith(MockitoExtension.class)
class FifthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    PreparedStatement preparedStatement;

    @Mock
    ResultSet resultSet;

    @Test
    void showTopNCountriesInContinent_generatesDynamicMarkdown() throws Exception {
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        class Row {
            String code, name, continent, region, capital;
            int population;
            Row(String c, String n, String con, String r, int p, String cap) {
                code = c; name = n; continent = con; region = r; population = p; capital = cap;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("AAA", "CountryA", "TestContinent", "RegionA", 5_000_000, "CapitalA"));
        rows.add(new Row("BBB", "CountryB", "TestContinent", "RegionB", 3_000_000, "CapitalB"));

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("Code")).thenReturn(rows.get(0).code, rows.get(1).code);
        when(resultSet.getString("Country")).thenReturn(rows.get(0).name, rows.get(1).name);
        when(resultSet.getString("Continent")).thenReturn(rows.get(0).continent, rows.get(1).continent);
        when(resultSet.getString("Region")).thenReturn(rows.get(0).region, rows.get(1).region);
        when(resultSet.getInt("Population")).thenReturn(rows.get(0).population, rows.get(1).population);
        when(resultSet.getString("Capital")).thenReturn(rows.get(0).capital, rows.get(1).capital);

        int n = 2;
        String continent = "TestContinent";

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {
            FifthReport report = new FifthReport(connection);
            report.showTopNCountriesInContinent(n, continent);

            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("5_FifthReport"),
                    eq("2_Top_Populated_Countries_In_TestContinent.md"),
                    argThat(md -> {
                        if (!md.contains("# 2 top populated countries in TestContinent")) return false;
                        for (Row r : rows) {
                            if (!(md.contains(r.code)
                                    && md.contains(r.name)
                                    && md.contains(r.continent)
                                    && md.contains(r.region)
                                    && md.contains(String.valueOf(r.population))
                                    && md.contains(r.capital))) {
                                return false;
                            }
                        }
                        return true;
                    })
            ));
        }
    }
}
