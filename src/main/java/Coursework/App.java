/**
 * Main implementation of coursework project*/


package Coursework;

public class App {
    /**
     * Calls all SQL functions from all reports and executes them*/
    public static void main(String[] args) {
        ReportManager.prepareReportFolder();

        Connection con = new Connection();
        FirstReport f1r = new FirstReport(con);
        SecondReport s2r = new SecondReport(con);
        ThirdReport t3r = new ThirdReport(con);
        SeventhReport s7r = new SeventhReport(con);
        EighthReport e8r = new EighthReport(con);


        // Defaults for local debugging (DB exposed on localhost:33060)
        if (args.length < 2) {
            con.connect("localhost:33060", 30000);
        } else {
            con.connect(args[0], Integer.parseInt(args[1]));
        }

        f1r.showCountriesByPopulation();
        s2r.showCountriesContinent();
        t3r.showCountriesByRegion();
        s7r.showCitiesByPopulation();
        e8r.showCitiesContinent();


        con.disconnect();
    }
}