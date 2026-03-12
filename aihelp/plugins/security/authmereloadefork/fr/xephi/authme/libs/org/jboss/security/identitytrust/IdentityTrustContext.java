package fr.xephi.authme.libs.org.jboss.security.identitytrust;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;

public abstract class IdentityTrustContext {
   protected IdentityTrustManager.TrustDecision DENY;
   protected IdentityTrustManager.TrustDecision PERMIT;
   protected IdentityTrustManager.TrustDecision NOTAPPLICABLE;
   protected SecurityContext securityContext;
   protected CallbackHandler callbackHandler;
   protected Map<String, Object> sharedState;
   protected String securityDomain;
   protected List<IdentityTrustModule> modules;
   protected ArrayList<ControlFlag> controlFlags;

   public IdentityTrustContext() {
      this.DENY = IdentityTrustManager.TrustDecision.Deny;
      this.PERMIT = IdentityTrustManager.TrustDecision.Permit;
      this.NOTAPPLICABLE = IdentityTrustManager.TrustDecision.NotApplicable;
      this.sharedState = new HashMap();
      this.modules = new ArrayList();
      this.controlFlags = new ArrayList();
   }

   public abstract IdentityTrustManager.TrustDecision isTrusted() throws IdentityTrustException;
}
