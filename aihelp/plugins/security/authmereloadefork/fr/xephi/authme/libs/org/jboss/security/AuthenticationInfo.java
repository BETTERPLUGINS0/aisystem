package fr.xephi.authme.libs.org.jboss.security;

import java.security.AccessController;
import javax.security.auth.AuthPermission;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;

public class AuthenticationInfo {
   public static final AuthPermission GET_CONFIG_ENTRY_PERM = new AuthPermission("getLoginConfiguration");
   public static final AuthPermission SET_CONFIG_ENTRY_PERM = new AuthPermission("setLoginConfiguration");
   private AppConfigurationEntry[] loginModules;
   private CallbackHandler callbackHandler;

   public AppConfigurationEntry[] getAppConfigurationEntry() {
      AccessController.checkPermission(GET_CONFIG_ENTRY_PERM);
      return this.loginModules;
   }

   public void setAppConfigurationEntry(AppConfigurationEntry[] loginModules) {
      AccessController.checkPermission(SET_CONFIG_ENTRY_PERM);
      this.loginModules = loginModules;
   }

   public CallbackHandler getAppCallbackHandler() {
      return this.callbackHandler;
   }

   public void setAppCallbackHandler(CallbackHandler handler) {
      this.callbackHandler = handler;
   }
}
