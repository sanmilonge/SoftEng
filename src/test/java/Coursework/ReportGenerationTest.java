package Coursework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReportGenerationTest {

    static Connection con;

    @BeforeAll
    static void setup() {
        con = new Connection();
        //  supply location + delay (DB must be running on localhost:33060)
        con.connect("localhost:33060", 1000);

        // Ensure reports folder exists (and is clean for a stable test)
        ReportManager.prepareReportFolder();
        cleanOldReports();
    }

    @Test
    void testFirstReportGeneratesFile() {
        FirstReport report = new FirstReport(con);
        report.showCountriesByPopulation();

        assertTrue(checkForFile("FirstReport"),
                "FirstReport.md file should exist after running report");
    }

    @Test
    void testSeventhReportGeneratesFile() {
        SeventhReport report = new SeventhReport(con);
        report.showCitiesByPopulation();

        assertTrue(checkForFile("SeventhReport"),
                "SeventhReport.md file should exist after running report");
    }

    // --- helpers ---

    private static void cleanOldReports() {
        Path dir = Paths.get("src/main/resources/reports");
        try {
            if (Files.exists(dir)) {
                Files.walk(dir)
                        .filter(Files::isRegularFile)
                        .forEach(p -> {
                            try { Files.deleteIfExists(p); } catch (IOException ignored) {}
                        });
            } else {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            fail("Failed to prepare reports folder: " + e.getMessage());
        }
    }

    private boolean checkForFile(String baseName) {
        try {
            return Files.walk(Paths.get("src/main/resources/reports"))
                    .anyMatch(p -> p.getFileName().toString().startsWith(baseName));
        } catch (IOException e) {
            fail("Failed to check for " + baseName + " file: " + e.getMessage());
            return false;
        }
    }
}
