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

/**
 * Unit test for NinthReport using Mockito.
 */

@ExtendWith(MockitoExtension.class)
class NinthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement distinctRegionStatement;

    @Mock
    ResultSet regionsResult;

    @Mock
    PreparedStatement regionPreparedStatement;

    @Mock
    ResultSet regionCitiesResult;

    @Test
    void showCitiesByRegion_generatesMarkdownPerRegion() throws Exception {

        // Arrange
        when(connection.getConnection()).thenReturn(sqlConnection);

        // Distinct regions
        when(sqlConnection.createStatement()).thenReturn(distinctRegionStatement);
        when(distinctRegionStatement.executeQuery(startsWith("SELECT DISTINCT")))
                .thenReturn(regionsResult);

        when(regionsResult.next()).thenReturn(true, false);
        when(regionsResult.getString("Region")).thenReturn("Western Europe");

        // Per-region SQL
        when(sqlConnection.prepareStatement(startsWith("SELECT city.Name AS City")))
                .thenReturn(regionPreparedStatement);

        when(regionPreparedStatement.executeQuery()).thenReturn(regionCitiesResult);

        // Fake rows
        when(regionCitiesResult.next()).thenReturn(true, false);

        when(regionCitiesResult.getString("City")).thenReturn("London");
        when(regionCitiesResult.getString("Country")).thenReturn("United Kingdom");
        when(regionCitiesResult.getString("District")).thenReturn("London");
        when(regionCitiesResult.getInt("Population")).thenReturn(9000000);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            NinthReport report = new NinthReport(connection);

            // Act
            report.showCitiesByRegion();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("9_NinthReport"),
                    eq("Western Europe.md"),
                    argThat(md ->
                            md.contains("# Cities in Western Europe") &&
                                    md.contains("London") &&
                                    md.contains("United Kingdom") &&
                                    md.contains("9000000")
                    )
            ));
        }
    }
}
