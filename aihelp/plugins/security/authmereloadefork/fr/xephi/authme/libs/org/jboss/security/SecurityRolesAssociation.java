package fr.xephi.authme.libs.org.jboss.security;

import java.util.Map;
import java.util.Set;

public final class SecurityRolesAssociation {
   private static ThreadLocal<Map<String, Set<String>>> threadSecurityRoleMapping = new ThreadLocal();

   public static Map<String, Set<String>> getSecurityRoles() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityRolesAssociation.class.getName() + ".getSecurityRoles"));
      }

      return (Map)threadSecurityRoleMapping.get();
   }

   public static void setSecurityRoles(Map<String, Set<String>> securityRoles) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityRolesAssociation.class.getName() + ".setSecurityRoles"));
      }

      PicketBoxLogger.LOGGER.traceSecRolesAssociationSetSecurityRoles(securityRoles);
      if (securityRoles == null) {
         threadSecurityRoleMapping.remove();
      } else {
         threadSecurityRoleMapping.set(securityRoles);
      }

   }
}
