package fr.xephi.authme.libs.org.jboss.security;

import fr.xephi.authme.libs.org.jboss.security.identity.extensions.CredentialIdentity;
import java.security.Principal;
import javax.security.auth.Subject;

public class SecurityIdentity {
   SubjectInfo theSubjectInfo = null;
   RunAs runAs = null;
   RunAs callerRunAs = null;

   public SecurityIdentity(SubjectInfo subject, RunAs outgoingRunAs, RunAs incomingRunAs) {
      this.theSubjectInfo = subject;
      this.runAs = outgoingRunAs;
      this.callerRunAs = incomingRunAs;
   }

   public Principal getPrincipal() {
      if (this.theSubjectInfo != null) {
         CredentialIdentity<?> identity = (CredentialIdentity)this.theSubjectInfo.getIdentity(CredentialIdentity.class);
         if (identity != null) {
            return identity.asPrincipal();
         }
      }

      return null;
   }

   public Object getCredential() {
      if (this.theSubjectInfo != null) {
         CredentialIdentity<?> identity = (CredentialIdentity)this.theSubjectInfo.getIdentity(CredentialIdentity.class);
         if (identity != null) {
            return identity.getCredential();
         }
      }

      return null;
   }

   public Subject getSubject() {
      return this.theSubjectInfo != null ? this.theSubjectInfo.getAuthenticatedSubject() : null;
   }

   public RunAs getOutgoingRunAs() {
      return this.runAs;
   }

   public RunAs getIncomingRunAs() {
      return this.callerRunAs;
   }
}
