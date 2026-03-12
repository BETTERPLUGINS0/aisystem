package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import java.security.acl.Group;
import javax.security.auth.login.LoginException;

public class AnonLoginModule extends UsernamePasswordLoginModule {
   protected Group[] getRoleSets() throws LoginException {
      SimpleGroup roles = new SimpleGroup("Roles");
      Group[] roleSets = new Group[]{roles};
      return roleSets;
   }

   protected String getUsersPassword() throws LoginException {
      return null;
   }
}
