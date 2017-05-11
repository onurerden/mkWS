/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import mkws.api.SendTestEmail;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author onurerden
 */
public class MailSender {

    public void sendMail(String to, String message, String subject) {
        Credentials cr = new Credentials();
        HtmlEmail email2 = new HtmlEmail();
        email2.setHostName(cr.getEmailHost());
        email2.setSmtpPort(465);
        email2.setAuthenticator(new DefaultAuthenticator(cr.getEmailUserName(), cr.getEmailUserPassword()));
        email2.setSSLOnConnect(true);
        try {
            email2.setFrom("no-reply@followmeapp.xyz");
            email2.setSubject(subject);
            // set the html message
            email2.setHtmlMsg(message);

            // set the alternative message
            email2.setTextMsg("Your email client does not support HTML messages");

            email2.addTo(to);
            email2.send();
        } catch (EmailException ex) {
            Logger.getLogger(SendTestEmail.class.getName()).log(Level.SEVERE, null, ex);
            ServerEngine server = new ServerEngine();
            LogMessage log = new LogMessage();
            log.setLogLevel(1);
            log.setLogMessage(ex.getLocalizedMessage());
            Gson gson = new Gson();
            server.sendLog(gson.toJson(log));
        }
    }
}
