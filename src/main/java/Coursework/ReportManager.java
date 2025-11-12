package Coursework;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {

    private static final String BASE_REPORT_FOLDER = "src/main/resources/reports";

    // Determine active report folder (can be overridden in tests)
    private static final String REPORT_FOLDER =
            System.getProperty("report.folder", BASE_REPORT_FOLDER);

    /**
     * Prepare (and if needed, clear) the current report folder.
     * Does NOT delete production folders like 1_, 3_, 7_.
     */
    public static void prepareReportFolder() {
        try {
            Path folderPath = Paths.get(REPORT_FOLDER);

            // If we're working in the main "reports" folder, never delete production data
            if (folderPath.endsWith("reports")) {
                Files.createDirectories(folderPath);
                System.out.println("Preserving production report folders: " + folderPath.toAbsolutePath());
                return;
            }

            // Otherwise clear test folder only
            if (Files.exists(folderPath)) {
                Files.walk(folderPath)
                        .sorted((a, b) -> b.compareTo(a))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                System.out.println("Failed to delete: " + path + " -> " + e.getMessage());
                            }
                        });
            }

            Files.createDirectories(folderPath);
            System.out.println("Test report folder ready: " + folderPath.toAbsolutePath());

        } catch (IOException e) {
            System.out.println("Error preparing report folder: " + e.getMessage());
        }
    }

    /**
     * Write markdown to a subfolder, automatically placing it under
     * either /reports or /reports/TestReports based on current mode.
     */
    public static void writeMarkdown(String subfolder, String baseFilename, String content) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
            String nameWithoutExt = baseFilename.replace(".md", "");
            String finalName = nameWithoutExt + "_" + timestamp + ".md";

            // Normalize subfolder path so “TestReports” doesn’t get duplicated
            Path folderPath = Paths.get(REPORT_FOLDER).resolve(subfolder).normalize();
            Files.createDirectories(folderPath);

            Path filePath = folderPath.resolve(finalName);
            Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Report saved: " + filePath.toAbsolutePath());

            logReportGeneration(finalName);

        } catch (IOException e) {
            System.out.println("Failed to write report: " + e.getMessage());
        }
    }

    private static void logReportGeneration(String filename) {
        try {
            Path logPath = Paths.get(REPORT_FOLDER, "RunLog.md");
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logEntry = String.format("- %s — generated *%s*%n", timestamp, filename);

            Files.writeString(logPath, logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Failed to update RunLog.md: " + e.getMessage());
        }
    }
}