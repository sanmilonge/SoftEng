package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Statement;
import java.sql.ResultSet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwentiethReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement stmt;

    @Mock
    ResultSet rset;

    @Test
    void showTopNCapitalCities_generatesCorrectMarkdown() throws Exception {

        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(stmt);

        when(stmt.executeQuery(startsWith("SELECT city.Name"))).thenReturn(rset);

        // Fake result set: 2 rows
        when(rset.next()).thenReturn(true, true, false);
        when(rset.getString("CityName")).thenReturn("London", "Tokyo");
        when(rset.getString("CountryName")).thenReturn("United Kingdom", "Japan");
        when(rset.getInt("Population")).thenReturn(8900000, 37000000);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            TwentiethReport report = new TwentiethReport(connection);

            report.showTopNCapitalCities(2);

            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("20_TwentiethReport"),
                    eq("TwentiethReport.md"),
                    argThat(md ->
                            md.contains("# Top 2 Populated Capital Cities in the World") &&
                                    md.contains("London") &&
                                    md.contains("United Kingdom") &&
                                    md.contains("8900000") &&
                                    md.contains("Tokyo") &&
                                    md.contains("Japan") &&
                                    md.contains("37000000")
                    )
            ));
        }
    }
}
