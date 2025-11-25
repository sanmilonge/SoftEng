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
class TwentySecondReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement stmt;

    @Mock
    ResultSet rset;

    @Test
    void showTopNCapitalCitiesInRegion_generatesCorrectMarkdown() throws Exception {

        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(stmt);

        when(stmt.executeQuery(startsWith("SELECT city.Name"))).thenReturn(rset);

        // Fake result set: 1 row
        when(rset.next()).thenReturn(true, false);
        when(rset.getString("CityName")).thenReturn("Berlin");
        when(rset.getString("CountryName")).thenReturn("Germany");
        when(rset.getInt("Population")).thenReturn(3600000);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            TwentySecondReport report = new TwentySecondReport(connection);

            report.showTopNCapitalCitiesInRegion("Western Europe", 1);

            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("22_TwentySecondReport"),
                    eq("TwentySecondReport.md"),
                    argThat(md ->
                            md.contains("# Top 1 Top Populated Capital Cities in Region: Western Europe") &&
                                    md.contains("Berlin") &&
                                    md.contains("Germany") &&
                                    md.contains("3600000")
                    )
            ));
        }
    }
}
