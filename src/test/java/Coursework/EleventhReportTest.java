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
class EleventhReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement distinctDistrictStatement;

    @Mock
    ResultSet districtsResult;

    @Mock
    PreparedStatement districtPreparedStatement;

    @Mock
    ResultSet districtCitiesResult;

    @Test
    void showCitiesByDistrict_generatesMarkdownPerDistrict() throws Exception {

        when(connection.getConnection()).thenReturn(sqlConnection);

        // Distinct districts
        when(sqlConnection.createStatement()).thenReturn(distinctDistrictStatement);
        when(distinctDistrictStatement.executeQuery(startsWith(
                "SELECT DISTINCT District"
        ))).thenReturn(districtsResult);

        when(districtsResult.next()).thenReturn(true, false);
        when(districtsResult.getString("District")).thenReturn("Lagos");

        // Per-district SQL
        when(sqlConnection.prepareStatement(startsWith("SELECT city.Name AS City")))
                .thenReturn(districtPreparedStatement);

        when(districtPreparedStatement.executeQuery()).thenReturn(districtCitiesResult);

        when(districtCitiesResult.next()).thenReturn(true, false);
        when(districtCitiesResult.getString("City")).thenReturn("Lagos");
        when(districtCitiesResult.getString("Country")).thenReturn("Nigeria");
        when(districtCitiesResult.getString("District")).thenReturn("Lagos");
        when(districtCitiesResult.getInt("Population")).thenReturn(200);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            EleventhReport report = new EleventhReport(connection);

            // Act
            report.showCitiesByDistrict();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("11_EleventhReport"),
                    eq("Lagos.md"),
                    argThat(md -> {

                        // Contains expected strings
                        if (!md.contains("# Cities in Lagos")) return false;
                        if (!md.contains("Lagos")) return false;
                        if (!md.contains("Nigeria")) return false;

                        // Population > 1 check
                        for (String line : md.split("\n")) {
                            if (line.contains("| Lagos |")) {
                                String pop = line.split("\\|")[4].trim();
                                return Integer.parseInt(pop) > 1;
                            }
                        }
                        return false;
                    })
            ));
        }
    }
}
