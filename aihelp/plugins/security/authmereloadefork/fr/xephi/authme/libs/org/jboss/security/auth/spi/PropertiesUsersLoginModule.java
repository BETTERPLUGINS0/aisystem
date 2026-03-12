package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import java.io.IOException;
import java.security.acl.Group;
import java.util.Map;
import java.util.Properties;
import javax.security.auth.login.LoginException;

public class PropertiesUsersLoginModule extends UsersRolesLoginModule {
   protected Group[] getRoleSets() throws LoginException {
      return new Group[0];
   }

   protected Properties createRoles(Map<String, ?> options) throws IOException {
      return new Properties();
   }
}
