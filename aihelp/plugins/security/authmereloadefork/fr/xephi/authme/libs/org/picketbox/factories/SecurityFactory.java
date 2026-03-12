package fr.xephi.authme.libs.org.picketbox.factories;

import fr.xephi.authme.libs.org.jboss.security.AuthenticationManager;
import fr.xephi.authme.libs.org.jboss.security.AuthorizationManager;
import fr.xephi.authme.libs.org.jboss.security.ISecurityManagement;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextFactory;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditManager;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.config.StandaloneConfiguration;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingManager;
import fr.xephi.authme.libs.org.picketbox.plugins.PicketBoxSecurityManagement;
import java.net.URL;
import javax.security.auth.login.Configuration;

public class SecurityFactory {
   private static ISecurityManagement securityManagement = new PicketBoxSecurityManagement();
   private static Configuration parentConfiguration = null;
   private static String AUTH_CONF_FILE = "auth.conf";
   private static String AUTH_CONF_SYSPROP = "java.security.auth.login.config";
   private static StandaloneConfiguration standaloneConfiguration;

   public static AuthenticationManager getAuthenticationManager(String securityDomain) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".getAuthenticationManager"));
      }

      validate();
      return securityManagement.getAuthenticationManager(securityDomain);
   }

   public static AuthorizationManager getAuthorizationManager(String securityDomain) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".getAuthorizationManager"));
      }

      validate();
      return securityManagement.getAuthorizationManager(securityDomain);
   }

   public static AuditManager getAuditManager(String securityDomain) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".getAuditManager"));
      }

      validate();
      return securityManagement.getAuditManager(securityDomain);
   }

   public static MappingManager getMappingManager(String securityDomain) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".getMappingManager"));
      }

      validate();
      return securityManagement.getMappingManager(securityDomain);
   }

   public static ISecurityManagement getSecurityManagement() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".getSecurityManagement"));
      }

      return securityManagement;
   }

   public static void setSecurityManagement(ISecurityManagement iSecurityManagement) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".setSecurityManagement"));
      }

      securityManagement = iSecurityManagement;
   }

   public static void prepare() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".prepare"));
      }

      if (!(Configuration.getConfiguration() instanceof ApplicationPolicyRegistration)) {
         standaloneConfiguration.setParentConfig(parentConfiguration);
         Configuration.setConfiguration(standaloneConfiguration);
      }

   }

   public static SecurityContext establishSecurityContext(String securityDomainName) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".establishSecurityContext"));
      }

      SecurityContext securityContext = null;

      try {
         securityContext = SecurityContextFactory.createSecurityContext(securityDomainName);
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }

      SecurityActions.setSecurityContext(securityContext);
      return securityContext;
   }

   public static void release() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".release"));
      }

      Configuration config = Configuration.getConfiguration();
      if (config == standaloneConfiguration) {
         Configuration.setConfiguration(parentConfiguration);
      }

   }

   private static void validate() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".validate"));
      }

      assert securityManagement != null;

   }

   static {
      try {
         ClassLoader tcl = SecurityActions.getContextClassLoader();
         if (tcl == null) {
            throw PicketBoxMessages.MESSAGES.invalidThreadContextClassLoader();
         }

         URL configLocation = tcl.getResource(AUTH_CONF_FILE);
         if (SecurityActions.getSystemProperty(AUTH_CONF_SYSPROP, (String)null) == null) {
            if (configLocation == null) {
               throw PicketBoxMessages.MESSAGES.invalidNullLoginConfig();
            }

            SecurityActions.setSystemProperty(AUTH_CONF_SYSPROP, configLocation.toExternalForm());
         }

         parentConfiguration = Configuration.getConfiguration();
      } catch (Exception var2) {
         throw PicketBoxMessages.MESSAGES.unableToInitSecurityFactory(var2);
      }

      standaloneConfiguration = StandaloneConfiguration.getInstance();
   }
}
