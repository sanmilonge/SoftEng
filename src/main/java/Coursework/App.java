package Coursework;

public class App {
    public static void main(String[] args) {
        ReportManager.prepareReportFolder();

        Connection db = new Connection();
        FirstReport fr = new FirstReport(db);
        SeventhReport sr = new SeventhReport(db);

        // Defaults for local debugging (DB exposed on localhost:33060)
        if (args.length < 2) {
            db.connect();
        }
        fr.showCountriesByPopulation();
        sr.showCitiesByPopulation();

        db.disconnect();
    }}
