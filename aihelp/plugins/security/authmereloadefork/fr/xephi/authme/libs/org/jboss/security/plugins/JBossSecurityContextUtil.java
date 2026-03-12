package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.RunAsIdentity;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextUtil;
import fr.xephi.authme.libs.org.jboss.security.SecurityIdentity;
import fr.xephi.authme.libs.org.jboss.security.SubjectInfo;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.extensions.CredentialIdentity;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import javax.security.auth.Subject;

public class JBossSecurityContextUtil extends SecurityContextUtil {
   public JBossSecurityContextUtil(SecurityContext sc) {
      this.securityContext = sc;
   }

   public <T> T get(String key) {
      this.validateSecurityContext();
      return "RunAsIdentity".equals(key) ? this.securityContext.getOutgoingRunAs() : this.securityContext.getData().get(key);
   }

   public String getUserName() {
      Principal p = this.getUserPrincipal();
      return p != null ? p.getName() : null;
   }

   public Principal getUserPrincipal() {
      this.validateSecurityContext();
      Principal p = null;
      SubjectInfo subjectInfo = this.securityContext.getSubjectInfo();
      if (subjectInfo != null) {
         CredentialIdentity<?> cIdentity = (CredentialIdentity)subjectInfo.getIdentity(CredentialIdentity.class);
         p = cIdentity != null ? cIdentity.asPrincipal() : null;
      }

      return p;
   }

   public Object getCredential() {
      this.validateSecurityContext();
      Object cred = null;
      SubjectInfo subjectInfo = this.securityContext.getSubjectInfo();
      if (subjectInfo != null) {
         CredentialIdentity<?> cIdentity = (CredentialIdentity)subjectInfo.getIdentity(CredentialIdentity.class);
         cred = cIdentity != null ? cIdentity.getCredential() : null;
      }

      return cred;
   }

   public Subject getSubject() {
      this.validateSecurityContext();
      Subject s = null;
      SubjectInfo subjectInfo = this.securityContext.getSubjectInfo();
      if (subjectInfo != null) {
         s = subjectInfo.getAuthenticatedSubject();
      }

      return s;
   }

   public <T> void set(String key, T obj) {
      this.validateSecurityContext();
      if (key == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("key");
      } else {
         if (obj != null) {
            if ("RunAsIdentity".equals(key) && !(obj instanceof RunAsIdentity)) {
               throw PicketBoxMessages.MESSAGES.invalidType(RunAsIdentity.class.getName());
            }

            if ("Roles".equals(key) && !(obj instanceof Group)) {
               throw PicketBoxMessages.MESSAGES.invalidType(Group.class.getName());
            }
         }

         if ("RunAsIdentity".equals(key)) {
            this.setRunAsIdentity((RunAsIdentity)obj);
         } else {
            this.securityContext.getData().put(key, obj);
         }

      }
   }

   public <T> T remove(String key) {
      if (key == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("key");
      } else {
         Map<String, Object> contextMap = this.securityContext.getData();
         if ("RunAsIdentity".equals(key)) {
            RunAs runAs = this.securityContext.getOutgoingRunAs();
            this.securityContext.setOutgoingRunAs((RunAs)contextMap.get("CallerRunAsIdentity"));
            contextMap.remove("CallerRunAsIdentity");
            return runAs;
         } else {
            return contextMap.remove(key);
         }
      }
   }

   public void setRoles(RoleGroup roles) {
      this.validateSecurityContext();
      this.securityContext.getSubjectInfo().setRoles(roles);
   }

   public void setSecurityIdentity(SecurityIdentity sidentity) {
      this.createSubjectInfo(sidentity.getPrincipal(), sidentity.getCredential(), sidentity.getSubject());
      this.securityContext.setOutgoingRunAs(sidentity.getOutgoingRunAs());
      this.securityContext.setIncomingRunAs(sidentity.getIncomingRunAs());
   }

   public SecurityIdentity getSecurityIdentity() {
      return new SecurityIdentity(this.securityContext.getSubjectInfo(), this.securityContext.getOutgoingRunAs(), this.securityContext.getIncomingRunAs());
   }

   private void setRunAsIdentity(RunAsIdentity rai) {
      Map<String, Object> contextMap = this.securityContext.getData();
      RunAs currentRA = this.securityContext.getOutgoingRunAs();
      contextMap.put("CallerRunAsIdentity", currentRA);
      this.securityContext.setOutgoingRunAs(rai);
   }

   public RoleGroup getRoles() {
      this.validateSecurityContext();
      return this.securityContext.getSubjectInfo().getRoles();
   }

   private void validateSecurityContext() {
      if (this.securityContext == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("securityDomain");
      }
   }
}
