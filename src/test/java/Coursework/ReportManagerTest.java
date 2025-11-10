package Coursework; // make sure this matches your package name

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ReportManagerTest
 * -----------------
 * Unit test for the ReportManager class.
 *
 * What it checks:
 * 1. That the reports folder exists after preparation.
 * 2. That ReportManager.writeMarkdown() creates a new markdown file
 *    with a timestamp in its filename.
 * 3. That RunLog.md is created/updated when a report is generated.
 */
class ReportManagerTest {

    // Path to the directory where reports should be written
    static Path outDir;

    /**
     * Runs once before all tests.
     * It sets up the reports folder so every test starts clean.
     */
    @BeforeAll
    static void setUp() throws Exception {
        // Define the output folder (same as used in ReportManager)
        outDir = Paths.get("src", "main", "resources", "reports");

        // Prepare the folder (ReportManager deletes old files + recreates the folder)
        ReportManager.prepareReportFolder();

        // Verify that the folder actually exists before running tests
        assertTrue(Files.exists(outDir), "Report folder should exist");
    }

    /**
     * Test: writeMarkdown_createsTimestampedFile_andUpdatesLog
     * ---------------------------------------------------------
     * Checks that calling writeMarkdown() successfully:
     * 1. Creates a file named JUnitReport_YYYY-MM-dd_HH-mm.md
     * 2. Updates (or creates) RunLog.md to record the report generation.
     */
    @Test
    void writeMarkdown_createsTimestampedFile_andUpdatesLog() throws Exception {
        // Define a base filename (ReportManager will add a timestamp)
        String base = "JUnitReport.md";

        // Create a new markdown report using the ReportManager
        ReportManager.writeMarkdown(base, "# hello\ncontent");

        // AtomicBoolean is used so we can modify its value inside the lambda
        AtomicBoolean found = new AtomicBoolean(false);

        // Walk through all files in the reports folder to check for timestamped report
        try (var stream = Files.walk(outDir)) {
            stream.filter(Files::isRegularFile).forEach(p -> {
                String n = p.getFileName().toString();
                // Look for something like "JUnitReport_2025-11-10_02-15.md"
                if (n.startsWith("JUnitReport_") && n.endsWith(".md")) {
                    found.set(true);
                }
            });
        }

        //  Assert that a timestamped markdown file was created
        assertTrue(found.get(),
                "A timestamped markdown file should be created (JUnitReport_*.md)");

        // Assert that RunLog.md exists — meaning report generation was logged
        assertTrue(Files.exists(outDir.resolve("RunLog.md")),
                "RunLog.md should be created/updated after writing report");
    }
}
