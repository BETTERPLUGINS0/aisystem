package fr.xephi.authme.libs.org.jboss.security.auth.login;

import fr.xephi.authme.libs.org.jboss.security.config.BaseSecurityInfo;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.List;
import javax.security.auth.login.AppConfigurationEntry;

public class BaseAuthenticationInfo extends BaseSecurityInfo<Object> {
   public BaseAuthenticationInfo() {
   }

   public BaseAuthenticationInfo(String name) {
      super(name);
   }

   protected BaseSecurityInfo<Object> create(String name) {
      return new BaseAuthenticationInfo(name);
   }

   public AppConfigurationEntry[] getAppConfigurationEntry() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      }

      AppConfigurationEntry[] entries = new AppConfigurationEntry[super.moduleEntries.size()];
      super.moduleEntries.toArray(entries);
      return entries;
   }

   public AppConfigurationEntry[] copyAppConfigurationEntry() {
      return this.copyAppConfigurationEntry(super.moduleEntries);
   }

   protected AppConfigurationEntry[] copyAppConfigurationEntry(List<Object> entries) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      }

      AppConfigurationEntry[] copy = new AppConfigurationEntry[entries.size()];

      for(int i = 0; i < copy.length; ++i) {
         AppConfigurationEntry entry = (AppConfigurationEntry)entries.get(i);
         HashMap<String, Object> options = new HashMap(entry.getOptions());
         if (!this.disableSecurityDomainInOptions()) {
            options.put("jboss.security.security_domain", this.getName());
         }

         copy[i] = new AppConfigurationEntry(entry.getLoginModuleName(), entry.getControlFlag(), options);
      }

      return copy;
   }

   private boolean disableSecurityDomainInOptions() {
      String sysprop = (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
         public String run() {
            return System.getProperty("jboss.security.disable.secdomain.option");
         }
      });
      return "true".equalsIgnoreCase(sysprop);
   }
}
