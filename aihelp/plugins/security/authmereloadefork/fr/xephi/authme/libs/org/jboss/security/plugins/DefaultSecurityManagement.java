package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.AuthenticationManager;
import fr.xephi.authme.libs.org.jboss.security.AuthorizationManager;
import fr.xephi.authme.libs.org.jboss.security.ISecurityManagement;
import fr.xephi.authme.libs.org.jboss.security.JBossJSSESecurityDomain;
import fr.xephi.authme.libs.org.jboss.security.JSSESecurityDomain;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditManager;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingManager;
import fr.xephi.authme.libs.org.jboss.security.plugins.audit.JBossAuditManager;
import fr.xephi.authme.libs.org.jboss.security.plugins.identitytrust.JBossIdentityTrustManager;
import fr.xephi.authme.libs.org.jboss.security.plugins.mapping.JBossMappingManager;
import javax.security.auth.callback.CallbackHandler;

public class DefaultSecurityManagement implements ISecurityManagement {
   private static final long serialVersionUID = 1L;
   private CallbackHandler handler = null;

   public DefaultSecurityManagement(CallbackHandler cbh) {
      this.handler = cbh;
   }

   public AuditManager getAuditManager(String securityDomain) {
      return new JBossAuditManager(securityDomain);
   }

   public AuthenticationManager getAuthenticationManager(String securityDomain) {
      return new JBossAuthenticationManager(securityDomain, this.handler);
   }

   public AuthorizationManager getAuthorizationManager(String securityDomain) {
      return new JBossAuthorizationManager(securityDomain);
   }

   public IdentityTrustManager getIdentityTrustManager(String securityDomain) {
      return new JBossIdentityTrustManager(securityDomain);
   }

   public MappingManager getMappingManager(String securityDomain) {
      return new JBossMappingManager(securityDomain);
   }

   public JSSESecurityDomain getJSSE(String securityDomain) {
      return new JBossJSSESecurityDomain(securityDomain);
   }
}
