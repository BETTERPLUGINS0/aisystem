package fr.xephi.authme.libs.org.jboss.security.audit;

import fr.xephi.authme.libs.org.jboss.security.BaseSecurityManager;

public interface AuditManager extends BaseSecurityManager {
   void audit(AuditEvent var1);
}
