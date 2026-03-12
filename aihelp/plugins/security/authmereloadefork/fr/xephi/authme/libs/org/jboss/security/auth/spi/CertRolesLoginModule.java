package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.IOException;
import java.security.acl.Group;
import java.util.Map;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class CertRolesLoginModule extends BaseCertLoginModule {
   private static final String ROLES_PROPERTIES = "rolesProperties";
   private static final String DEFAULT_ROLES_PROPERTIES = "defaultRolesProperties";
   private static final String ROLE_GROUP_SEPERATOR = "roleGroupSeperator";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"rolesProperties", "defaultRolesProperties", "roleGroupSeperator"};
   private String defaultRolesRsrcName = "defaultRoles.properties";
   private String rolesRsrcName = "roles.properties";
   private Properties roles;
   private char roleGroupSeperator = '.';

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      PicketBoxLogger.LOGGER.traceBeginInitialize();

      try {
         String option = (String)options.get("rolesProperties");
         if (option != null) {
            this.rolesRsrcName = option;
         }

         option = (String)options.get("defaultRolesProperties");
         if (option != null) {
            this.defaultRolesRsrcName = option;
         }

         option = (String)options.get("roleGroupSeperator");
         if (option != null) {
            this.roleGroupSeperator = option.charAt(0);
         }

         this.loadRoles();
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.errorLoadingUserRolesPropertiesFiles(var6);
      }

      PicketBoxLogger.LOGGER.traceEndInitialize();
   }

   public boolean login() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      if (this.roles == null) {
         throw PicketBoxMessages.MESSAGES.missingPropertiesFile(this.rolesRsrcName);
      } else {
         boolean wasSuccessful = super.login();
         PicketBoxLogger.LOGGER.traceEndLogin(wasSuccessful);
         return wasSuccessful;
      }
   }

   protected Group[] getRoleSets() throws LoginException {
      String targetUser = this.getUsername();
      Group[] roleSets = Util.getRoleSets(targetUser, this.roles, this.roleGroupSeperator, this);
      return roleSets;
   }

   private void loadRoles() throws IOException {
      this.roles = Util.loadProperties(this.defaultRolesRsrcName, this.rolesRsrcName);
   }
}
