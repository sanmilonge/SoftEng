package Coursework;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final boolean CI_MODE = //Restricts github to use default values
            System.getenv("CI") != null &&
                    System.getenv("CI").equalsIgnoreCase("true");

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        ReportManager.prepareReportFolder();

        Connection con = new Connection();

        // Connect
        if (args.length < 2) {
            con.connect("localhost:33060", 30000);
        } else {
            con.connect(args[0], Integer.parseInt(args[1]));
        }

        // Init reports
        FirstReport f1r = new FirstReport(con);
        SecondReport s2r = new SecondReport(con);
        ThirdReport t3r = new ThirdReport(con);
        FourthReport f4r = new FourthReport(con);
        FifthReport f5r = new FifthReport(con);
        SixthReport s6r = new SixthReport(con);
        SeventhReport s7r = new SeventhReport(con);
        EighthReport e8r = new EighthReport(con);
        NinthReport n9r = new NinthReport(con);
        TenthReport t10r = new TenthReport(con);
        EleventhReport e11r = new EleventhReport(con);
        TwelfthReport t12r = new TwelfthReport(con);
        ThirteenthReport t13r = new ThirteenthReport(con);
        FourteenthReport f14r = new FourteenthReport(con);
        FifteenthReport f15r = new FifteenthReport(con);
        SixteenthReport s16r = new SixteenthReport(con);
        SeventeenthReport s17r = new SeventeenthReport(con);
        EighteenthReport e18r = new EighteenthReport(con);
        NineteenthReport n19r = new NineteenthReport(con);
        TwentiethReport t20r = new TwentiethReport(con);
        TwentyFirstReport t21r = new TwentyFirstReport(con);
        TwentySecondReport t22r = new TwentySecondReport(con);
        TwentyThirdReport t23r = new TwentyThirdReport(con);
        TwentyFourthReport t24r = new TwentyFourthReport(con);
        TwentyFifthReport t25r = new TwentyFifthReport(con);
        TwentySixthReport t26r = new TwentySixthReport(con);
        TwentySeventhReport t27r = new TwentySeventhReport(con);
        TwentyEighthReport t28r = new TwentyEighthReport(con);
        TwentyNinthReport t29r = new TwentyNinthReport(con);
        ThirtiethReport t30r = new ThirtiethReport(con);
        ThirtyFirstReport t31r = new ThirtyFirstReport(con);
        ThirtySecondReport t32r = new ThirtySecondReport(con);

        GetAll helper = new GetAll(con);

        // ========== REPORTS THAT REQUIRE USER INPUT ==========

        // ---------- 4 ----------
        int n4 = askForInt(input,
                "Enter how many countries to see most populated countries in the world:",
                1, safeMax(helper.totalNumberOfCountries(null, null)));
        f4r.showTopNCountries(n4);
        pause(input);

        // ---------- 5 ----------
        String continent5 = askForValidatedString(input,
                "Enter continent to find most populated countries:",
                helper.getAllContinents(), "Africa");
        int n5 = askForInt(input,
                "Enter how many countries to see the most populated in " + continent5,
                1, safeMax(helper.totalNumberOfCountries(continent5, null)));
        f5r.showTopNCountriesInContinent(n5, continent5);
        pause(input);

        // ---------- 6 ----------
        String region6 = askForValidatedString(input,
                "Enter region to find most populated countries:",
                helper.getAllRegions(), "Western Europe");
        int n6 = askForInt(input,
                "Enter how many countries to see the most populated in " + region6,
                1, safeMax(helper.totalNumberOfCountries(null, region6)));
        s6r.showTopNCountriesInRegion(n6, region6);
        pause(input);

        // ---------- 12 ----------
        int n12 = askForInt(input, "Enter how many cities to know the most populated in the world: ", 1, safeMax(helper.totalNumberOfCities(null, null, null, null)));
        t12r.showTopNCitiesITheWorld(n12);
        pause(input);

        // ---------- 13 ----------
        String continent13 = askForValidatedString(input, "Enter continent to find the most populated cities: ", helper.getAllContinents(), "Africa");
        int n13 = askForInt(input, "Enter how many cities to see the most populated in " + continent13, 1, safeMax(helper.totalNumberOfCities(continent13, null, null, null)));
        t13r.showTopNCitiesInContinent(n13, continent13);
        pause(input);

        // ---------- 14 ----------
        String region14 = askForValidatedString(input, "Enter region to find the most populated cities: ", helper.getAllRegions(), "Western Europe");
        int n14 = askForInt(input, "Enter how many cities to see the most populated in " + region14, 1, safeMax(helper.totalNumberOfCities(null, region14, null, null)));
        f14r.showTopNCitiesInRegion(n14, region14);
        pause(input);

        // ---------- 15 ----------
        String country = askForValidatedString(input, "Enter country to find the most populated cities: ", helper.getAllCountries(), "United States");
        int n15 = askForInt(input, "Enter how many cities to see the most populated in " + country, 1, safeMax(helper.totalNumberOfCities(null, null, country, null)));
        f15r.showTopNCitiesInCountry(n15, country);
        pause(input);

        // ---------- 16 ----------
        String district = askForValidatedString(input, "Enter district to find the most populated cities: ", helper.getAllDistricts(), "California");
        int n16 = askForInt(input, "Enter how many cities to see the most populated in " + district, 1, safeMax(helper.totalNumberOfCities(null, null, null, district)));
        s16r.showTopNCitiesInDistrict(n16, district);
        pause(input);

        // ---------- 20 ----------
        int n20 = askForInt(input,
                "Enter how many capital cities in order of population in the world:",
                1, safeMax(helper.totalNumberOfCountries(null, null)));
        t20r.showTopNCapitalCities(n20);
        pause(input);

        // ---------- 21 ----------
        String continent21 = askForValidatedString(input,
                "Enter a continent to find the top populated capital cities:",
                helper.getAllContinents(), "Africa");
        int n21 = askForInt(input,
                "Enter the number of top capital cities to return:",
                1, safeMax(helper.totalNumberOfCountries(continent21, null)));
        t21r.showTopNCapitalCitiesInContinent(continent21, n21);
        pause(input);

        // ---------- 22 ----------
        String region22 = askForValidatedString(input,
                "Enter a region to find the top populated capital cities:",
                helper.getAllRegions(), "Western Europe");
        int n22 = askForInt(input,
                "Enter the number of top capital cities to return:",
                1, safeMax(helper.totalNumberOfCountries(null, region22)));
        t22r.showTopNCapitalCitiesInRegion(region22, n22);
        pause(input);

        // ========== REPORTS WITH NO USER INPUT ==========

        f1r.showCountriesByPopulation();
        s2r.showCountriesContinent();
        t3r.showCountriesByRegion();
        s7r.showCitiesByPopulation();
        e8r.showCitiesContinent();
        n9r.showCitiesByRegion();
        t10r.showCitiesByCountry();
        e11r.showCitiesByDistrict();
        s17r.showCapitalCitiesInWorld();
        e18r.showCapitalCitiesInMultipleContinents();
        n19r.showCapitalCitiesForMultipleRegions();
        t23r.showContinentPopulationSummary();
        t24r.showRegionPopulationSummary();
        t25r.showCountryPopulationSummary();
        t26r.showWorldPopulation();
        t27r.showPopulationOfAllContinents();
        t28r.showPopulationOfAllRegions();
        t29r.showPopulationOfAllCountries();
        t30r.showPopulationOfAllDistricts();
        t31r.showPopulationOfAllCities();
        t32r.showLanguageSpeakers();

        con.disconnect();
    }

    // ---------- HELPERS ----------

    private static int askForInt(Scanner input, String prompt, int min, int max) {
        if (CI_MODE) return min;

        if (max < min) {
            System.out.println("No data available. Using default value: " + min);
            return min;
        }

        while (true) {
            System.out.println(prompt + " (" + min + "–" + max + ")");
            try {
                int value = Integer.parseInt(input.nextLine().trim());
                if (value >= min && value <= max) return value;
                System.out.println("Invalid number. Try again.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String askForValidatedString(Scanner input, String prompt, List<String> validOptions, String defaultValue) {
        if (CI_MODE) return defaultValue;

        while (true) {
            System.out.println(prompt);
            String value = input.nextLine().trim();

            for (String option : validOptions) {
                if (option.equalsIgnoreCase(value)) return option;
            }

            System.out.println("Invalid input: '" + value + "'");
            System.out.println("Did you mean:");
            validOptions.stream()
                    .sorted((a, b) -> levenshtein(value.toLowerCase(), a.toLowerCase()) -
                            levenshtein(value.toLowerCase(), b.toLowerCase()))
                    .limit(5)
                    .forEach(opt -> System.out.println("  • " + opt));
        }
    }

    private static int safeMax(int max) {
        return Math.max(1, max);
    }

    private static void pause(Scanner input) {
        if (!CI_MODE) {
            System.out.println("Press Enter to continue...");
            input.nextLine();
        }
    }

    private static int levenshtein(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else {
                    dp[i][j] = Math.min(
                            dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)
                    );
                }
            }
        }
        return dp[a.length()][b.length()];
    }
}
