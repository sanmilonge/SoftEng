package Coursework;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {

    // Folder where reports will be saved
    private static final String REPORT_FOLDER = "src/main/resources/reports";

    /**
     * Deletes all files inside the report folder (if it exists)
     * and recreates it clean.
     */
    public static void prepareReportFolder() {
        try {
            Path folderPath = Paths.get(REPORT_FOLDER);

            // If folder exists, clear only the files (not the folder itself)
            if (Files.exists(folderPath)) {
                Files.walk(folderPath)
                        .filter(Files::isRegularFile) // only delete files
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.out.println(" Failed to delete file: " + path + " -> " + e.getMessage());
                            }
                        });
            }

            // Ensure the folder exists
            Files.createDirectories(folderPath);
            System.out.println(" Report folder ready: " + folderPath.toAbsolutePath());

        } catch (IOException e) {
            System.out.println("Error preparing report folder: " + e.getMessage());
        }
    }

    /**
     * Writes markdown content to a file with a timestamp in its name.
     * Also logs the creation to RunLog.md.
     */
    public static void writeMarkdown(String baseFilename, String content) {
        try {
            // Create a timestamp for filenames and logs
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

            // Add timestamp to the filename
            String nameWithoutExt = baseFilename.replace(".md", "");
            String finalName = nameWithoutExt + "_" + timestamp + ".md";

            // Create full file path
            Path filePath = Paths.get(REPORT_FOLDER, finalName);

            // Write the markdown report file
            Files.writeString(filePath, content,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Report saved: " + filePath.toAbsolutePath());

            // Log entry in RunLog.md
            logReportGeneration(finalName);

        } catch (IOException e) {
            System.out.println("Failed to write report: " + e.getMessage());
        }
    }

    /**
     * Appends an entry to RunLog.md to track report generation history.
     */
    private static void logReportGeneration(String filename) {
        try {
            Path logPath = Paths.get(REPORT_FOLDER, "RunLog.md");
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String logEntry = String.format("- %s — generated **%s**%n", timestamp, filename);

            Files.writeString(logPath, logEntry,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.out.println("Failed to update RunLog.md: " + e.getMessage());
        }
    }
}
