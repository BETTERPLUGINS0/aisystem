package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.plugins.auth.JaasSecurityManagerBase;
import javax.security.auth.callback.CallbackHandler;

public class JBossAuthenticationManager extends JaasSecurityManagerBase {
   public JBossAuthenticationManager() {
   }

   public JBossAuthenticationManager(String securityDomain, CallbackHandler handler) {
      super(securityDomain, handler);
   }
}
