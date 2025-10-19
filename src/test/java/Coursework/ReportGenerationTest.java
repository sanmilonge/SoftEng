package Coursework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ReportGenerationTest
 * --------------------
 * This class tests the report generation part of the project.
 *
 * It checks that:
 * 1. The FirstReport and SeventhReport classes both run successfully.
 * 2. Each one produces a Markdown (.md) file inside the reports folder.
 *
 * Basically, it makes sure that when the program runs a report,
 * it actually creates the output files that should be generated.
 */


public class ReportGenerationTest
{
    static Connection con;

    @BeforeAll
    static void setup()
    {
        con = new Connection();
        con.connect();
        ReportManager.prepareReportFolder();
    }

    /**
     * Test 1 - Check that FirstReport creates a Markdown file.
     *
     * What it does:
     * - Runs the FirstReport (countries by population).
     * - Checks if a file starting with “FirstReport” exists in the reports folder.
     *
     * Why it matters:
     * - Proves that the report method not only runs but also
     *   saves a file successfully.
     */

    @Test
    void testFirstReportGeneratesFile()
    {
        FirstReport report = new FirstReport(con);
        report.showCountriesByPopulation();

        boolean fileExists = checkForFile("FirstReport");
        assertTrue(fileExists, "FirstReport.md file should exist after running report");
    }
    /**
     * Test 2 - Check that SeventhReport creates a Markdown file.
     *
     * What it does:
     * - Runs the SeventhReport (cities by population).
     * - Checks if a file starting with “SeventhReport” exists in the reports folder.
     *
     * Why it matters:
     * - Makes sure other reports work too — not just the first one.
     */
    @Test
    void testSeventhReportGeneratesFile()
    {
        SeventhReport report = new SeventhReport(con);
        report.showCitiesByPopulation();

        boolean fileExists = checkForFile("SeventhReport");
        assertTrue(fileExists, "SeventhReport.md file should exist after running report");
    }

    private boolean checkForFile(String baseName)
    {
        try {
            return Files.walk(Paths.get("src/main/resources/reports"))
                    .anyMatch(p -> p.getFileName().toString().startsWith(baseName));
        } catch (IOException e) {
            fail("Failed to check for " + baseName + " file: " + e.getMessage());
            return false;
        }
    }
}
