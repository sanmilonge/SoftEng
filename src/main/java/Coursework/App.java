package Coursework;

public class App
{
    public static void main( String[] args )
    {
        Connection con = new Connection();
        FirstReport fr = new FirstReport(con);
        SecondReport sr = new SecondReport(con);
        con.connect();
        fr.showCountriesByPopulation();
        sr.showCountriesByPopulation();
        con.disconnect();
    }
}