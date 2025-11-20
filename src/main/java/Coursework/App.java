package Coursework;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ReportManager.prepareReportFolder();

        Connection con = new Connection();

        // Reports 1–7
        FirstReport fr = new FirstReport(con);
        ThirdReport tr = new ThirdReport(con);
        SeventhReport sr = new SeventhReport(con);

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

        // Defaults for local debugging
        if (args.length < 2) {
            con.connect("localhost:33060", 30000);
        } else {
            con.connect(args[0], Integer.parseInt(args[1]));
        }

        // Reports 1–7
        fr.showCountriesByPopulation();
        sr.showCitiesByPopulation();
        tr.showCountriesByRegion();

        // 17th — all world capital cities
        r17.showCapitalCitiesInWorld();

        // 18th
        er.showCapitalCitiesInContinent("Europe");

        // 19th
        nr.showCapitalCitiesForMultipleRegions();

        Scanner input = new Scanner(System.in);

        // 20th
        System.out.print("Enter the numbers of top populated capital cities (N): ");
        int n = input.nextInt();
        tr20.showTopNCapitalCities(n);

        input.nextLine(); // clear buffer

        // 21st
        System.out.print("Enter a continent for top populated capital cities: ");
        String continent = input.nextLine();
        System.out.print("Enter the number of capital cities to return (N): ");
        int n2 = input.nextInt();
        tr21.showTopNCapitalCitiesInContinent(continent, n2);

        input.nextLine(); // clear buffer

        // 22nd
        System.out.print("Enter a region for top populated capital cities: ");
        String region = input.nextLine();
        System.out.print("Enter the number of capital cities to return (N): ");
        int n3 = input.nextInt();
        tr22.showTopNCapitalCitiesInRegion(region, n3);

        // 23rd
        tr23.showContinentPopulationSummary();

        // 24th
        tr24.showRegionPopulationSummary();

        // 25th
        tr25.showCountryPopulationSummary();

        // 26th
        tr26.showWorldPopulation();

        // 27th
        tr27.showPopulationOfAllContinents();

        // 28th
        tr28.showPopulationOfAllRegions();

        // 29th
        tr29.showPopulationOfAllCountries();

        // 30th
        tr30.showPopulationOfAllDistricts();

        // 31st
        tr31.showPopulationOfAllCities();

        // 32nd
        tr32.showLanguageSpeakers();

        con.disconnect();
    }
}
