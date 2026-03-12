package fr.xephi.authme.libs.org.jboss.security.client;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextFactory;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.security.Principal;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class JBossSecurityClient extends SecurityClient {
   protected LoginContext lc = null;
   private SecurityContext previousSecurityContext = null;

   protected void peformSASLLogin() {
      throw new UnsupportedOperationException();
   }

   protected void performJAASLogin() throws LoginException {
      this.lc = new LoginContext(this.loginConfigName, this.callbackHandler);
      this.lc.login();
   }

   protected void performSimpleLogin() {
      Principal up = null;
      if (this.userPrincipal instanceof String) {
         up = new SimplePrincipal((String)this.userPrincipal);
      } else {
         up = (Principal)this.userPrincipal;
      }

      this.previousSecurityContext = SecurityContextAssociation.getSecurityContext();
      SecurityContext sc = null;

      try {
         sc = SecurityContextFactory.createSecurityContext("CLIENT");
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }

      sc.getUtil().createSubjectInfo((Principal)up, this.credential, (Subject)null);
      SecurityContextAssociation.setSecurityContext(sc);
   }

   protected void cleanUp() {
      SecurityContextAssociation.setSecurityContext(this.previousSecurityContext);
      if (this.lc != null) {
         try {
            this.lc.logout();
         } catch (LoginException var2) {
            throw new RuntimeException(var2);
         }
      }

   }
}
