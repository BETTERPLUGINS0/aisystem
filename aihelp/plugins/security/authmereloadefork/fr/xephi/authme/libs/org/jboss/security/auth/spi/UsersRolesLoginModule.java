package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.util.StringPropertyReplacer;
import java.io.IOException;
import java.security.acl.Group;
import java.util.Map;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class UsersRolesLoginModule extends UsernamePasswordLoginModule {
   private static final String USER_PROPERTIES = "usersProperties";
   private static final String DEFAULT_USER_PROPERTIES = "defaultUsersProperties";
   private static final String ROLES_PROPERTIES = "rolesProperties";
   private static final String DEFAULT_ROLES_PROPERTIES = "defaultRolesProperties";
   private static final String ROLE_GROUP_SEPERATOR = "roleGroupSeperator";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"usersProperties", "defaultUsersProperties", "rolesProperties", "defaultRolesProperties", "roleGroupSeperator"};
   private String defaultUsersRsrcName = "defaultUsers.properties";
   private String defaultRolesRsrcName = "defaultRoles.properties";
   private String usersRsrcName = "users.properties";
   private String rolesRsrcName = "roles.properties";
   private Properties users;
   private Properties roles;
   private char roleGroupSeperator = '.';

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);

      try {
         String option = (String)options.get("usersProperties");
         if (option != null) {
            this.usersRsrcName = StringPropertyReplacer.replaceProperties(option);
         }

         option = (String)options.get("defaultUsersProperties");
         if (option != null) {
            this.defaultUsersRsrcName = StringPropertyReplacer.replaceProperties(option);
         }

         option = (String)options.get("rolesProperties");
         if (option != null) {
            this.rolesRsrcName = StringPropertyReplacer.replaceProperties(option);
         }

         option = (String)options.get("defaultRolesProperties");
         if (option != null) {
            this.defaultRolesRsrcName = StringPropertyReplacer.replaceProperties(option);
         }

         option = (String)options.get("roleGroupSeperator");
         if (option != null) {
            this.roleGroupSeperator = option.charAt(0);
         }

         this.users = this.createUsers(options);
         this.roles = this.createRoles(options);
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.errorLoadingUserRolesPropertiesFiles(var6);
      }

   }

   public boolean login() throws LoginException {
      if (this.users == null) {
         throw PicketBoxMessages.MESSAGES.missingPropertiesFile(this.usersRsrcName);
      } else if (this.roles == null) {
         throw PicketBoxMessages.MESSAGES.missingPropertiesFile(this.rolesRsrcName);
      } else {
         return super.login();
      }
   }

   protected Group[] getRoleSets() throws LoginException {
      String targetUser = this.getUsername();
      Group[] roleSets = Util.getRoleSets(targetUser, this.roles, this.roleGroupSeperator, this);
      return roleSets;
   }

   protected String getUsersPassword() {
      String username = this.getUsername();
      String password = null;
      if (username != null) {
         password = this.users.getProperty(username, (String)null);
      }

      return password;
   }

   protected void loadUsers() throws IOException {
      this.users = Util.loadProperties(this.defaultUsersRsrcName, this.usersRsrcName);
   }

   protected Properties createUsers(Map<String, ?> options) throws IOException {
      this.loadUsers();
      return this.users;
   }

   protected void loadRoles() throws IOException {
      this.roles = Util.loadProperties(this.defaultRolesRsrcName, this.rolesRsrcName);
   }

   protected Properties createRoles(Map<String, ?> options) throws IOException {
      this.loadRoles();
      return this.roles;
   }

   protected void parseGroupMembers(Group group, String roles) {
      Util.parseGroupMembers(group, roles, this);
   }
}
