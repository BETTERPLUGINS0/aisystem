package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

public class MemoryUsersRolesLoginModule extends UsersRolesLoginModule {
   private static final String USERS = "users";
   private static final String ROLES = "roles";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"users", "roles"};
   private Properties users;
   private Properties roles;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.users = (Properties)options.get("users");
      this.roles = (Properties)options.get("roles");
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
   }

   protected Properties createUsers(Map<String, ?> options) {
      return this.users;
   }

   protected Properties createRoles(Map<String, ?> options) throws IOException {
      return this.roles;
   }
}
