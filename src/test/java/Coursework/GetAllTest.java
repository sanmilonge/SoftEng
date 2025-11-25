package Coursework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GetAll.java
 * Uses Mockito to simulate database behaviour without a real DB.
 *
 * Goal: verify that SQL queries return correctly parsed lists and totals.
 */
@ExtendWith(MockitoExtension.class)
class GetAllTest {

    // Mock our wrapper connection class
    @Mock
    Coursework.Connection c;

    // Mock actual JDBC components
    @Mock java.sql.Connection sqlCon;
    @Mock Statement stmt;
    @Mock PreparedStatement pstmt;
    @Mock ResultSet rset;

    // -------------------------------------------------------------
    // getAllContinents() tests
    // -------------------------------------------------------------
    @Test
    void getAllContinents_returnsList() throws Exception {

        // DB wiring
        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rset);

        // Simulate 2 rows → then no more
        when(rset.next()).thenReturn(true, true, false);
        when(rset.getString("Continent")).thenReturn("Europe", "Asia");

        GetAll g = new GetAll(c);
        var list = g.getAllContinents();

        assert list.size() == 2;
        assert list.contains("Europe");
        assert list.contains("Asia");
    }

    // -------------------------------------------------------------
    // getAllRegions() tests
    // -------------------------------------------------------------
    @Test
    void getAllRegions_returnsList() throws Exception {

        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rset);

        when(rset.next()).thenReturn(true, false);
        when(rset.getString("Region")).thenReturn("Caribbean");

        GetAll g = new GetAll(c);
        var list = g.getAllRegions();

        assert list.size() == 1;
        assert list.contains("Caribbean");
    }

    // -------------------------------------------------------------
    // getAllCountries() tests
    // -------------------------------------------------------------
    @Test
    void getAllCountries_returnsList() throws Exception {

        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rset);

        when(rset.next()).thenReturn(true, true, false);
        when(rset.getString("Country")).thenReturn("France", "Ghana");

        GetAll g = new GetAll(c);

        var list = g.getAllCountries();
        assert list.size() == 2;
    }

    // -------------------------------------------------------------
    // getAllDistricts() tests
    // -------------------------------------------------------------
    @Test
    void getAllDistricts_returnsList() throws Exception {

        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rset);

        when(rset.next()).thenReturn(true, false);
        when(rset.getString("District")).thenReturn("California");

        GetAll g = new GetAll(c);

        var list = g.getAllDistricts();
        assert list.get(0).equals("California");
    }

    // -------------------------------------------------------------
    // totalNumberOfCountries() tests
    // -------------------------------------------------------------
    @Test
    void totalCountries_byRegion() throws Exception {

        // PreparedStatement is used when filters exist
        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rset);

        // COUNT(*) returns "Total"
        when(rset.next()).thenReturn(true);
        when(rset.getInt("Total")).thenReturn(15);

        GetAll g = new GetAll(c);

        int result = g.totalNumberOfCountries(null, "Western Europe");
        assert result == 15;
    }

    @Test
    void totalCountries_byContinent() throws Exception {

        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rset);

        when(rset.next()).thenReturn(true);
        when(rset.getInt("Total")).thenReturn(50);

        GetAll g = new GetAll(c);

        int result = g.totalNumberOfCountries("Asia", null);
        assert result == 50;
    }

    @Test
    void totalCountries_world() throws Exception {

        // No filters → normal Statement used
        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rset);

        when(rset.next()).thenReturn(true);
        when(rset.getInt("Total")).thenReturn(195);

        GetAll g = new GetAll(c);

        int result = g.totalNumberOfCountries(null, null);
        assert result == 195;
    }

    // -------------------------------------------------------------
    // totalNumberOfCities() tests
    // -------------------------------------------------------------
    @Test
    void totalCities_byRegion() throws Exception {

        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rset);

        when(rset.next()).thenReturn(true);
        when(rset.getInt("Total")).thenReturn(200);

        GetAll g = new GetAll(c);

        int result = g.totalNumberOfCities(null, "Eastern Africa", null, null);
        assert result == 200;
    }

    @Test
    void totalCities_byContinent() throws Exception {

        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rset);

        when(rset.next()).thenReturn(true);
        when(rset.getInt("Total")).thenReturn(800);

        GetAll g = new GetAll(c);

        int result = g.totalNumberOfCities("Europe", null, null, null);
        assert result == 800;
    }

    @Test
    void totalCities_byCountry() throws Exception {

        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rset);

        when(rset.next()).thenReturn(true);
        when(rset.getInt("Total")).thenReturn(120);

        GetAll g = new GetAll(c);

        int result = g.totalNumberOfCities(null, null, "France", null);
        assert result == 120;
    }

    @Test
    void totalCities_byDistrict() throws Exception {

        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rset);

        when(rset.next()).thenReturn(true);
        when(rset.getInt("Total")).thenReturn(12);

        GetAll g = new GetAll(c);

        int result = g.totalNumberOfCities(null, null, null, "California");
        assert result == 12;
    }

    @Test
    void totalCities_world() throws Exception {

        when(c.getConnection()).thenReturn(sqlCon);
        when(sqlCon.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rset);

        when(rset.next()).thenReturn(true);
        when(rset.getInt("Total")).thenReturn(4079);

        GetAll g = new GetAll(c);

        int result = g.totalNumberOfCities(null, null, null, null);
        assert result == 4079;
    }
}
