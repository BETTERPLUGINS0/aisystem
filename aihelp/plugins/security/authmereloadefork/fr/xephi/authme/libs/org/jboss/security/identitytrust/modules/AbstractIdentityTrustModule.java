package fr.xephi.authme.libs.org.jboss.security.identitytrust.modules;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustException;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustModule;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;

public abstract class AbstractIdentityTrustModule implements IdentityTrustModule {
   protected SecurityContext securityContext;
   protected CallbackHandler callbackHandler;
   protected Map<String, Object> sharedState;
   protected Map<String, Object> options;

   public boolean abort() throws IdentityTrustException {
      return true;
   }

   public boolean commit() throws IdentityTrustException {
      return true;
   }

   public void initialize(SecurityContext sc, CallbackHandler handler, Map<String, Object> sharedState, Map<String, Object> options) throws IdentityTrustException {
      this.securityContext = sc;
      this.callbackHandler = handler;
      this.sharedState = sharedState;
      this.options = options;
   }

   public abstract IdentityTrustManager.TrustDecision isTrusted() throws IdentityTrustException;
}
