package Coursework;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {

    /**
     * Always resolves the report folder dynamically.
     * Tests override report.folder → production does not.
     */
    private static String getReportRoot() {
        return System.getProperty("report.folder", "src/main/resources/reports");
    }

    /**
     * Deletes all report subfolders EXCEPT RunLog.md.
     */
    public static void prepareReportFolder() {
        Path root = Paths.get(getReportRoot());

        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
                return;
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
                for (Path item : stream) {
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
     * Recursively deletes folder.
     */
    private static void deleteFolder(Path folder) {
        try {
            Files.walk(folder)
                    .sorted((a, b) -> b.compareTo(a))   // delete children first
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
     * Writes a timestamped markdown file into its subfolder.
     */
    public static void writeMarkdown(String subfolder, String baseFilename, String content) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

            String nameWithoutExt = baseFilename.replace(".md", "");
            String finalName = nameWithoutExt + "_" + timestamp + ".md";

            Path folderPath = Paths.get(getReportRoot(), subfolder);
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
     * Appends a line to RunLog.md showing which report was generated.
     */
    private static void logReportGeneration(String subfolder, String filename) {
        try {
            Path logPath = Paths.get(getReportRoot(), "RunLog.md");

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String logEntry = String.format(
                    "- %s — [%s] generated %s%n",
                    timestamp, subfolder, filename
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
