package Coursework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ReportManagerTest {
    static Path outDir;

    @BeforeAll
    static void setUp() throws Exception {
        outDir = Paths.get("src", "main", "resources", "reports");
        ReportManager.prepareReportFolder();   // clears files and recreates dir
        assertTrue(Files.exists(outDir), "Report folder should exist");
    }

    @Test
    void writeMarkdown_createsTimestampedFile_andUpdatesLog() throws Exception {
        String base = "JUnitReport.md";
        ReportManager.writeMarkdown(base, "# hello\ncontent");

        // find a file like JUnitReport_YYYY-MM-dd_HH-mm.md
        AtomicBoolean found = new AtomicBoolean(false);
        try (var stream = Files.walk(outDir)) {
            stream.filter(Files::isRegularFile).forEach(p -> {
                String n = p.getFileName().toString();
                if (n.startsWith("JUnitReport_") && n.endsWith(".md")) {
                    found.set(true);
                }
            });
        }

        assertTrue(found.get(), "A timestamped markdown file should be created (JUnitReport_*.md)");
        assertTrue(Files.exists(outDir.resolve("RunLog.md")), "RunLog.md should be created/updated");
    }
}
