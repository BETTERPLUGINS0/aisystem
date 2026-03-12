package fr.xephi.authme.libs.org.jboss.security.auth.message.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.auth.callback.JBossCallbackHandler;
import fr.xephi.authme.libs.org.jboss.security.auth.container.config.AuthModuleEntry;
import fr.xephi.authme.libs.org.jboss.security.auth.container.modules.DelegatingServerAuthModule;
import fr.xephi.authme.libs.org.jboss.security.auth.login.AuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.auth.login.BaseAuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.auth.login.JASPIAuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.SecurityConfiguration;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocator;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocatorFactory;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

public class JBossServerAuthConfig implements ServerAuthConfig {
   private String layer;
   private String contextId;
   private CallbackHandler callbackHandler = new JBossCallbackHandler();
   private List modules = new ArrayList();
   private Map contextProperties;

   public JBossServerAuthConfig(String layer, String appContext, CallbackHandler handler, Map properties) {
      this.layer = layer;
      this.contextId = appContext;
      this.callbackHandler = handler;
      this.contextProperties = properties;
   }

   public ServerAuthContext getAuthContext(String authContextID, Subject serviceSubject, Map properties) throws AuthException {
      List<ControlFlag> controlFlags = new ArrayList();
      Map<String, Map> mapOptionsByName = new HashMap();
      SecurityContext securityContext = SecurityActions.getSecurityContext();
      String secDomain = null;
      if (securityContext != null) {
         secDomain = securityContext.getSecurityDomain();
      } else {
         secDomain = (String)properties.get("security-domain");
         if (secDomain == null) {
            throw PicketBoxMessages.MESSAGES.failedToObtainSecDomainFromContextOrConfig();
         }
      }

      String defaultAppDomain = "other";
      ApplicationPolicy ap = SecurityConfiguration.getApplicationPolicy(secDomain);
      if (ap == null) {
         ap = SecurityConfiguration.getApplicationPolicy(defaultAppDomain);
      }

      if (ap == null) {
         throw PicketBoxMessages.MESSAGES.failedToObtainApplicationPolicy(secDomain);
      } else {
         BaseAuthenticationInfo bai = ap.getAuthenticationInfo();
         if (bai == null) {
            throw PicketBoxMessages.MESSAGES.failedToObtainAuthenticationInfo(secDomain);
         } else {
            if (bai instanceof AuthenticationInfo) {
               ServerAuthModule sam = new DelegatingServerAuthModule();
               Map options = new HashMap();
               options.put("javax.security.auth.login.LoginContext", secDomain);
               sam.initialize((MessagePolicy)null, (MessagePolicy)null, this.callbackHandler, options);
               this.modules.add(sam);
            } else {
               JASPIAuthenticationInfo jai = (JASPIAuthenticationInfo)bai;
               AuthModuleEntry[] amearr = jai.getAuthModuleEntry();
               ClassLoader moduleCL = null;
               String jbossModule = jai.getJBossModuleName();
               if (jbossModule != null && !jbossModule.isEmpty()) {
                  ClassLoaderLocator locator = ClassLoaderLocatorFactory.get();
                  if (locator != null) {
                     moduleCL = locator.get(jbossModule);
                  }
               }

               AuthModuleEntry[] arr$ = amearr;
               int len$ = amearr.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  AuthModuleEntry ame = arr$[i$];
                  ServerAuthModule sam;
                  HashMap options;
                  if (ame.getLoginModuleStackHolderName() != null) {
                     try {
                        mapOptionsByName.put(ame.getAuthModuleName(), ame.getOptions());
                        controlFlags.add(ame.getControlFlag());
                        sam = this.createSAM(moduleCL, ame.getAuthModuleName(), ame.getLoginModuleStackHolderName());
                        options = new HashMap();
                        sam.initialize((MessagePolicy)null, (MessagePolicy)null, this.callbackHandler, options);
                        this.modules.add(sam);
                     } catch (Exception var22) {
                        throw new AuthException(var22.getLocalizedMessage());
                     }
                  } else {
                     try {
                        mapOptionsByName.put(ame.getAuthModuleName(), ame.getOptions());
                        controlFlags.add(ame.getControlFlag());
                        sam = this.createSAM(moduleCL, ame.getAuthModuleName());
                        options = new HashMap();
                        sam.initialize((MessagePolicy)null, (MessagePolicy)null, this.callbackHandler, options);
                        this.modules.add(sam);
                     } catch (Exception var21) {
                        throw new AuthException(var21.getLocalizedMessage());
                     }
                  }
               }
            }

            JBossServerAuthContext serverAuthContext = new JBossServerAuthContext(this.modules, mapOptionsByName, this.callbackHandler);
            serverAuthContext.setControlFlags(controlFlags);
            return serverAuthContext;
         }
      }
   }

   public String getAppContext() {
      return this.contextId;
   }

   public String getMessageLayer() {
      return this.layer;
   }

   public void refresh() {
   }

   public List getServerAuthModules() {
      return this.modules;
   }

   public String getAuthContextID(MessageInfo messageInfo) {
      return this.contextId;
   }

   public boolean isProtected() {
      throw new UnsupportedOperationException();
   }

   private ServerAuthModule createSAM(ClassLoader moduleCL, String name) throws Exception {
      Class clazz = SecurityActions.loadClass(moduleCL, name);
      Constructor ctr = clazz.getConstructor();
      return (ServerAuthModule)ctr.newInstance();
   }

   private ServerAuthModule createSAM(ClassLoader moduleCL, String name, String lmshName) throws Exception {
      Class clazz = SecurityActions.loadClass(moduleCL, name);
      Constructor ctr = clazz.getConstructor(String.class);
      return (ServerAuthModule)ctr.newInstance(lmshName);
   }
}
