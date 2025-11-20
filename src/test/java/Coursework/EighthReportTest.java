package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for EighthReport using Mockito.
 * Verifies that per-continent city reports are generated correctly.
 */
@ExtendWith(MockitoExtension.class)
class EighthReportTest extends ReportTestSupport {

    @Test
    void showCitiesContinent_generatesMarkdownForEachContinent() throws Exception {

        // Arrange full mock DB environment
        DBMock db = mockDBEnvironment();

        // Mock distinct continents
        mockRows(db.rs, new Object[][]{
                row(col("Continent", "Europe"))
        });

        // SECOND ResultSet for cities
        ResultSet cityRS = mock(ResultSet.class);

        when(db.sqlConn.prepareStatement(startsWith("SELECT city.Name")))
                .thenReturn(db.pstmt);

        when(db.pstmt.executeQuery()).thenReturn(cityRS);

        // Mock city rows
        when(cityRS.next()).thenReturn(true, true, false);
        when(cityRS.getString("City")).thenReturn("London", "Paris");
        when(cityRS.getString("Country")).thenReturn("United Kingdom", "France");
        when(cityRS.getString("District")).thenReturn("London", "Île-de-France");
        when(cityRS.getInt("Population")).thenReturn(9000000, 2148000);

        EighthReport report = new EighthReport(db.appConn);

        // Act + Assert in one try-with-resources block
        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            report.showCitiesContinent();

            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("8_EighthReport"),
                    eq("Europe.md"),
                    argThat(md ->
                            md.contains("# Cities in Europe") &&
                                    md.contains("London") &&
                                    md.contains("United Kingdom") &&
                                    md.contains("Paris") &&
                                    md.contains("France")
                    )
            ));
        }
    }
}
