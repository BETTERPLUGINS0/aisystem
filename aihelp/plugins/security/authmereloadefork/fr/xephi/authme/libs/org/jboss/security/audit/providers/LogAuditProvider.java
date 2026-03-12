package fr.xephi.authme.libs.org.jboss.security.audit.providers;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.audit.AbstractAuditProvider;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditEvent;

public class LogAuditProvider extends AbstractAuditProvider {
   public void audit(AuditEvent auditEvent) {
      if (PicketBoxLogger.AUDIT_LOGGER.isTraceEnabled()) {
         Exception e = auditEvent.getUnderlyingException();
         if (e != null) {
            PicketBoxLogger.AUDIT_LOGGER.trace(auditEvent, e);
         } else {
            PicketBoxLogger.AUDIT_LOGGER.trace(auditEvent);
         }

      }
   }
}
