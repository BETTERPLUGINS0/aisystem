package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.security.Principal;
import java.security.acl.Group;
import javax.security.auth.login.LoginException;

public class SimpleServerLoginModule extends UsernamePasswordLoginModule {
   private SimplePrincipal user;
   private boolean guestOnly;

   protected Principal getIdentity() {
      Principal principal = this.user;
      if (principal == null) {
         principal = super.getIdentity();
      }

      return (Principal)principal;
   }

   protected boolean validatePassword(String inputPassword, String expectedPassword) {
      boolean isValid = false;
      if (inputPassword == null) {
         this.guestOnly = true;
         isValid = true;
         this.user = new SimplePrincipal("guest");
      } else {
         isValid = inputPassword.equals(expectedPassword);
      }

      return isValid;
   }

   protected Group[] getRoleSets() throws LoginException {
      Group[] roleSets = new Group[]{new SimpleGroup("Roles")};
      if (!this.guestOnly) {
         roleSets[0].addMember(new SimplePrincipal("user"));
      }

      roleSets[0].addMember(new SimplePrincipal("guest"));
      return roleSets;
   }

   protected String getUsersPassword() throws LoginException {
      return this.getUsername();
   }

   public boolean logout() throws LoginException {
      Group[] groups = this.getRoleSets();
      this.subject.getPrincipals().remove(groups[0]);
      return super.logout();
   }
}
