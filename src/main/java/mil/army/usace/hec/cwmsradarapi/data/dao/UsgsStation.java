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

package mil.army.usace.hec.cwmsradarapi.data.dao;

import mil.army.usace.erdc.crrel.dataquery.annotations.Entity;

/**
 *
 * @author rdcrlrsg
 */
@Entity(table="USGS_STATION_T")
public class UsgsStation {
    
    private String agencyCd;
    private String stationId;
    private String stationName;
    private String siteTypeCode;
    private Double lat;
    private Double lon;
    private String CoordAcyCd;
    private String datumHorizontal;
    private Double altVa;
    private Double altAcyVa;
    private String datumVertical;
    private String stateAbbr;

    public String getAgencyCd() {
        return agencyCd;
    }

    public void setAgencyCd(String agencyCd) {
        this.agencyCd = agencyCd;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getSiteTypeCode() {
        return siteTypeCode;
    }

    public void setSiteTypeCode(String siteTypeCode) {
        this.siteTypeCode = siteTypeCode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCoordAcyCd() {
        return CoordAcyCd;
    }

    public void setCoordAcyCd(String CoordAcyCd) {
        this.CoordAcyCd = CoordAcyCd;
    }

    public String getDatumHorizontal() {
        return datumHorizontal;
    }

    public void setDatumHorizontal(String datumHorizontal) {
        this.datumHorizontal = datumHorizontal;
    }

    public Double getAltVa() {
        return altVa;
    }

    public void setAltVa(Double altVa) {
        this.altVa = altVa;
    }

    public Double getAltAcyVa() {
        return altAcyVa;
    }

    public void setAltAcyVa(Double altAcyVa) {
        this.altAcyVa = altAcyVa;
    }

    public String getDatumVertical() {
        return datumVertical;
    }

    public void setDatumVertical(String datumVertical) {
        this.datumVertical = datumVertical;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }
    
    
    
}
