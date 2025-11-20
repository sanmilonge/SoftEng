package Coursework;

import java.sql.*;

public class ThirtySecondReport {
    private final Connection c;

    public ThirtySecondReport(Connection c) {
        this.c = c;
    }

    public void showLanguageSpeakers() {
        try {
            Statement stmt = c.getConnection().createStatement();

            // World population
            String worldSql = "SELECT SUM(Population) AS WorldPopulation FROM country;";
            ResultSet worldRset = stmt.executeQuery(worldSql);
            long worldPopulation = 0;
            if (worldRset.next()) {
                worldPopulation = worldRset.getLong("WorldPopulation");
            }

            // Main query for selected languages
            String sql =
                    "SELECT cl.Language, SUM(country.Population * cl.Percentage / 100) AS TotalSpeakers " +
                            "FROM countrylanguage cl " +
                            "JOIN country ON cl.CountryCode = country.Code " +
                            "WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                            "GROUP BY cl.Language " +
                            "ORDER BY TotalSpeakers DESC;";

            ResultSet rset = stmt.executeQuery(sql);

            // Build markdown
            StringBuilder md = new StringBuilder();
            md.append("# Number of People Who Speak Selected Languages\n\n");
            md.append("| Language | Total Speakers | % of World Population |\n");
            md.append("|----------|----------------|------------------------|\n");

            while (rset.next()) {
                String language = rset.getString("Language");
                long speakers = rset.getLong("TotalSpeakers");
                double percentage = (speakers * 100.0) / worldPopulation;

                md.append(String.format("| %s | %d | %.2f%% |\n",
                        language, speakers, percentage));
            }

            ReportManager.writeMarkdown(
                    "32_ThirtySecondReport",
                    "ThirtySecondReport.md",
                    md.toString()
            );

            System.out.println("Thirty-second report completed.");

        } catch (Exception e) {
            System.out.println("Failed to generate language report: " + e.getMessage());
        }
    }
}
