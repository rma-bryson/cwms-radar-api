/*
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package mil.army.usace.hec.cwmsradarapi.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

/**
 *
 * @author rdcrlrsg
 */
public class CwmsDataSource {
    
    private final DataSource ds;
    
    public static CwmsDataSource init(Properties properties){
        String host=properties.getProperty("DB.HOST");
        String db=properties.getProperty("DB.INSTANCE");
        String min=properties.getProperty("DB.POOL.MIN");
        int port=Integer.parseInt(properties.getProperty("DB.PORT"));
        int max=Integer.parseInt(properties.getProperty("DB.POOL.MAX"));
        String inactivityTimeout=properties.getProperty("DB.POOL.INACTIVITYTIMEOUT");
        int idleTimeout=Integer.parseInt(inactivityTimeout)*1000;
        String maxLifetime=properties.getProperty("DB.POOL.MAXLIFETIME");
        int maximumLifetime=Integer.parseInt(maxLifetime)*1000;
        
        HikariConfig config = new HikariConfig();       
        config.setDataSourceClassName("oracle.jdbc.pool.OracleDataSource");
        config.addDataSourceProperty("URL", String.format("jdbc:oracle:thin:@%s:%d/%s",host,port,db));
        config.setUsername(properties.getProperty("DB.USER"));
        config.setPassword(properties.getProperty("DB.PASS"));
        config.setMaximumPoolSize(max);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maximumLifetime);
        config.setPoolName("Oracle Application Pool");
        DataSource dataSource = new HikariDataSource(config);
        return new CwmsDataSource(dataSource);
    }
    
    public CwmsDataSource(DataSource dataSource){
        this.ds=dataSource;
    }
    
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
}
