/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.army.usace.hec.cwmsradarapi.handlers;

import io.javalin.http.Context;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mil.army.usace.hec.cwmsradarapi.data.CwmsDataManager;
import mil.army.usace.hec.cwmsradarapi.data.CwmsDataSource;
import mil.army.usace.hec.cwmsradarapi.data.dao.UsgsStation;

/**
 *
 * @author rdcrlrsg
 */
public class CwmsHandler {
    
    /*
        Simple example returning a string.
    */
    public static void version(Context ctx){
        ctx.result("Version 0.1");
    }
    
    
    /*
        Example Working with servlet request/response    
    */
    public static void sample(Context ctx) throws IOException{
        HttpServletRequest request = ctx.req;
        HttpServletResponse response = ctx.res;
        for (int i = 0;i<100;i++){
            response.getOutputStream().write(i);
            response.getOutputStream().flush();
        }
    }
    
    /*
        Example Call to Oracle
    */
    public static void getUsgsStations(Context ctx) throws Exception{
        try(CwmsDataManager cdm = new CwmsDataManager(ctx)){
            List<UsgsStation> stations = cdm.getUsgsStations();
            ctx.json(stations);
        }
    }
    
    public static void getUsgsStations2(Context ctx) throws Exception{
        try(CwmsDataManager cdm = new CwmsDataManager(ctx)){
            String stationsJson = cdm.getUsgsStations2();
            ctx.result(stationsJson);
        }
    }
    
    public static void getUsgsStation(Context ctx) throws Exception{
        String id = ctx.pathParam("id");
        try(CwmsDataManager cdm = new CwmsDataManager(ctx)){
            ctx.json(cdm.getUsgsStationById(id));
        }
    }
     
    public static void getStuff(Context ctx) throws Exception{
        String us = ctx.pathParam("us");
        try(CwmsDataManager cdm = new CwmsDataManager(ctx)){
            ctx.result(cdm.getStuff(us));
        }
    }
    
}
