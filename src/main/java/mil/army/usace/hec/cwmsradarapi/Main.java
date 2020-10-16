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

package mil.army.usace.hec.cwmsradarapi;

import mil.army.usace.hec.cwmsradarapi.handlers.Routes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJson;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import mil.army.usace.hec.cwmsradarapi.data.CwmsDataSource;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rdcrlrsg
 */
public class Main {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("AccessControl");
    
    public static void main(String[] args){ 
        logger.info("Starting CWMS API");
        try {    
            init(args);
            CwmsDataSource dataSource = CwmsDataSource.init(Config.props);
            //AccessManager accessManager = new CwmsAccessManager(); 
            Javalin app = Javalin.create(
                config -> {
                    config.contextPath = "/cwmsapi";
                    //config.accessManager(accessManager);
                })
                .attribute(CwmsDataSource.class,dataSource)
                .start(Config.host, Config.port);
            
            //AccessControlMiddleware.validateJwt(app);
            Routes.initRoutes(app);
            Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").serializeNulls().create();
            JavalinJson.setFromJsonMapper(gson::fromJson);
            JavalinJson.setToJsonMapper(gson::toJson);
            logger.info("Startup complete."); 
        }
        catch (Exception e) {
            logger.error("Error starting application", e);
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void init(String[] args) throws FileNotFoundException, IOException {
        Properties props=propsFromEnv();
        Config.props = props;
        
        Config.host=props.getProperty("HOST");
        try{
            Config.port=Integer.parseInt(props.getProperty("PORT"));
        }
        catch(NumberFormatException ex){
            logger.warn(String.format("No port provided. Using default port of %d",Config.port));
        }
        
        String authPkPath = props.getProperty("AUTH.PK");
        if(authPkPath==null){
            throw new RuntimeException("Missing Authentication Public Key File");
        }
        
        Config.authPk=Files.readString(Paths.get(authPkPath));
    }
    
    private static Properties propsFromEnv(){
        Map<String,String> env = System.getenv();
        Properties props = new Properties();
        props.putAll(env);
        return props;
    }
    
}
