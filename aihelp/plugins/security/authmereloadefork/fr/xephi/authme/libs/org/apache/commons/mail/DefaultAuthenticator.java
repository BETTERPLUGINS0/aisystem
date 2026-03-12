package fr.xephi.authme.libs.org.apache.commons.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class DefaultAuthenticator extends Authenticator {
   private final PasswordAuthentication authentication;

   public DefaultAuthenticator(String userName, String password) {
      this.authentication = new PasswordAuthentication(userName, password);
   }

   protected PasswordAuthentication getPasswordAuthentication() {
      return this.authentication;
   }
}
