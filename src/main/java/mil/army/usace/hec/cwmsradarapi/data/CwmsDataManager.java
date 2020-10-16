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

import io.javalin.http.Context;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import mil.army.usace.erdc.crrel.dataquery.connection.action.Select.ReturnType;
import mil.army.usace.erdc.crrel.dataquery.fluent.DataQuery;
import mil.army.usace.hec.cwmsradarapi.data.dao.UsgsStation;

/**
 *
 * @author rdcrlrsg
 */
public class CwmsDataManager implements AutoCloseable{
    
    private final DataQuery db;
    
    public CwmsDataManager(Context ctx) throws SQLException{
        Connection conn = ctx.appAttribute(CwmsDataSource.class).getConnection();
        this.db = new DataQuery(conn);
    }
    
    public List<UsgsStation> getUsgsStations(){
        return db.select(UsgsStation.class)
                 .fetch();
    }
    
    public String getUsgsStations2(){
        return db.select(ReturnType.JSON)
                 .sql("select * from (select * from usgs_station_t) t1 where rownum<10")
                 .fetchValue();
    }
    
    public UsgsStation getUsgsStationById(String id){
        return db.select(UsgsStation.class)
                 .criteria("where station_id=?")
                 .params(id)
                 .fetchRow();
    }
    
    //select ts_code,value,date_time,unit_system from cwms_curr_ts_mv where ts_code = '34099030';
    String stuffSql="SELECT\n" +
"			l.public_name, l.location_code, l.location_id, \n" +
"			lk.location_kind_id, l.latitude, l.longitude\n" +
"		FROM \n" +
"			cwms_v_loc l\n" +
"			INNER JOIN cwms_v_location_kind lk \n" +
"				ON l.location_kind_id = lk.location_kind_id\n" +
"			INNER JOIN cwms_v_office o\n" +
"				ON l.db_office_id = o.office_id\n" +
"		WHERE \n" +
"			l.active_flag = 'T'\n" +
"			AND l.unit_system = ?\n" +
"			AND l.latitude IS NOT NULL AND l.longitude IS NOT NULL\n" +
"			AND lk.location_kind_id IN ('PROJECT','BASIN','STREAM')";
    public String getStuff(String unitSystem){
        return db.select(ReturnType.JSON)
                 .sql(stuffSql)
                 .params(unitSystem)
                 .fetchValue();
    }

    @Override
    public void close() throws Exception {
        this.db.close();
    }
    
}
