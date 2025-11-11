package Coursework;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {

    // Base folder for all generated reports
    private static final String REPORT_FOLDER = "src/main/resources/reports";

    /**
     * Prepares the base reports folder by deleting old files/folders
     * and recreating it cleanly.
     */
    public static void prepareReportFolder() {
        try {
            Path folderPath = Paths.get(REPORT_FOLDER);

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
            System.out.println("Report folder ready: " + folderPath.toAbsolutePath());

        } catch (IOException e) {
            System.out.println("Error preparing report folder: " + e.getMessage());
        }
    }

    /**
     * Writes a Markdown file into a specific subfolder, with timestamped filenames.
     *
     * @param subfolder Subdirectory (e.g., "1_FirstReport")
     * @param baseFilename Report filename (e.g., "FirstReport.md")
     * @param content Markdown content to write
     */
    public static void writeMarkdown(String subfolder, String baseFilename, String content) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

            String nameWithoutExt = baseFilename.replace(".md", "");
            String finalName = nameWithoutExt + "_" + timestamp + ".md";

            Path folderPath = Paths.get(REPORT_FOLDER, subfolder);
            Files.createDirectories(folderPath);

            Path filePath = folderPath.resolve(finalName);
            Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Report saved: " + filePath.toAbsolutePath());

            logReportGeneration(subfolder, finalName);

        } catch (IOException e) {
            System.out.println("Failed to write report: " + e.getMessage());
        }
    }

    /**
     * Logs report generation info inside its subfolder RunLog.md
     */
    private static void logReportGeneration(String subfolder, String filename) {
        try {
            Path logPath = Paths.get(REPORT_FOLDER, subfolder, "RunLog.md");
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String logEntry = String.format("- %s — generated %s%n", timestamp, filename);

            Files.writeString(logPath, logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Failed to update RunLog.md: " + e.getMessage());
        }
    }
}