package fr.xephi.authme.libs.org.picketbox.datasource.security;

import fr.xephi.authme.libs.org.jboss.security.auth.spi.AbstractServerLoginModule;
import javax.security.auth.login.LoginException;

public abstract class AbstractPasswordCredentialLoginModule extends AbstractServerLoginModule {
   public boolean logout() throws LoginException {
      this.removeCredentials();
      return super.logout();
   }

   protected void removeCredentials() {
      this.sharedState.remove("javax.security.auth.login.name");
      this.sharedState.remove("javax.security.auth.login.password");
      SubjectActions.removeCredentials(this.subject);
   }
}
