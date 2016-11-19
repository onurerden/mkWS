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

/**
 *
 * @author onurerden
 */
public class TokenEvaluator {
    
    public int evaluateRequestForToken(HttpServletRequest request){
       int userId=-1;
        if(request.getParameter("token")!=null){
            String token = request.getParameter("token");
            Credentials cr = new Credentials();
            try {
                Jws<Claims> claims = Jwts.parser()
                        .requireSubject("user")
                        .setSigningKey(cr.getJjwtKey())
                        .parseClaimsJws(token);
                userId = claims.getBody().get("userId",Integer.class);
                
                System.out.println("token is valid");
                
                System.out.println("user id is " + userId );
                
            } catch (MissingClaimException e) {
                System.out.println("missing exception");
                // we get here if the required claim is not present

            } catch (IncorrectClaimException e) {
                System.out.println("incorrect claim exception");
                // we get here if ther required claim has the wrong value

            } catch (SignatureException e) {
                System.out.println("signature exception");
            }
            } else {            
            System.out.println("No Token");
            }
return userId;    
    }

}
