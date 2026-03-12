package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.AuthenticationManager;
import fr.xephi.authme.libs.org.jboss.security.ISecurityManagement;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SubjectFactory;
import fr.xephi.authme.libs.org.jboss.security.auth.callback.JBossCallbackHandler;
import java.security.Principal;
import javax.security.auth.Subject;

public class JBossSecuritySubjectFactory implements SubjectFactory {
   private ISecurityManagement securityManagement;

   public Subject createSubject() {
      return this.createSubject("other");
   }

   public Subject createSubject(String securityDomainName) {
      if (this.securityManagement == null) {
         PicketBoxLogger.LOGGER.warnSecurityMagementNotSet();
         this.securityManagement = new DefaultSecurityManagement(new JBossCallbackHandler());
      }

      Subject subject = new Subject();
      Principal principal = SubjectActions.getPrincipal();
      AuthenticationManager authenticationManager = this.securityManagement.getAuthenticationManager(securityDomainName);
      if (authenticationManager == null) {
         String defaultSecurityDomain = "other";
         PicketBoxLogger.LOGGER.debugNullAuthenticationManager(securityDomainName);
         authenticationManager = this.securityManagement.getAuthenticationManager(defaultSecurityDomain);
      }

      ClassLoader tccl = SubjectActions.getContextClassLoader();

      try {
         SubjectActions.setContextClassLoader(this.getClass().getClassLoader());
         if (!authenticationManager.isValid(principal, SubjectActions.getCredential(), subject)) {
            throw new SecurityException(PicketBoxMessages.MESSAGES.authenticationFailedMessage());
         }
      } finally {
         SubjectActions.setContextClassLoader(tccl);
      }

      return subject;
   }

   public void setSecurityManagement(ISecurityManagement securityManagement) {
      this.securityManagement = securityManagement;
   }
}
