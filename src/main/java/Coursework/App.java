package Coursework;

import java.util.Scanner;

public class App {

    private static final boolean CI_MODE =
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

        // Reports 1–16 (no input needed)
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

        // Reports 17–32
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
        GetAll helper = new GetAll(con);

        // ---- VALIDATED INPUT METHODS ----
        //Fourth Report
        int n4 = askForInt(input,
                "Enter how many countries to see most populated countries in the world:",
                1, helper.totalNumberOf(null, null));

        System.out.println("✓ You selected: " + n4);
        f4r.showTopNCountries(n4);

//        //Fifth Report
        String continent5 = askForString(input, "Enter continent to find most populated countries: ", "Africa");

        System.out.println("✓ You selected: " + continent5);

        int n5 = askForInt(input, "Enter how many countries to see the most populated in "+ continent5, 1, helper.totalNumberOf(continent5, null));
        System.out.println("✓ You selected: " + n5);
        f5r.showTopNCountriesInContinent(n5, continent5);
        System.out.println("Press enter to continue.");
        if (!CI_MODE)
        {
            input.nextLine(); // clear buffer
        }


        //Sixth report
        String region6 = askForString(input, "Enter region to find most populated countries: ", "Western Europe");

        System.out.println("✓ You selected: " + region6);

        int n6 = askForInt(input, "Enter how many countries to see the most populated in "+ region6, 1, helper.totalNumberOf(null, region6));
        System.out.println("✓ You selected: " + n6);
        s6r.showTopNCountriesInRegion(n6, region6);
        System.out.println("Press enter to continue.");
        if (!CI_MODE)
        {
            input.nextLine(); // clear buffer
        }


        //Twentieth Report
        int n20 = askForInt(input,
                "Enter how many capital cities in order of population in the world:",
                1, helper.totalNumberOf(null, null));

        System.out.println("✓ You selected: " + n20);
        tr20.showTopNCapitalCities(n20);
        System.out.println("Press enter to continue.");
        if (!CI_MODE)
        {
            input.nextLine(); // clear buffer
        }


        //TwentyFirst Report
        String continent = askForString(input,
                "Enter a continent to find the top populated capital cities:", "Africa");

        System.out.println("✓ You selected: " + continent);

        int n21 = askForInt(input,
                "Enter the number of top capital cities to return:",
                1, helper.totalNumberOf(continent, null));

        System.out.println("✓ You selected: " + n21);
        tr21.showTopNCapitalCitiesInContinent(continent, n21);
        System.out.println("Press enter to continue.");
        if (!CI_MODE)
        {
            input.nextLine(); // clear buffer
        }


        //TwentySecond Report
        String region = askForString(input,
                "Enter a region to find the top populated capital cities:", "Western Europe");

        System.out.println("✓ You selected: " + region);

        int n22 = askForInt(input,
                "Enter the number of top capital cities to return:",
                1, helper.totalNumberOf(region, null));

        System.out.println("✓ You selected: " + n22);
        tr22.showTopNCapitalCitiesInRegion(region, n22);
        System.out.println("Press enter to continue.");
        if (!CI_MODE)
        {
            input.nextLine(); // clear buffer
        }



        // ---- RUN REMAINING REPORTS ----
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


    // ---------------- INPUT VALIDATION ---------------- //

    private static int askForInt(Scanner input, String prompt, int min, int max) {
        if (CI_MODE) {
            return min; // CI ALWAYS returns a safe default
        }

        int value;
        while (true) {
            System.out.println(prompt);
            try {
                value = Integer.parseInt(input.nextLine());
                if (value >= min && value <= max) return value;
                System.out.println("Invalid number. Try again.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String askForString(Scanner input, String prompt, String defaultvalue) {
        if (CI_MODE) {
            return defaultvalue; // safe default for CI
        }

        String value;
        while (true) {
            System.out.println(prompt);
            value = input.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Input cannot be empty. Try again.");
        }
    }
}
