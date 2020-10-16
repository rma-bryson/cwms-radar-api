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

import io.javalin.Javalin;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Handler;
import java.security.PublicKey;
import java.util.Set;
import mil.army.usace.erdc.crrel.cryptoj.PksUtils;
import mil.army.usace.erdc.crrel.cryptoj.jwt.Jwt;
import mil.army.usace.erdc.crrel.cryptoj.jwt.Jwt.CwbiClaims;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rdcrlrsg
 */
public class AccessControl {
    
    public static class AccessControlMiddleware{
    
        private static final org.slf4j.Logger logger = LoggerFactory.getLogger("AccessControl");

        /*
          validateJwt middleware gets the JWT from the request header, decodes it, and verifies the signature.
          If the JWT is valid, the claims are appended to the context and sent to the handler
        */
        public static void validateJwt(Javalin app){
            final Jwt jwt = new Jwt();
            PublicKey pk = PksUtils.readPublicKey(Config.authPk);
            jwt.setPublicKey(pk); 
            app.before(ctx -> {
                String authString=ctx.header("Authorization");
                if(authString !=null && authString.contains("Bearer")){
                    try{
                        String jwtString=authString.replace("Bearer", "").trim();
                        CwbiClaims claims=jwt.parse(jwtString);
                        ctx.attribute("jwtclaims", claims);
                    }
                    catch(Exception ex){
                        logger.info(String.format("Unauthorized: %s",ex.getMessage()));
                        throw new ForbiddenResponse("Unauthorized");
                    }
                }
                else{
                    throw new ForbiddenResponse("Unauthorized");
                }
            });
        }
    }
    
    /*
       the list of application roles
    */
    public enum CwmsRole implements Role{
        PUBLIC("PUBLIC"),
        SYSTEMADMIN("SYSADMIN");
        
        private String role;
        
        private CwmsRole(String role){
            this.role=role;
        }
        
        public String getRole(){
            return this.role;
        }   
    }
    
    
    /*
      Just a data structure to return an error in JSON.
      This really isn't necessary
    */
    public static class JsonError{
        public String title;
        public Integer status;
        public String type;
        public String[] details;
        
        public JsonError(String title, int status, String type, String[] details){
            this.title=title;
            this.status=status;
            this.type=type;
            this.details=details;
        }
    }
    
    /*
        Javalin access manager implementation for our roles
    */
    public static class CwmsAccessManager implements AccessManager{

        @Override
        public void manage(Handler hndlr, Context ctx, Set<Role> permittedRoles) throws Exception {
            CwbiClaims claims = (CwbiClaims)ctx.attribute("claims");
            boolean authorized=false;
            for(Role r:permittedRoles){
                String permittedRoleString = ((AccessControl.CwmsRole)r).getRole();
                if(claims.roles.contains(permittedRoleString)){
                    authorized=true;
                    break;
                }
            }
            if(authorized){
                hndlr.handle(ctx);
            }
            else{
                ctx.status(401).json(new JsonError("Unauthorized",401,null,null));
            }
            
        }
        
    }
      
    
}
