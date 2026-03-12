package fr.xephi.authme.libs.org.jboss.security.identitytrust;

import fr.xephi.authme.libs.org.jboss.security.BaseSecurityManager;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;

public interface IdentityTrustManager extends BaseSecurityManager {
   IdentityTrustManager.TrustDecision isTrusted(SecurityContext var1);

   public static enum TrustDecision {
      Permit,
      Deny,
      NotApplicable;
   }
}
