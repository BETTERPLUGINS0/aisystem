package fr.xephi.authme.libs.org.postgresql.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

public class KerberosTicket {
   private static final String CONFIG_ITEM_NAME = "ticketCache";
   private static final String KRBLOGIN_MODULE = "com.sun.security.auth.module.Krb5LoginModule";

   public static boolean credentialCacheExists(Properties info) {
      LoginContext lc = null;
      Configuration existingConfiguration = Configuration.getConfiguration();
      Configuration.setConfiguration(new KerberosTicket.CustomKrbConfig());

      try {
         lc = new LoginContext("ticketCache", new CallbackHandler() {
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
               throw new RuntimeException("This is an error, you should set doNotPrompt to false in jaas.config");
            }
         });
         lc.login();
      } catch (LoginException var4) {
         if (existingConfiguration != null) {
            Configuration.setConfiguration(existingConfiguration);
         }

         return false;
      }

      if (existingConfiguration != null) {
         Configuration.setConfiguration(existingConfiguration);
      }

      Subject sub = lc.getSubject();
      return sub != null;
   }

   static class CustomKrbConfig extends Configuration {
      public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
         if ("ticketCache".equals(name)) {
            Map<String, String> options = new HashMap();
            options.put("refreshKrb5Config", Boolean.FALSE.toString());
            options.put("useTicketCache", Boolean.TRUE.toString());
            options.put("doNotPrompt", Boolean.TRUE.toString());
            options.put("useKeyTab", Boolean.TRUE.toString());
            options.put("isInitiator", Boolean.FALSE.toString());
            options.put("renewTGT", Boolean.FALSE.toString());
            options.put("debug", Boolean.FALSE.toString());
            return new AppConfigurationEntry[]{new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule", LoginModuleControlFlag.REQUIRED, options)};
         } else {
            return null;
         }
      }
   }
}
