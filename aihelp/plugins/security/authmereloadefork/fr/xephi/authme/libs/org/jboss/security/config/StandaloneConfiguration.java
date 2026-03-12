package fr.xephi.authme.libs.org.jboss.security.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.auth.login.AuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.auth.login.BaseAuthenticationInfo;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

public class StandaloneConfiguration extends Configuration implements ApplicationPolicyRegistration {
   protected Configuration parentConfig;
   protected ConcurrentMap<String, ApplicationPolicy> appPolicyMap = new ConcurrentHashMap();
   protected static StandaloneConfiguration _instance;

   protected StandaloneConfiguration() {
   }

   public static StandaloneConfiguration getInstance() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(StandaloneConfiguration.class.getName() + ".getInstance"));
      }

      if (_instance == null) {
         _instance = new StandaloneConfiguration();
      }

      return _instance;
   }

   public void addApplicationPolicy(String appName, ApplicationPolicy aPolicy) {
      this.appPolicyMap.put(appName, aPolicy);
      SecurityConfiguration.addApplicationPolicy(aPolicy);
   }

   public ApplicationPolicy getApplicationPolicy(String domainName) {
      return (ApplicationPolicy)this.appPolicyMap.get(domainName);
   }

   public boolean removeApplicationPolicy(String domainName) {
      ApplicationPolicy ap = (ApplicationPolicy)this.appPolicyMap.remove(domainName);
      return ap != null;
   }

   public void setParentConfig(Configuration parentConfig) {
      this.parentConfig = parentConfig;
   }

   public AppConfigurationEntry[] getAppConfigurationEntry(String appName) {
      AppConfigurationEntry[] entry = null;
      ApplicationPolicy aPolicy = this.getApplicationPolicy(appName);
      final BaseAuthenticationInfo authInfo = null;
      if (aPolicy != null) {
         authInfo = aPolicy.getAuthenticationInfo();
      }

      if (authInfo == null) {
         PicketBoxLogger.LOGGER.traceGetAppConfigEntryViaParent(appName, this.parentConfig != null ? this.parentConfig.toString() : null);
         if (this.parentConfig != null) {
            entry = this.parentConfig.getAppConfigurationEntry(appName);
         }

         if (entry == null) {
            PicketBoxLogger.LOGGER.traceGetAppConfigEntryViaDefault(appName, "other");
         }

         ApplicationPolicy defPolicy = this.getApplicationPolicy("other");
         authInfo = defPolicy != null ? (AuthenticationInfo)defPolicy.getAuthenticationInfo() : null;
      }

      if (authInfo != null) {
         PicketBoxLogger.LOGGER.traceEndGetAppConfigEntryWithSuccess(appName, authInfo.toString());
         PrivilegedAction<AppConfigurationEntry[]> action = new PrivilegedAction<AppConfigurationEntry[]>() {
            public AppConfigurationEntry[] run() {
               return ((BaseAuthenticationInfo)authInfo).copyAppConfigurationEntry();
            }
         };
         entry = (AppConfigurationEntry[])AccessController.doPrivileged(action);
      } else {
         PicketBoxLogger.LOGGER.traceEndGetAppConfigEntryWithFailure(appName);
      }

      return entry;
   }
}
