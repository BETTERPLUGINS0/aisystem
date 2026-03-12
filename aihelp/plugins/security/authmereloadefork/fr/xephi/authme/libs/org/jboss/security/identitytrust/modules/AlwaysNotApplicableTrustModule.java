package fr.xephi.authme.libs.org.jboss.security.identitytrust.modules;

import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustException;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;

public class AlwaysNotApplicableTrustModule extends AbstractIdentityTrustModule {
   public IdentityTrustManager.TrustDecision isTrusted() throws IdentityTrustException {
      return IdentityTrustManager.TrustDecision.NotApplicable;
   }
}
