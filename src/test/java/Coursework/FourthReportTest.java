package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Unit test for FourthReport using Mockito.
 * Tests the 'showTopNCountries' method.
 */
@ExtendWith(MockitoExtension.class)
class FourthReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void showTopNCountries_generatesCorrectMarkdown() throws Exception {

        // Arrange
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        // When query is executed, return a fake ResultSet
        when(statement.executeQuery(startsWith("SELECT country.Code"))).thenReturn(resultSet);

        // Fake 2 countries returned, then stop
        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getString("Code")).thenReturn("CHN", "IND");
        when(resultSet.getString("Country")).thenReturn("China", "India");
        when(resultSet.getString("Continent")).thenReturn("Asia", "Asia");
        when(resultSet.getString("Region")).thenReturn("Eastern Asia", "Southern Asia");
        when(resultSet.getInt("Population")).thenReturn(1400000000, 1380000000);
        when(resultSet.getString("Capital")).thenReturn("Beijing", "New Delhi");

        int n = 2; // top 2

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            FourthReport report = new FourthReport(connection);

            // Act
            report.showTopNCountries(n);

            // Assert — verify ReportManager.writeMarkdown was called correctly
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("4_FourthReport"),
                    eq("2_Top_Populated_Countries_In_The_World.md"),
                    argThat(md ->
                            md.contains("# 2 top populated countries in the world") &&
                                    md.contains("CHN") &&
                                    md.contains("China") &&
                                    md.contains("Beijing") &&
                                    md.contains("IND") &&
                                    md.contains("India") &&
                                    md.contains("New Delhi")
                    )
            ));
        }
    }
}
