package fr.xephi.authme.libs.org.jboss.security.plugins.identitytrust;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustContext;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustException;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.JBossIdentityTrustContext;

public class JBossIdentityTrustManager implements IdentityTrustManager {
   private String securityDomain = null;
   private IdentityTrustContext identityTrustContext = null;

   public JBossIdentityTrustManager(String securityDomain) {
      this.securityDomain = securityDomain;
   }

   public void setIdentityTrustContext(IdentityTrustContext identityTrustContext) {
      if (identityTrustContext == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("identityTrustContext");
      } else {
         this.identityTrustContext = identityTrustContext;
      }
   }

   public IdentityTrustManager.TrustDecision isTrusted(SecurityContext securityContext) {
      if (securityContext == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("securityContext");
      } else {
         if (this.identityTrustContext == null) {
            this.identityTrustContext = new JBossIdentityTrustContext(this.securityDomain, securityContext);
         }

         IdentityTrustManager.TrustDecision td = IdentityTrustManager.TrustDecision.NotApplicable;
         if (this.identityTrustContext == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("identityTrustContext");
         } else {
            try {
               td = this.identityTrustContext.isTrusted();
            } catch (IdentityTrustException var4) {
               PicketBoxLogger.LOGGER.debugIgnoredException(var4);
            }

            return td;
         }
      }
   }

   public String getSecurityDomain() {
      return this.securityDomain;
   }
}
