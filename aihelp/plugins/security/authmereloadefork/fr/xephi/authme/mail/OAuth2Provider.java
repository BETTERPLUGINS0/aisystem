package fr.xephi.authme.mail;

import java.security.Provider;

public class OAuth2Provider extends Provider {
   private static final long serialVersionUID = 1L;

   public OAuth2Provider() {
      super("Google OAuth2 Provider", 1.0D, "Provides the XOAUTH2 SASL Mechanism");
      this.put("SaslClientFactory.XOAUTH2", "fr.xephi.authme.mail.OAuth2SaslClientFactory");
   }
}
