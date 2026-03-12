package fr.xephi.authme.libs.org.jboss.security;

import fr.xephi.authme.libs.org.jboss.security.audit.AuditManager;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingManager;

public interface SecurityManagerLocator {
   AuditManager getAuditManager();

   AuthenticationManager getAuthenticationManager();

   AuthorizationManager getAuthorizationManager();

   IdentityTrustManager getIdentityTrustManager();

   MappingManager getMappingManager();
}
