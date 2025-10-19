package Coursework;

public class App
{
    public static void main( String[] args ) {
        ReportManager.prepareReportFolder();


        Connection con = new Connection();
        FirstReport fr = new FirstReport(con);
        SeventhReport sr = new SeventhReport(con);

        con.connect();
        fr.showCountriesByPopulation();
        con.disconnect();


        con.connect();
        sr.showCitiesByPopulation();
        con.disconnect();
    }
}