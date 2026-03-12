package fr.xephi.authme.libs.org.jboss.security.factories;

import fr.xephi.authme.libs.org.jboss.security.AuthorizationManager;
import java.lang.reflect.Constructor;

public class AuthorizationManagerFactory {
   private static String fqn = "fr.xephi.authme.libs.org.jboss.security.plugins.JBossAuthorizationManager";

   public static AuthorizationManager getAuthorizationManager(String securityDomain) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(AuthorizationManagerFactory.class.getName() + ".getAuthorizationManager"));
      }

      Class clazz = SecurityActions.loadClass(fqn);
      Constructor ctr = clazz.getConstructor(String.class);
      return (AuthorizationManager)ctr.newInstance(securityDomain);
   }

   public static void setFQN(String name) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(AuthorizationManagerFactory.class.getName() + ".setFQN"));
      }

      fqn = name;
   }
}
