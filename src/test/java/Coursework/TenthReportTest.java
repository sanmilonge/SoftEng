package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement distinctCountryStatement;

    @Mock
    ResultSet countriesResult;

    @Mock
    PreparedStatement countryPreparedStatement;

    @Mock
    ResultSet countryCitiesResult;

    @Test
    void showCitiesByCountry_generatesMarkdownPerCountry() throws Exception {

        // Arrange: Return JDBC connection
        when(connection.getConnection()).thenReturn(sqlConnection);

        // ---- FIXED STUB: match real SQL in GetAll.getAllCountries() ----
        when(sqlConnection.createStatement()).thenReturn(distinctCountryStatement);
        when(distinctCountryStatement.executeQuery(startsWith("SELECT Name AS Country")))
                .thenReturn(countriesResult);

        // Return one country, then stop
        when(countriesResult.next()).thenReturn(true, false);
        when(countriesResult.getString("Country")).thenReturn("Nigeria");

        // Prepare city query
        when(sqlConnection.prepareStatement(startsWith("SELECT city.Name AS City")))
                .thenReturn(countryPreparedStatement);

        when(countryPreparedStatement.executeQuery()).thenReturn(countryCitiesResult);

        // Fake result rows
        when(countryCitiesResult.next()).thenReturn(true, false);
        when(countryCitiesResult.getString("City")).thenReturn("Lagos");
        when(countryCitiesResult.getString("Country")).thenReturn("Nigeria");
        when(countryCitiesResult.getString("District")).thenReturn("Lagos");
        when(countryCitiesResult.getInt("Population")).thenReturn(200);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            TenthReport report = new TenthReport(connection);

            // Act
            report.showCitiesByCountry();

            // Assert markdown
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("10_TenthReport"),
                    eq("Nigeria.md"),
                    argThat(md -> {

                        // Required content
                        if (!(md.contains("# Cities in Nigeria") &&
                                md.contains("Lagos") &&
                                md.contains("Nigeria")))
                            return false;

                        // Check population > 100
                        for (String line : md.split("\n")) {
                            if (line.contains("Lagos")) {
                                String pop = line.split("\\|")[4].trim();
                                return Integer.parseInt(pop) > 100;
                            }
                        }
                        return false;
                    })
            ));
        }
    }
}
