package fr.xephi.authme.libs.org.jboss.security.javaee;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustException;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;
import java.security.Principal;
import java.util.Map;
import javax.security.auth.Subject;

public class EJBAuthenticationHelper extends AbstractJavaEEHelper {
   public EJBAuthenticationHelper(SecurityContext sc) {
      if (sc == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security context");
      } else {
         this.securityContext = sc;
      }
   }

   public boolean isTrusted() throws IdentityTrustException {
      IdentityTrustManager.TrustDecision td = IdentityTrustManager.TrustDecision.NotApplicable;
      IdentityTrustManager itm = this.securityContext.getIdentityTrustManager();
      if (itm != null) {
         td = itm.isTrusted(this.securityContext);
         if (td == IdentityTrustManager.TrustDecision.Deny) {
            throw new IdentityTrustException(PicketBoxMessages.MESSAGES.deniedByIdentityTrustMessage());
         }
      }

      return td == IdentityTrustManager.TrustDecision.Permit;
   }

   public boolean isValid(Subject subject, String methodName) {
      if (subject == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("subject");
      } else if (methodName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("method");
      } else {
         Principal p = this.securityContext.getUtil().getUserPrincipal();
         Object cred = this.securityContext.getUtil().getCredential();
         Map<String, Object> cMap = this.getContextMap(p, methodName);
         boolean auth = this.securityContext.getAuthenticationManager().isValid(p, cred, subject);
         if (!auth) {
            String EX_KEY = "fr.xephi.authme.libs.org.jboss.security.exception";
            Exception ex = (Exception)this.securityContext.getData().get(EX_KEY);
            if (ex == null) {
               this.authenticationAudit("Failure", cMap, (Exception)null);
            } else {
               this.authenticationAudit("Error", cMap, ex);
            }
         } else {
            this.authenticationAudit("Success", cMap, (Exception)null);
         }

         return auth;
      }
   }

   public void pushSubjectContext(Subject subject) {
      this.securityContext.getSubjectInfo().setAuthenticatedSubject(subject);
   }
}
