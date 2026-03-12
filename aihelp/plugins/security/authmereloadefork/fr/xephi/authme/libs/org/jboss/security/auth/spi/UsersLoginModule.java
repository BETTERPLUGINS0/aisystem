package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.acl.Group;
import java.util.Map;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class UsersLoginModule extends UsernamePasswordLoginModule {
   private static final String USER_PROPERTIES = "usersProperties";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"usersProperties"};
   private String usersRsrcName = "users.properties";
   private Properties users;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);

      try {
         String option = (String)options.get("usersProperties");
         if (option != null) {
            this.usersRsrcName = option;
         }

         this.loadUsers();
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.errorLoadingUserRolesPropertiesFiles(var6);
      }

   }

   public boolean login() throws LoginException {
      if (this.users == null) {
         throw PicketBoxMessages.MESSAGES.missingPropertiesFile(this.usersRsrcName);
      } else {
         return super.login();
      }
   }

   protected Group[] getRoleSets() throws LoginException {
      return new Group[0];
   }

   protected String getUsersPassword() {
      String username = this.getUsername();
      String password = null;
      if (username != null) {
         password = this.users.getProperty(username, (String)null);
      }

      return password;
   }

   private void loadUsers() throws IOException {
      this.users = this.loadProperties(this.usersRsrcName);
   }

   private Properties loadProperties(String propertiesName) throws IOException {
      Properties bundle = null;
      ClassLoader loader = SecurityActions.getContextClassLoader();
      URL url = loader.getResource(propertiesName);
      if (url == null) {
         throw PicketBoxMessages.MESSAGES.unableToFindPropertiesFile(propertiesName);
      } else {
         InputStream is = null;

         Properties var6;
         try {
            is = url.openStream();
            if (is == null) {
               throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
            }

            bundle = new Properties();
            bundle.load(is);
            var6 = bundle;
         } finally {
            this.safeClose(is);
         }

         return var6;
      }
   }
}
