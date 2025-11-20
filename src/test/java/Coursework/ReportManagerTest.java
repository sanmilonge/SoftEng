package Coursework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReportManager.
 * Uses the TestReports folder so production reports are never touched.
 */
class ReportManagerTest extends ReportTestSupport {

    @BeforeEach
    void resetFolder() throws Exception {
        // Force ReportManager to use TestReports
        System.setProperty("report.folder", TEST_REPORT_ROOT.toString());

        // Ensure TestReports exists and is empty
        if (Files.exists(TEST_REPORT_ROOT)) {
            Files.walk(TEST_REPORT_ROOT)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try { Files.deleteIfExists(p); } catch (Exception ignored) {}
                    });
        }
        Files.createDirectories(TEST_REPORT_ROOT);
    }

    @Test
    void prepareReportFolder_deletesSubfoldersButPreservesRunLog() throws Exception {
        Path root = TEST_REPORT_ROOT;

        // Arrange: create dummy subfolders and RunLog.md
        Path sub1 = root.resolve("1_FirstReport");
        Path sub2 = root.resolve("2_SecondReport");
        Files.createDirectories(sub1);
        Files.createDirectories(sub2);

        Path fileInSub1 = sub1.resolve("dummy.md");
        Files.writeString(fileInSub1, "dummy");

        // IMPORTANT: create RunLog.md BEFORE calling prepareReportFolder()
        Path runLog = root.resolve("RunLog.md");
        Files.writeString(runLog, "existing log line");

        // Act
        ReportManager.prepareReportFolder();

        // Assert: Report folders deleted
        assertFalse(Files.exists(sub1), "1_FirstReport should be deleted by prepareReportFolder");
        assertFalse(Files.exists(sub2), "2_SecondReport should be deleted by prepareReportFolder");

        // Assert: RunLog.md preserved
        assertTrue(Files.exists(runLog), "RunLog.md should be preserved");

        String logContent = Files.readString(runLog);
        assertTrue(logContent.contains("existing log line"),
                "Existing content in RunLog.md should not be removed");
    }

    @Test
    void writeMarkdown_createsTimestampedFile_andUpdatesRunLog() throws Exception {
        // Act
        ReportManager.writeMarkdown("", "JUnitReport.md", "# Hello\ncontent");

        // Assert markdown created in TestReports
        boolean found = existsReportFile("", "JUnitReport_");
        assertTrue(found, "A timestamped JUnitReport_*.md file should be created in TestReports");

        // Assert RunLog.md created/updated
        Path logPath = TEST_REPORT_ROOT.resolve("RunLog.md");
        assertTrue(Files.exists(logPath), "RunLog.md should exist after writing a report");

        String log = Files.readString(logPath);
        assertTrue(log.contains("JUnitReport"),
                "RunLog.md should contain an entry referencing JUnitReport");
    }
}
