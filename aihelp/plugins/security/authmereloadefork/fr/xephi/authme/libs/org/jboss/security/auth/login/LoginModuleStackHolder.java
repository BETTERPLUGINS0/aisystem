package fr.xephi.authme.libs.org.jboss.security.auth.login;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.AuthPermission;
import javax.security.auth.login.AppConfigurationEntry;

public class LoginModuleStackHolder {
   public static final AuthPermission GET_CONFIG_ENTRY_PERM = new AuthPermission("getLoginConfiguration");
   public static final AuthPermission SET_CONFIG_ENTRY_PERM = new AuthPermission("setLoginConfiguration");
   private String name = "";
   private ArrayList appEntries;

   public LoginModuleStackHolder(String name, List entries) {
      this.name = name;
      if (entries != null) {
         this.appEntries = new ArrayList();
         this.appEntries.addAll(entries);
      }

   }

   public String getName() {
      return this.name;
   }

   public void addAppConfigurationEntry(AppConfigurationEntry entry) {
      if (this.appEntries == null) {
         this.appEntries = new ArrayList();
      }

      this.appEntries.add(entry);
   }

   public AppConfigurationEntry[] getAppConfigurationEntry() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      }

      AppConfigurationEntry[] entries = new AppConfigurationEntry[this.appEntries.size()];
      this.appEntries.toArray(entries);
      return entries;
   }

   public void setAppConfigurationEntry(List entries) {
      if (entries == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("entries");
      } else {
         if (this.appEntries == null) {
            this.appEntries = new ArrayList();
         }

         this.appEntries.addAll(entries);
      }
   }
}
