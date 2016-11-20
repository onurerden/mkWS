/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureException;
import javax.servlet.http.HttpServletRequest;
import mkws.Model.Token;

/**
 *
 * @author onurerden
 */
public class TokenEvaluator {
    
    public Token evaluateRequestForToken(HttpServletRequest request)  throws SignatureException, IncorrectClaimException, MissingClaimException{
       Token tokenModel = new Token();
       String auth = "" + request.getHeader("Authorization");
       if((auth!=null)&&(auth.startsWith("Bearer "))){
            String token = auth.replaceFirst("Bearer ", "");
        
            Credentials cr = new Credentials();
            
                Jws<Claims> claims = Jwts.parser()
                        .requireSubject("user")
                        .setSigningKey(cr.getJjwtKey())
                        .parseClaimsJws(token);
                tokenModel.setUserId(claims.getBody().get("userId",Integer.class));
                tokenModel.setIssuer(claims.getBody().getIssuer());
                
                System.out.println("token is valid");
                
                System.out.println("user id is " + tokenModel.getUserId() );
                tokenModel.setSubject("user");
            
            } else {            
            System.out.println("No Token");
            return null;
            }
        
return tokenModel;    
    }

}
