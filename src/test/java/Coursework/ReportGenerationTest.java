package Coursework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ReportGenerationTest
 * ---------------------
 * Integration test that checks First, Third, and Seventh report generation.
 *
 * Reports are saved in `reports/TestReports` so production data is untouched.
 */
public class ReportGenerationTest {

    static Connection con;

    @BeforeAll
    static void setup() {
        System.setProperty("report.folder", "src/main/resources/reports/TestReports");

        con = new Connection();
        con.connect("localhost:33060", 1000);

        ReportManager.prepareReportFolder();
    }

    @Test
    void testFirstReportGeneratesFile() {
        FirstReport report = new FirstReport(con);
        report.showCountriesByPopulation();

        assertTrue(checkForFile("1_FirstReport", "FirstReport"), "FirstReport.md should exist in its folder");
    }

    @Test
    void testThirdReportGeneratesFiles() {
        ThirdReport report = new ThirdReport(con);
        report.showCountriesByRegion();

        Path dir = Paths.get("src/main/resources/reports/TestReports/3_ThirdReport");
        assertTrue(Files.exists(dir), "3_ThirdReport folder should exist");

        try {
            boolean hasMd = Files.walk(dir).anyMatch(p -> p.getFileName().toString().endsWith(".md"));
            assertTrue(hasMd, "At least one region markdown file should be created");
        } catch (IOException e) {
            fail("Error checking for ThirdReport markdown files: " + e.getMessage());
        }
    }

    @Test
    void testSeventhReportGeneratesFile() {
        SeventhReport report = new SeventhReport(con);
        report.showCitiesByPopulation();

        assertTrue(checkForFile("7_SeventhReport", "SeventhReport"), "SeventhReport.md should exist in its folder");
    }

    private boolean checkForFile(String subfolder, String baseName) {
        try {
            return Files.walk(Paths.get("src/main/resources/reports/TestReports", subfolder))
                    .anyMatch(p -> p.getFileName().toString().startsWith(baseName));
        } catch (IOException e) {
            fail("Failed to check for " + baseName + " file: " + e.getMessage());
            return false;
        }
    }
}