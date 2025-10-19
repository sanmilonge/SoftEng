package Coursework;

public class App
{
    public static void main( String[] args )
    {
        Connection con = new Connection();
        FirstReport fr = new FirstReport(con);
        con.connect();
        fr.showCountriesByPopulation();
        con.disconnect();
    }
}