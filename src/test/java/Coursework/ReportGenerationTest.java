package Coursework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ReportGenerationTest
 * ---------------------
 * This integration test verifies that reports are correctly generated and saved
 * when running the report-generating classes in the Coursework application.
 *
 * The tests interact with:
 *  - The actual database (via the Connection class)
 *  - The report generation classes (FirstReport, SeventhReport)
 *  - The file system (where markdown reports are saved)
 *
 * Therefore, this is an **integration test**, not a unit test.
 * It ensures the complete data flow from the database → report logic → file creation works properly.
 */
public class ReportGenerationTest {

    // A shared database connection object used by all tests.
    static Connection con;

    /**
     * @BeforeAll — runs once before any test method.
     *
     * Purpose:
     * - Establishes a connection to the MySQL container (world-db)
     * - Ensures the reports folder exists and is cleaned out
     *   before generating new files, keeping tests reproducible.
     */
    @BeforeAll
    static void setup() {
        con = new Connection();

        // Connect to the database on localhost port 33060.
        // The world-db container must be running (via docker-compose or CI workflow).
        con.connect("localhost:33060", 1000);

        // Prepare the folder used to store generated reports.
        ReportManager.prepareReportFolder();

        // Clean up old report files so tests start in a known state.
        cleanOldReports();
    }

    /**
     * Test 1 — Verify that the FirstReport class generates a markdown file.
     *
     * Steps:
     *  1. Instantiate the FirstReport object using the DB connection.
     *  2. Run its showCountriesByPopulation() method, which should trigger report generation.
     *  3. Check if a file starting with “FirstReport” exists in the reports folder.
     *
     * Expected:
     *  - A file named “FirstReport_<timestamp>.md” should be found after the method runs.
     */
    @Test
    void testFirstReportGeneratesFile() {
        FirstReport report = new FirstReport(con);
        report.showCountriesByPopulation();

        assertTrue(checkForFile("FirstReport"),
                "FirstReport.md file should exist after running report");
    }

    /**
     * Test 2 — Verify that the SeventhReport class generates its markdown file.
     *
     * Steps:
     *  1. Instantiate the SeventhReport class using the active DB connection.
     *  2. Run its showCitiesByPopulation() method.
     *  3. Verify that a file beginning with “SeventhReport” exists in the reports directory.
     *
     * Expected:
     *  - A file named “SeventhReport_<timestamp>.md” should be created successfully.
     */
    @Test
    void testSeventhReportGeneratesFile() {
        SeventhReport report = new SeventhReport(con);
        report.showCitiesByPopulation();

        assertTrue(checkForFile("SeventhReport"),
                "SeventhReport.md file should exist after running report");
    }

    // ---------------- Helper Methods ----------------

    /**
     * Deletes all files inside the reports folder to ensure a clean testing environment.
     *
     * Why:
     * - Prevents leftover reports from previous runs from affecting test results.
     * - Ensures that only files generated during the current test are detected.
     */
    private static void cleanOldReports() {
        Path dir = Paths.get("src/main/resources/reports");
        try {
            if (Files.exists(dir)) {
                // Walk through all files and delete them (ignores subdirectories)
                Files.walk(dir)
                        .filter(Files::isRegularFile)
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException ignored) {
                                // Ignore minor file deletion issues
                            }
                        });
            } else {
                // If folder doesn’t exist, create it
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            fail("Failed to prepare reports folder: " + e.getMessage());
        }
    }

    /**
     * Checks whether a file starting with the given base name exists in the reports folder.
     *
     * @param baseName The prefix of the expected report file (e.g. “FirstReport”)
     * @return true if the report file exists, false otherwise.
     *
     * Why:
     * - Ensures that report generation actually wrote a file.
     * - Allows flexible matching since filenames include timestamps.
     */
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
