package fr.xephi.authme.libs.org.jboss.security;

import fr.xephi.authme.libs.org.jboss.security.audit.AuditManager;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingManager;
import java.io.Serializable;

public interface ISecurityManagement extends Serializable {
   AuthenticationManager getAuthenticationManager(String var1);

   AuthorizationManager getAuthorizationManager(String var1);

   MappingManager getMappingManager(String var1);

   AuditManager getAuditManager(String var1);

   IdentityTrustManager getIdentityTrustManager(String var1);

   JSSESecurityDomain getJSSE(String var1);
}
