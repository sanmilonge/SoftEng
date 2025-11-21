package Coursework;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ReportManager.prepareReportFolder();

        Connection con = new Connection();

        // Reports 1–16
        FirstReport f1r = new FirstReport(con);
        SecondReport s2r = new SecondReport(con);
        ThirdReport t3r = new ThirdReport(con);
        FourthReport f4r = new FourthReport(con);
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

        // Defaults for local debugging
        if (args.length < 2) {
            con.connect("localhost:33060", 30000);
        } else {
            con.connect(args[0], Integer.parseInt(args[1]));
        }

        // Reports 1–16
      f1r.showCountriesByPopulation();
        s2r.showCountriesContinent();
        t3r.showCountriesByRegion();

        System.out.println("Enter how many countries to see most populated countries in the world: ");
        int n4 = input.nextInt();
        f4r.showTopNCountries(n4);
        s7r.showCitiesByPopulation();
        e8r.showCitiesContinent();
        n9r.showCitiesByRegion();
        t10r.showCitiesByCountry();
        e11r.showCitiesByDistrict();

        // 17th — all world capital cities
        r17.showCapitalCitiesInWorld();

        // 18th
        er.showCapitalCitiesInMultipleContinents();

        // 19th
        nr.showCapitalCitiesForMultipleRegions();



        // 20th
        System.out.print("Enter how many capital cities in order of population in the world: ");
        int n = input.nextInt();
        tr20.showTopNCapitalCities(n);

        input.nextLine(); // clear buffer

        // 21st
        System.out.print("Enter a continent to find out the the top populated capital cities: ");
        String continent = input.nextLine();
        System.out.print("Enter the number of  top capital cities to return: ");
        int n2 = input.nextInt();
        tr21.showTopNCapitalCitiesInContinent(continent, n2);

        input.nextLine(); // clear buffer

        // 22nd
        System.out.print("Enter a region to find out the the top populated capital cities: ");
        String region = input.nextLine();
        System.out.print("Enter the number of  top capital cities to return: ");
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
