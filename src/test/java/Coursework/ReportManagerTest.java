package Coursework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * ReportManagerTest
 * -----------------
 * This class tests the ReportManager class to make sure the report folder
 * and markdown reports are created properly.
 *
 * These tests check:
 * 1. If the report folder is created successfully.
 * 2. If markdown files are actually written into the folder.
 */

public class ReportManagerTest {
    // Path to the reports folder
    static final String REPORT_FOLDER = "src/main/resources/reports";

    @BeforeAll
    static void setup() {
        // Prepares (cleans and recreates) the report folder before all tests
        ReportManager.prepareReportFolder();
    }


    /**
     * Test 1 - Check if the reports folder is created.
     *
     * What it does:
     * - Looks for the folder path on the computer.
     * - Makes sure the folder actually exists and isn’t missing.
     * - Makes sure it’s a directory (not just a file).
     *
     * Why it matters:
     * - The program needs this folder to save all generated markdown reports.
     */

    @Test
    void testReportFolderCreated() {
        Path folderPath = Paths.get(REPORT_FOLDER);

        assertTrue(Files.exists(folderPath),
                "Report folder should exist after preparation");

        assertTrue(Files.isDirectory(folderPath),
                "Report folder path should be a directory");
    }

    /**
     * Test 2 - Check if a markdown file is created properly.
     *
     * What it does:
     * - Calls the writeMarkdown() method to create a new markdown file.
     * - Waits a bit so the file has time to save.
     * - Looks inside the reports folder to see if a new file exists.
     *
     * Why it matters:
     * - This proves that the writeMarkdown() function works correctly
     *   and that the system can actually save reports.
     */


    @Test
    void testWriteMarkdownCreatesFile() {
        String testContent = "# Sample Report\n\nTesting file generation.";
        ReportManager.writeMarkdown("JUnitReport.md", testContent);

        try {
            // Give file system a moment to complete the write
            Thread.sleep(1000);

            // Print all files in the folder for debugging
            System.out.println("Files in report folder:");
            Files.walk(Paths.get(REPORT_FOLDER))
                    .forEach(p -> System.out.println(" - " + p.getFileName()));

            boolean found = Files.walk(Paths.get(REPORT_FOLDER))
                    .anyMatch(p -> p.getFileName().toString().startsWith("JUnitReport"));

            assertTrue(found, "Markdown file should be created by writeMarkdown()");
        } catch (IOException e) {
            fail("Failed to verify markdown creation: " + e.getMessage());
        } catch (InterruptedException ignored) {
        }
    }
}
