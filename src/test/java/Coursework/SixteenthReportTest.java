// src/test/java/Coursework/SixteenthReportTest.java
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
 * Unit test for SixteenthReport – top N cities in a district.
 */
@ExtendWith(MockitoExtension.class)
class SixteenthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    PreparedStatement preparedStatement;

    @Mock
    ResultSet resultSet;

    @Test
    void showTopNCitiesInDistrict_generatesCorrectMarkdown() throws Exception {

        String testDistrict = "California";

        // Mock connection
        when(connection.getConnection()).thenReturn(sqlConnection);

        // Mock prepared statement + query execution
        when(sqlConnection.prepareStatement(startsWith("SELECT city.Name AS City"))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Mock two example rows
        class Row {
            String city, country, district;
            int population;
            Row(String city, String country, String district, int population) {
                this.city = city;
                this.country = country;
                this.district = district;
                this.population = population;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Los Angeles", "United States", "California", 3694820));
        rows.add(new Row("San Diego", "United States", "California", 1223400));

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("City")).thenReturn(rows.get(0).city, rows.get(1).city);
        when(resultSet.getString("Country")).thenReturn(rows.get(0).country, rows.get(1).country);
        when(resultSet.getString("District")).thenReturn(rows.get(0).district, rows.get(1).district);
        when(resultSet.getInt("Population")).thenReturn(rows.get(0).population, rows.get(1).population);

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            SixteenthReport report = new SixteenthReport(connection);

            // Act
            report.showTopNCitiesInDistrict(2, testDistrict);

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("16_SixteenthReport"),
                    eq("2_Top_Populated_Cities_In_" + testDistrict + ".md"),
                    argThat(md -> {
                        // Header checks
                        if (!md.contains("Top 2 populated cities in " + testDistrict)) return false;
                        if (!md.contains("| City | Country | District | Population |")) return false;

                        // Row checks
                        for (Row r : rows) {
                            if (!(md.contains(r.city)
                                    && md.contains(r.country)
                                    && md.contains(r.district)
                                    && md.contains(String.valueOf(r.population)))) {
                                return false;
                            }
                        }
                        return true;
                    })
            ));
        }
    }
}
