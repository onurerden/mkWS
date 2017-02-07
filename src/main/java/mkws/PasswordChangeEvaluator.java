/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

/**
 *
 * @author onurerden
 */
public class PasswordChangeEvaluator {

    public boolean changePassword(String token) {

        Credentials cr = new Credentials();
        ServerEngine server = new ServerEngine();
        boolean result = false;
        try {
            Jws<Claims> claims = Jwts.parser()
                    .requireSubject("passwordReset")
                    .setSigningKey(cr.getJjwtKey())
                    .parseClaimsJws(token);
            result = server.resetPassword(claims.getBody().get("userId", Integer.class));

        } catch (ExpiredJwtException ex) {
            System.out.println("Reset Password token is expired." + ex.getMessage());
            return false;
        }
        return result;

    }

}
