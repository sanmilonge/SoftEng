package Coursework;

public class App {
    public static void main(String[] args) {
        ReportManager.prepareReportFolder();

        Connection con = new Connection();
        FirstReport f1r = new FirstReport(con);
        SecondReport s2r = new SecondReport(con);
        ThirdReport t3r = new ThirdReport(con);
        SeventhReport s7r = new SeventhReport(con);


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


        con.disconnect();
    }
}