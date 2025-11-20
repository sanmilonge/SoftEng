package Coursework;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Shared test utilities for report-related tests.
 * - Isolates report output under src/main/resources/reports/TestReports
 * - Provides helpers to clean folders and check for generated files
 * - Provides a helper to mock ReportManager static methods
 */
public abstract class ReportTestSupport {

    protected static final Path TEST_REPORT_ROOT =
            Paths.get("src", "main", "resources", "reports", "TestReports");

    @BeforeEach
    void initReportFolder() throws IOException {
        // Tell ReportManager to use TestReports as root
        System.setProperty("report.folder", TEST_REPORT_ROOT.toString());

        // Clean up any existing content
        if (Files.exists(TEST_REPORT_ROOT)) {
            Files.walk(TEST_REPORT_ROOT)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) { }
                    });
        }

        Files.createDirectories(TEST_REPORT_ROOT);
    }

    protected Path resolveSubfolder(String subfolder) {
        if (subfolder == null || subfolder.isEmpty()) {
            return TEST_REPORT_ROOT;
        }
        return TEST_REPORT_ROOT.resolve(subfolder);
    }

    protected boolean existsReportFile(String subfolder, String baseNamePrefix) throws IOException {
        Path root = resolveSubfolder(subfolder);
        if (!Files.exists(root)) return false;

        try (var walk = Files.walk(root)) {
            return walk
                    .filter(Files::isRegularFile)
                    .anyMatch(p -> p.getFileName().toString().startsWith(baseNamePrefix));
        }
    }

    protected long countMarkdownFiles(String subfolder) throws IOException {
        Path root = resolveSubfolder(subfolder);
        if (!Files.exists(root)) return 0;

        try (var walk = Files.walk(root)) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".md"))
                    .count();
        }
    }

    /**
     * Convenience helper to mock ReportManager static methods.
     * Use with try-with-resources:
     *
     * try (MockedStatic<ReportManager> rm = mockReportManagerStatic()) { ... }
     */
    protected MockedStatic<ReportManager> mockReportManagerStatic() {
        return Mockito.mockStatic(ReportManager.class);
    }

    /**
     * Helper to assert a directory exists (handy for integration-style tests).
     */
    protected void assertDirectoryExists(Path dir, String message) {
        assertTrue(Files.exists(dir) && Files.isDirectory(dir), message);
    }
}
