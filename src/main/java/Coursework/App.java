package Coursework;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ReportManager.prepareReportFolder();

        Connection con = new Connection();
        FirstReport fr = new FirstReport(con);
        ThirdReport tr = new ThirdReport(con);
        SeventhReport sr = new SeventhReport(con);
        EighteenthReport er = new EighteenthReport(con);
        NineteenthReport nr = new NineteenthReport(con);
        TwentiethReport tr20 = new TwentiethReport(con);
        TwentyFirstReport tr21 = new TwentyFirstReport(con);
        TwentySecondReport tr22 = new TwentySecondReport(con);
        TwentyThirdReport tr23 = new TwentyThirdReport(con);
        TwentyFourthReport tr24 = new TwentyFourthReport(con);  // ← NEW 24th Report

        // Defaults for local debugging (DB exposed on localhost:33060)
        if (args.length < 2) {
            con.connect("localhost:33060", 30000);
        } else {
            con.connect(args[0], Integer.parseInt(args[1]));
        }

        // Existing reports
        fr.showCountriesByPopulation();
        sr.showCitiesByPopulation();
        tr.showCountriesByRegion();

        // 18th report
        er.showCapitalCitiesInContinent("Europe");

        // 19th report – combined multiple regions
        nr.showCapitalCitiesForMultipleRegions();

        Scanner input = new Scanner(System.in);

        // 20th report — ask user for N
        System.out.print("Enter the number of top populated capital cities (N): ");
        int n = input.nextInt();
        tr20.showTopNCapitalCities(n);

        input.nextLine(); // Clear buffer

        // 21st report — ask user for continent + N
        System.out.print("Enter a continent for top populated capital cities: ");
        String continent = input.nextLine();
        System.out.print("Enter the number of capital cities to return (N): ");
        int n2 = input.nextInt();
        tr21.showTopNCapitalCitiesInContinent(continent, n2);

        input.nextLine(); // Clear buffer

        // 22nd report — ask user for region + N
        System.out.print("Enter a region for top populated capital cities: ");
        String region = input.nextLine();
        System.out.print("Enter the number of capital cities to return (N): ");
        int n3 = input.nextInt();
        tr22.showTopNCapitalCitiesInRegion(region, n3);

        // 23rd report — continent population summary
        tr23.showContinentPopulationSummary();

        // 24th report — region population summary
        tr24.showRegionPopulationSummary();

        con.disconnect();
    }
}
