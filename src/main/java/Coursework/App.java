package Coursework;

public class App {
    public static void main(String[] args) {
        ReportManager.prepareReportFolder();

        Connection con = new Connection();
        FirstReport fr = new FirstReport(con);
        ThirdReport tr = new ThirdReport(con);
        SeventhReport sr = new SeventhReport(con);


        // Defaults for local debugging (DB exposed on localhost:33060)
        if (args.length < 2) {
            con.connect("localhost:33060", 30000);
        } else {
            con.connect(args[0], Integer.parseInt(args[1]));
        }

        fr.showCountriesByPopulation();
        sr.showCitiesByPopulation();
        tr.showCountriesByRegion();

        con.disconnect();
    }
}