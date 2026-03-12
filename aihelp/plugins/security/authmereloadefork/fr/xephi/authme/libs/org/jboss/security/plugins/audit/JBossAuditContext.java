package fr.xephi.authme.libs.org.jboss.security.plugins.audit;

import fr.xephi.authme.libs.org.jboss.security.audit.AuditContext;

public class JBossAuditContext extends AuditContext {
   public JBossAuditContext(String sdomain) {
      this.securityDomain = sdomain;
   }
}
