package Coursework;

import java.util.Scanner;

public class App {

    /** Safely reads an integer from the scanner. Keeps asking until valid. */
    private static int readInt(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String raw = input.nextLine().trim();
                int value = Integer.parseInt(raw);
                System.out.println("✔ Input accepted: " + value);
                return value;
            } catch (Exception e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
    }

    /** Safely reads a non-empty line of text */
    private static String readString(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            if (!text.isEmpty()) {
                System.out.println("✔ Input accepted: " + text);
                return text;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        ReportManager.prepareReportFolder();
        Connection con = new Connection();

        // Instantiate all reports
        FirstReport f1r = new FirstReport(con);
        SecondReport s2r = new SecondReport(con);
        ThirdReport t3r = new ThirdReport(con);
        FourthReport f4r = new FourthReport(con);
        SeventhReport s7r = new SeventhReport(con);
        EighthReport e8r = new EighthReport(con);
        NinthReport n9r = new NinthReport(con);
        TenthReport t10r = new TenthReport(con);
        EleventhReport e11r = new EleventhReport(con);

        SeventeenthReport r17 = new SeventeenthReport(con);
        EighteenthReport er = new EighteenthReport(con);
        NineteenthReport nr = new NineteenthReport(con);
        TwentiethReport tr20 = new TwentiethReport(con);
        TwentyFirstReport tr21 = new TwentyFirstReport(con);
        TwentySecondReport tr22 = new TwentySecondReport(con);
        TwentyThirdReport tr23 = new TwentyThirdReport(con);
        TwentyFourthReport tr24 = new TwentyFourthReport(con);
        TwentyFifthReport tr25 = new TwentyFifthReport(con);
        TwentySixthReport tr26 = new TwentySixthReport(con);
        TwentySeventhReport tr27 = new TwentySeventhReport(con);
        TwentyEighthReport tr28 = new TwentyEighthReport(con);
        TwentyNinthReport tr29 = new TwentyNinthReport(con);
        ThirtiethReport tr30 = new ThirtiethReport(con);
        ThirtyFirstReport tr31 = new ThirtyFirstReport(con);
        ThirtySecondReport tr32 = new ThirtySecondReport(con);

        // Connect (CI passes args so this does not hang)
        if (args.length < 2) {
            con.connect("localhost:33060", 30000);
        } else {
            con.connect(args[0], Integer.parseInt(args[1]));
        }

        // ---- INPUT-BASED REPORTS (now fully safe) ----

        // 4th report: top N most populated countries
        int n4 = readInt(input,
                "Enter how many countries to see most populated countries in the world: ");
        f4r.showTopNCountries(n4);

        // 20th report: top N capital cities in world
        int n20 = readInt(input,
                "Enter how many capital cities in order of population in the world: ");
        tr20.showTopNCapitalCities(n20);

        // 21st report: top capital cities in a continent
        String continent = readString(input,
                "Enter a continent to find out the top populated capital cities: ");
        int n21 = readInt(input, "Enter the number of top capital cities to return: ");
        tr21.showTopNCapitalCitiesInContinent(continent, n21);

        // 22nd report: top capital cities in a region
        String region = readString(input,
                "Enter a region to find out the the top populated capital cities: ");
        int n22 = readInt(input, "Enter the number of top capital cities to return: ");
        tr22.showTopNCapitalCitiesInRegion(region, n22);

        // ---- NON-INTERACTIVE REPORTS (just run) ----
        f1r.showCountriesByPopulation();
        s2r.showCountriesContinent();
        t3r.showCountriesByRegion();
        s7r.showCitiesByPopulation();
        e8r.showCitiesContinent();
        n9r.showCitiesByRegion();
        t10r.showCitiesByCountry();
        e11r.showCitiesByDistrict();

        r17.showCapitalCitiesInWorld();
        er.showCapitalCitiesInMultipleContinents();
        nr.showCapitalCitiesForMultipleRegions();

        tr23.showContinentPopulationSummary();
        tr24.showRegionPopulationSummary();
        tr25.showCountryPopulationSummary();
        tr26.showWorldPopulation();
        tr27.showPopulationOfAllContinents();
        tr28.showPopulationOfAllRegions();
        tr29.showPopulationOfAllCountries();
        tr30.showPopulationOfAllDistricts();
        tr31.showPopulationOfAllCities();
        tr32.showLanguageSpeakers();

        con.disconnect();
    }
}
