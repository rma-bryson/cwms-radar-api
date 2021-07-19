package cwms.radar.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codahale.metrics.MetricRegistry;
import cwms.radar.formatters.Formats;
import io.javalin.core.util.Header;
import io.javalin.http.Context;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 */
public class LocationControllerTest {
    
    private Context ctx = mock(Context.class);


    public LocationControllerTest() {
    }

    // Put your info here and it will connect to your database during the test...
    // Not ideal.
    protected Connection getConnection() throws SQLException
    {
        String url = "jdbc:oracle:thin:@hostname:1523:mysid";
        Properties connectionProps = new Properties();
        connectionProps.put("user", "theusername");
        connectionProps.put("password", "thepassword");
        final Connection conn = DriverManager.getConnection(url, connectionProps);
        return conn;
    }



    /**
     * Test of getAll method, of class LocationController.
     */
    @Test
    public void testGetAllGEOJSON() throws SQLException {

        String result = getAllWithFormat(Formats.GEOJSON);

    }

    /**
     * Test of getAll method, of class LocationController.
     */
    @Test
    public void testGetAllXML() throws SQLException
    {

        String result = getAllWithFormat(Formats.XML);
    }

    /**
     * Test of getAll method, of class LocationController.
     */
    @Test
    public void testGetAllJSON() throws SQLException
    {

        String result = getAllWithFormat(Formats.JSON);
    }

    @Test
    public void testGetAllCSV() throws SQLException
    {

        String result = getAllWithFormat(Formats.CSV);
    }

    @Test
    public void testGetAllTab() throws SQLException
    {
        String result = getAllWithFormat(Formats.TAB);
    }


    @Nullable
    private String getAllWithFormat(String xml) throws SQLException
    {
        final HttpServletRequest request= mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Map<Class<?>, ?> map = new LinkedHashMap<>();

        when(request.getAttribute("office-id")).thenReturn("LRL");
        when(request.getAttribute("database")).thenReturn(getConnection());

        when(request.getHeader(Header.ACCEPT)).thenReturn(xml);
        when(request.getQueryString()).thenReturn("office=LRL");

        Context ctx = new Context(request, response, map);

        LocationController controller = new LocationController(new MetricRegistry());
        controller.getAll(ctx);

        verify(response).setStatus(200);
        verify(response).setContentType(xml);

        String result = ctx.resultString();
        assertNotNull(result);
        assertFalse(result.isEmpty());

        return result;
    }


    /**
     * Test of create method, of class LocationController.
     */
    @Test
    public void testCreate() {
        System.out.println("create");        
        LocationController instance = new LocationController(new MetricRegistry());
        Assertions.assertThrows( UnsupportedOperationException.class , () -> {
            instance.create(ctx);
        });
        
        
    }

    /**
     * Test of update method, of class LocationController.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");        
        String Location_id = "";
        LocationController instance = new LocationController(new MetricRegistry());
        Assertions.assertThrows( UnsupportedOperationException.class, () -> {
            instance.update(ctx, Location_id);
        });
        
      
    }

    /**
     * Test of delete method, of class LocationController.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");        
        String Location_id = "";
        LocationController instance = new LocationController(new MetricRegistry());
        Assertions.assertThrows( UnsupportedOperationException.class, () -> {
            instance.delete(ctx, Location_id);
        });
        
        
    }
    
}
