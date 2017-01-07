/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

/**
 *
 * @author onurerden
 */
public class ActivationEvaluator {
    
    public boolean activateUser(String token){
    
    Credentials cr = new Credentials();
    
    Jws<Claims> claims = Jwts.parser()
                        .requireSubject("activation")
                        .setSigningKey(cr.getJjwtKey())
                        .parseClaimsJws(token);
    ServerEngine server = new ServerEngine();
    return server.activateUser(claims.getBody().get("userId",Integer.class));
        
    }
    
    
}
