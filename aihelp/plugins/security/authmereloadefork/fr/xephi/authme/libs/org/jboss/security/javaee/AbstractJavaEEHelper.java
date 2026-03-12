package fr.xephi.authme.libs.org.jboss.security.javaee;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditEvent;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditManager;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractJavaEEHelper {
   protected SecurityContext securityContext;
   protected PolicyRegistration policyRegistration;

   public SecurityContext getSecurityContext() {
      return this.securityContext;
   }

   public void setSecurityContext(SecurityContext sc) {
      if (sc == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security context");
      } else {
         this.securityContext = sc;
      }
   }

   public PolicyRegistration getPolicyRegistration() {
      return this.policyRegistration;
   }

   public void setPolicyRegistration(PolicyRegistration policyRegistration) {
      this.policyRegistration = policyRegistration;
   }

   public Principal getCallerPrincipal() {
      return (Principal)AccessController.doPrivileged(new PrivilegedAction<Principal>() {
         public Principal run() {
            Principal caller = null;
            if (AbstractJavaEEHelper.this.securityContext != null) {
               caller = AbstractJavaEEHelper.this.securityContext.getIncomingRunAs();
               if (caller == null) {
                  caller = AbstractJavaEEHelper.this.securityContext.getUtil().getUserPrincipal();
               }
            }

            return (Principal)caller;
         }
      });
   }

   protected void authorizationAudit(String level, Resource resource, Exception e) {
      if (this.securityContext.getAuditManager() != null) {
         Map<String, Object> contextualMap = resource.getMap();
         Map<String, Object> auditContextMap = new HashMap(contextualMap.size() + 3);
         auditContextMap.putAll(contextualMap);
         auditContextMap.put("Resource:", resource);
         auditContextMap.put("Action", "authorization");
         if (e != null) {
            String exceptionMessage = e != null ? e.getLocalizedMessage() : "";
            auditContextMap.put("Exception:", exceptionMessage);
         }

         if (e instanceof AuthorizationException) {
            this.audit("Failure", auditContextMap, (Exception)null);
         } else {
            this.audit(level, auditContextMap, (Exception)null);
         }

      }
   }

   protected void authenticationAudit(String level, Map<String, Object> contextMap, Exception e) {
      if (contextMap != null) {
         contextMap.put("Action", "authentication");
      }

      this.audit(level, contextMap, e);
   }

   protected void audit(String level, Map<String, Object> contextMap, Exception e) {
      AuditManager am = this.securityContext.getAuditManager();
      if (am != null) {
         contextMap.put("Source", this.getClass().getName());
         AuditEvent ae = new AuditEvent(level, contextMap, e);
         am.audit(ae);
      }
   }

   protected Map<String, Object> getContextMap(Principal principal, String methodName) {
      Map<String, Object> cmap = new HashMap();
      cmap.put("principal", principal);
      cmap.put("method", methodName);
      return cmap;
   }
}
