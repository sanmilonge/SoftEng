package Coursework;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {

    /**
     * IMPORTANT:
     * Report root is now dynamic.
     * Tests set:  System.setProperty("report.folder", "src/main/resources/reports/TestReports")
     * App uses:   src/main/resources/reports
     */
    private static String getReportsRoot() {
        return System.getProperty("report.folder", "src/main/resources/reports");
    }

    /**
     * Called at the start of the app.
     * Deletes report subfolders (1_FirstReport, etc)
     * but keeps RunLog.md untouched.
     */
    public static void prepareReportFolder() {
        Path root = Paths.get(getReportsRoot());

        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
                return;
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
                for (Path item : stream) {
                    // DELETE ONLY SUBFOLDERS
                    if (Files.isDirectory(item)) {
                        deleteFolder(item);
                    }
                }
            }

            System.out.println("Report folders cleaned. RunLog preserved.");

        } catch (IOException e) {
            System.out.println("Error preparing report folders: " + e.getMessage());
        }
    }

    /**
     * Recursively deletes a folder and all its contents.
     */
    private static void deleteFolder(Path folder) {
        try {
            Files.walk(folder)
                    .sorted((a, b) -> b.compareTo(a)) // Delete children first
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            System.out.println("Failed to delete " + path + ": " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.out.println("Failed to clear folder " + folder + ": " + e.getMessage());
        }
    }

    /**
     * Writes a Markdown file for a report.
     */
    public static void writeMarkdown(String subfolder, String baseFilename, String content) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

            String nameWithoutExt = baseFilename.replace(".md", "");
            String finalName = nameWithoutExt + "_" + timestamp + ".md";

            Path folderPath = Paths.get(getReportsRoot(), subfolder);
            Files.createDirectories(folderPath);

            Path filePath = folderPath.resolve(finalName);

            Files.writeString(
                    filePath,
                    content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            System.out.println("Report saved: " + filePath.toAbsolutePath());

            logReportGeneration(subfolder, finalName);

        } catch (IOException e) {
            System.out.println("Failed to write report: " + e.getMessage());
        }
    }

    /**
     * Appends entry to RunLog.md
     */
    private static void logReportGeneration(String subfolder, String filename) {
        try {
            Path logPath = Paths.get(getReportsRoot(), "RunLog.md");

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String logEntry = String.format(
                    "- %s — [%s] generated %s%n",
                    timestamp,
                    subfolder,
                    filename
            );

            Files.writeString(
                    logPath,
                    logEntry,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            System.out.println("Failed to update RunLog.md: " + e.getMessage());
        }
    }
}
