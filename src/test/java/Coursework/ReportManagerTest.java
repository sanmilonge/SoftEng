package Coursework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ReportManagerTest {

    static Path testDir;

    @BeforeAll
    static void setUp() throws Exception {
        // Force ReportManager to use this folder for testing
        System.setProperty("report.folder", "src/main/resources/reports/TestReports");

        testDir = Paths.get("src", "main", "resources", "reports", "TestReports");
        if (Files.exists(testDir)) {
            Files.walk(testDir)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception ignored) {}
                    });
        }
        Files.createDirectories(testDir);
    }

    @Test
    void writeMarkdown_createsTimestampedFile_andUpdatesLog() throws Exception {
        // Write the markdown directly into TestReports (no extra nesting)
        ReportManager.writeMarkdown("", "JUnitReport.md", "# hello\ncontent");

        AtomicBoolean found = new AtomicBoolean(false);
        try (var stream = Files.walk(testDir)) {
            stream.filter(Files::isRegularFile).forEach(p -> {
                String n = p.getFileName().toString();
                if (n.startsWith("JUnitReport_") && n.endsWith(".md")) {
                    found.set(true);
                }
            });
        }

        assertTrue(found.get(), "JUnitReport_*.md should be created inside TestReports");

        // Verify that the RunLog.md is also written in the same folder
        Path logPath = testDir.resolve("RunLog.md");
        assertTrue(Files.exists(logPath), "RunLog.md should exist in TestReports folder");
    }
}