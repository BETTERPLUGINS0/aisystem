package fr.xephi.authme.libs.org.jboss.security.identitytrust;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;

public interface IdentityTrustModule {
   boolean abort() throws IdentityTrustException;

   boolean commit() throws IdentityTrustException;

   void initialize(SecurityContext var1, CallbackHandler var2, Map<String, Object> var3, Map<String, Object> var4) throws IdentityTrustException;

   IdentityTrustManager.TrustDecision isTrusted() throws IdentityTrustException;
}
