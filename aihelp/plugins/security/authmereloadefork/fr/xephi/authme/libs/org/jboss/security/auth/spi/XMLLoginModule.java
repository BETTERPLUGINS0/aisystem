package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.security.acl.Group;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class XMLLoginModule extends UsernamePasswordLoginModule {
   private static final String USER_INFO = "userInfo";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"userInfo"};
   private Users users;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);

      try {
         this.users = (Users)options.get("userInfo");
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.errorLoadingUserRolesPropertiesFiles(var6);
      }

   }

   public boolean login() throws LoginException {
      if (this.users == null) {
         throw PicketBoxMessages.MESSAGES.missingXMLUserRolesMapping();
      } else {
         return super.login();
      }
   }

   protected Group[] getRoleSets() throws LoginException {
      String targetUser = this.getUsername();
      Users.User user = this.users.getUser(targetUser);
      Group[] roleSets = new Group[0];
      if (user != null) {
         roleSets = user.getRoleSets();
      }

      return roleSets;
   }

   protected String getUsersPassword() {
      String username = this.getUsername();
      Users.User user = this.users.getUser(username);
      String password = null;
      if (user != null) {
         password = user.getPassword();
      }

      return password;
   }
}
