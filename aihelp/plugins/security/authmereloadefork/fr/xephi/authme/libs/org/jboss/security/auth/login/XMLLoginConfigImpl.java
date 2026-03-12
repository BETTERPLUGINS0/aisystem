package fr.xephi.authme.libs.org.jboss.security.auth.login;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.config.SecurityConfiguration;
import fr.xephi.authme.libs.org.jboss.security.config.parser.StaxBasedConfigParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import javax.security.auth.AuthPermission;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

public class XMLLoginConfigImpl extends Configuration implements Serializable, ApplicationPolicyRegistration {
   private static final long serialVersionUID = -8965860493224188277L;
   private static final String DEFAULT_APP_CONFIG_NAME = "other";
   private static final AuthPermission REFRESH_PERM = new AuthPermission("refreshLoginConfiguration");
   transient fr.xephi.authme.libs.org.jboss.security.config.PolicyConfig appConfigs = new fr.xephi.authme.libs.org.jboss.security.config.PolicyConfig();
   protected URL loginConfigURL;
   protected Configuration parentConfig;
   private boolean validateDTD = true;
   private static final XMLLoginConfigImpl instance = new XMLLoginConfigImpl();

   private XMLLoginConfigImpl() {
   }

   public static XMLLoginConfigImpl getInstance() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(XMLLoginConfigImpl.class.getName() + ".getInstance"));
      }

      return instance;
   }

   public void refresh() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(REFRESH_PERM);
      }

      this.appConfigs.clear();
      this.loadConfig();
   }

   public AppConfigurationEntry[] getAppConfigurationEntry(String appName) {
      PicketBoxLogger.LOGGER.traceBeginGetAppConfigEntry(appName, this.appConfigs.size());
      if (this.appConfigs.size() == 0) {
         this.loadConfig();
      }

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
            ApplicationPolicy defPolicy = this.appConfigs.get("other");
            authInfo = defPolicy != null ? (AuthenticationInfo)defPolicy.getAuthenticationInfo() : null;
         }
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

   public URL getConfigURL() {
      return this.loginConfigURL;
   }

   public void setConfigURL(URL loginConfigURL) {
      this.loginConfigURL = loginConfigURL;
   }

   public void setConfigResource(String resourceName) throws IOException {
      ClassLoader tcl = SecurityActions.getContextClassLoader();
      this.loginConfigURL = tcl.getResource(resourceName);
      if (this.loginConfigURL == null) {
         throw PicketBoxMessages.MESSAGES.failedToFindResource(resourceName);
      }
   }

   public void setParentConfig(Configuration parentConfig) {
      this.parentConfig = parentConfig;
   }

   public boolean getValidateDTD() {
      return this.validateDTD;
   }

   public void setValidateDTD(boolean flag) {
      this.validateDTD = flag;
   }

   public void addApplicationPolicy(String appName, ApplicationPolicy aPolicy) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(REFRESH_PERM);
      }

      this.appConfigs.add(aPolicy);
      this.handleJASPIDelegation(aPolicy);
      SecurityConfiguration.addApplicationPolicy(aPolicy);
   }

   public void addAppConfig(String appName, AppConfigurationEntry[] entries) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(REFRESH_PERM);
      }

      AuthenticationInfo authInfo = new AuthenticationInfo(appName);
      authInfo.setAppConfigurationEntry(entries);
      PicketBoxLogger.LOGGER.traceAddAppConfig(appName, authInfo.toString());
      ApplicationPolicy aPolicy = new ApplicationPolicy(appName, authInfo);
      this.appConfigs.add(aPolicy);
      SecurityConfiguration.addApplicationPolicy(aPolicy);
   }

   public void copy(fr.xephi.authme.libs.org.jboss.security.config.PolicyConfig policyConfig) {
      this.appConfigs.copy(policyConfig);
   }

   /** @deprecated */
   @Deprecated
   public void removeAppConfig(String appName) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(REFRESH_PERM);
      }

      this.appConfigs.remove(appName);
      SecurityConfiguration.removeApplicationPolicy(appName);
   }

   public ApplicationPolicy getApplicationPolicy(String domainName) {
      if (this.appConfigs == null || this.appConfigs.size() == 0) {
         this.loadConfig();
      }

      ApplicationPolicy aPolicy = null;
      if (this.appConfigs != null) {
         aPolicy = this.appConfigs.get(domainName);
      }

      if (aPolicy != null) {
         SecurityConfiguration.addApplicationPolicy(aPolicy);
      }

      return aPolicy;
   }

   public boolean removeApplicationPolicy(String appName) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(REFRESH_PERM);
      }

      PicketBoxLogger.LOGGER.traceRemoveAppConfig(appName);
      this.appConfigs.remove(appName);
      SecurityConfiguration.removeApplicationPolicy(appName);
      return true;
   }

   public BaseAuthenticationInfo getAuthenticationInfo(String domainName) {
      ApplicationPolicy aPolicy = this.getApplicationPolicy(domainName);
      return aPolicy != null ? aPolicy.getAuthenticationInfo() : null;
   }

   public void clear() {
   }

   public void loadConfig() {
      String loginConfig = System.getProperty("java.security.auth.login.config");
      if (loginConfig == null) {
         loginConfig = "login-config.xml";
      }

      if (this.loginConfigURL == null) {
         try {
            this.loginConfigURL = new URL(loginConfig);
         } catch (MalformedURLException var9) {
            try {
               this.setConfigResource(loginConfig);
            } catch (IOException var8) {
               File configFile = new File(loginConfig);

               try {
                  this.setConfigURL(configFile.toURL());
               } catch (MalformedURLException var7) {
               }
            }
         }
      }

      if (this.loginConfigURL == null) {
         PicketBoxLogger.LOGGER.warnFailureToFindConfig(loginConfig);
      } else {
         PicketBoxLogger.LOGGER.traceBeginLoadConfig(this.loginConfigURL);

         try {
            this.loadConfig(this.loginConfigURL);
            PicketBoxLogger.LOGGER.traceEndLoadConfigWithSuccess(this.loginConfigURL);
         } catch (Exception var6) {
            PicketBoxLogger.LOGGER.warnEndLoadConfigWithFailure(this.loginConfigURL, var6);
         }

      }
   }

   protected String[] loadConfig(URL config) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(REFRESH_PERM);
      }

      ArrayList configNames = new ArrayList();
      PicketBoxLogger.LOGGER.debugLoadConfigAsXML(config);

      try {
         this.loadXMLConfig(config, configNames);
      } catch (Throwable var5) {
         PicketBoxLogger.LOGGER.debugLoadConfigAsSun(config, var5);
         this.loadSunConfig(config, configNames);
      }

      String[] names = new String[configNames.size()];
      configNames.toArray(names);
      return names;
   }

   private void handleJASPIDelegation(ApplicationPolicy aPolicy) {
      BaseAuthenticationInfo bai = aPolicy.getAuthenticationInfo();
      if (bai instanceof JASPIAuthenticationInfo) {
         JASPIAuthenticationInfo jai = (JASPIAuthenticationInfo)bai;
         LoginModuleStackHolder[] lmsharr = jai.getLoginModuleStackHolder();
         LoginModuleStackHolder[] arr$ = lmsharr;
         int len$ = lmsharr.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            LoginModuleStackHolder lmsh = arr$[i$];
            this.addAppConfig(lmsh.getName(), lmsh.getAppConfigurationEntry());
         }
      }

   }

   private void loadSunConfig(URL sunConfig, ArrayList configNames) throws Exception {
      InputStream is = null;
      InputStreamReader configFile = null;

      try {
         is = sunConfig.openStream();
         configFile = new InputStreamReader(is);
         SunConfigParser.doParse(configFile, this, PicketBoxLogger.LOGGER.isTraceEnabled());
      } finally {
         this.safeClose(configFile);
         this.safeClose(is);
      }

   }

   private void loadXMLConfig(URL loginConfigURL, ArrayList configNames) throws Exception {
      InputStream is = null;

      try {
         is = loginConfigURL.openStream();
         StaxBasedConfigParser parser = new StaxBasedConfigParser();
         parser.parse(is);
      } finally {
         this.safeClose(is);
      }

   }

   private void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }

   private void safeClose(InputStreamReader fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }
}
