package fr.xephi.authme.libs.org.jboss.security;

import java.security.AccessController;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.KeyStore;
import java.security.PermissionCollection;
import java.security.Permissions;
import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;

public class AppPolicy {
   public static final PermissionCollection NO_PERMISSIONS = new Permissions();
   private static PermissionCollection ALL_PERMISSIONS;
   private static AppPolicy defaultAppPolicy;
   private String appName;
   private KeyStore keyStore;
   private AuthorizationInfo permissionInfo;
   private AuthenticationInfo loginInfo;

   public KeyStore getKeyStore() {
      return this.keyStore;
   }

   public void setKeyStore(KeyStore keyStore) {
      this.keyStore = keyStore;
   }

   public static void setDefaultAppPolicy(AppPolicy policy) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(AppPolicy.class.getName() + ".setDefaultAppPolicy"));
      }

      if (policy == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("policy");
      } else {
         defaultAppPolicy = policy;
      }
   }

   public static AppPolicy getDefaultAppPolicy() {
      return defaultAppPolicy;
   }

   public AppPolicy(String appName) {
      this.appName = appName;
   }

   public AuthenticationInfo getLoginInfo() {
      AccessController.checkPermission(AuthenticationInfo.GET_CONFIG_ENTRY_PERM);
      return this.loginInfo;
   }

   public void setLoginInfo(AuthenticationInfo loginInfo) {
      AccessController.checkPermission(AuthenticationInfo.SET_CONFIG_ENTRY_PERM);
      this.loginInfo = loginInfo;
   }

   public AuthorizationInfo getPermissionInfo() {
      return this.permissionInfo;
   }

   public void setPermissionInfo(AuthorizationInfo permissionInfo) {
      this.permissionInfo = permissionInfo;
   }

   public AppConfigurationEntry[] getAppConfigurationEntry() {
      AppConfigurationEntry[] appConfig = null;
      if (this.loginInfo != null) {
         appConfig = this.loginInfo.getAppConfigurationEntry();
      }

      if (appConfig == null && this != defaultAppPolicy) {
         appConfig = defaultAppPolicy.getAppConfigurationEntry();
      }

      AppConfigurationEntry[] copy = null;
      if (appConfig != null) {
         copy = new AppConfigurationEntry[appConfig.length];

         for(int c = 0; c < copy.length; ++c) {
            AppConfigurationEntry e0 = appConfig[c];
            AppConfigurationEntry e1 = new AppConfigurationEntry(e0.getLoginModuleName(), e0.getControlFlag(), e0.getOptions());
            copy[c] = e1;
         }
      }

      return copy;
   }

   public PermissionCollection getPermissions(Subject subject, CodeSource codesource) {
      PermissionCollection perms = NO_PERMISSIONS;
      AuthorizationInfo info = this.getPermissionInfo();
      if (info == null) {
         info = defaultAppPolicy.getPermissionInfo();
      }

      if (info != null) {
         perms = info.getPermissions(subject, codesource);
      }

      return perms;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer(this.appName);
      buffer.append('\n');
      buffer.append("AuthenticationInfo:\n");
      if (this.loginInfo != null) {
         buffer.append(this.loginInfo);
      }

      buffer.append("AuthorizationInfo:\n");
      if (this.permissionInfo != null) {
         buffer.append(this.permissionInfo);
      }

      return buffer.toString();
   }

   static {
      AllPermission all = new AllPermission();
      ALL_PERMISSIONS = all.newPermissionCollection();
      ALL_PERMISSIONS.add(all);
      defaultAppPolicy = new AppPolicy("other");
   }
}
