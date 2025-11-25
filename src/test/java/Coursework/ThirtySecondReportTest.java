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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThirtySecondReportTest extends ReportTestSupport {

    @Mock
    Coursework.Connection connection;

    @Mock
    java.sql.Connection sqlConnection;

    @Mock
    Statement statement;

    @Mock
    ResultSet worldResultSet;

    @Mock
    ResultSet langResultSet;

    @Test
    void showLanguageSpeakers_generatesDynamicMarkdown() throws Exception {

        // World population query
        when(connection.getConnection()).thenReturn(sqlConnection);
        when(sqlConnection.createStatement()).thenReturn(statement);

        when(statement.executeQuery(startsWith("SELECT SUM(Population) AS WorldPopulation")))
                .thenReturn(worldResultSet);

        long worldPop = 8_000_000_000L; // mock world population

        when(worldResultSet.next()).thenReturn(true);
        when(worldResultSet.getLong("WorldPopulation")).thenReturn(worldPop);

        // Language speaker query
        when(statement.executeQuery(startsWith("SELECT cl.Language")))
                .thenReturn(langResultSet);

        class Row {
            final String language;
            final long speakers;

            Row(String language, long speakers) {
                this.language = language;
                this.speakers = speakers;
            }
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Chinese", 1_200_000_000L));
        rows.add(new Row("English", 900_000_000L));

        when(langResultSet.next()).thenReturn(true, true, false);

        when(langResultSet.getString("Language")).thenReturn(
                rows.get(0).language,
                rows.get(1).language
        );

        when(langResultSet.getLong("TotalSpeakers")).thenReturn(
                rows.get(0).speakers,
                rows.get(1).speakers
        );

        try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) {

            ThirtySecondReport report = new ThirtySecondReport(connection);

            // Act
            report.showLanguageSpeakers();

            // Assert
            rm.verify(() -> ReportManager.writeMarkdown(
                    eq("32_ThirtySecondReport"),
                    eq("ThirtySecondReport.md"),
                    argThat(md -> {

                        // Header check
                        if (!md.contains("Number of People Who Speak Selected Languages"))
                            return false;

                        // Validate rows
                        for (Row r : rows) {
                            double pct = (r.speakers * 100.0) / worldPop;

                            if (!(md.contains(r.language)
                                    && md.contains(String.valueOf(r.speakers))
                                    && md.contains(String.format("%.2f%%", pct))
                            )) {
                                return false;
                            }
                        }

                        return true;
                    })
            ));
        }
    }
}
