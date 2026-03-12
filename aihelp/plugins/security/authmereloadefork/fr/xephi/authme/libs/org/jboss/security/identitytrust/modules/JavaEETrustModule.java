package fr.xephi.authme.libs.org.jboss.security.identitytrust.modules;

import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.RunAsIdentity;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustException;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;

public class JavaEETrustModule extends AbstractIdentityTrustModule {
   public IdentityTrustManager.TrustDecision isTrusted() throws IdentityTrustException {
      RunAs runAs = this.securityContext.getIncomingRunAs();
      if (runAs instanceof RunAsIdentity) {
         RunAsIdentity runAsIdentity = (RunAsIdentity)runAs;
         if ("JavaEE".equals(runAsIdentity.getProof())) {
            return IdentityTrustManager.TrustDecision.Permit;
         }
      }

      return IdentityTrustManager.TrustDecision.NotApplicable;
   }
}
