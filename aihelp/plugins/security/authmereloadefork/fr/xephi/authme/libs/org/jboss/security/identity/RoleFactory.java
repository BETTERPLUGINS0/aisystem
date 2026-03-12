package fr.xephi.authme.libs.org.jboss.security.identity;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

public class RoleFactory {
   private static String SIMPLE_ROLE_CLASS = "fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole";
   private static String SIMPLE_ROLEGROUP_CLASS = "fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRoleGroup";

   public static Role createRole(final String name) throws PrivilegedActionException {
      return (Role)AccessController.doPrivileged(new PrivilegedExceptionAction<Role>() {
         public Role run() throws Exception {
            Class clazz;
            try {
               clazz = this.getClass().getClassLoader().loadClass(name);
            } catch (Exception var4) {
               ClassLoader tcl = Thread.currentThread().getContextClassLoader();
               clazz = tcl.loadClass(RoleFactory.SIMPLE_ROLE_CLASS);
            }

            Constructor<?> ctr = clazz.getConstructor(String.class);
            return (Role)ctr.newInstance(name);
         }
      });
   }

   public static RoleGroup createRoleGroup(final String name) throws PrivilegedActionException {
      return (RoleGroup)AccessController.doPrivileged(new PrivilegedExceptionAction<RoleGroup>() {
         public RoleGroup run() throws Exception {
            Class clazz;
            try {
               clazz = this.getClass().getClassLoader().loadClass(name);
            } catch (Exception var4) {
               ClassLoader tcl = Thread.currentThread().getContextClassLoader();
               clazz = tcl.loadClass(RoleFactory.SIMPLE_ROLEGROUP_CLASS);
            }

            Constructor<?> ctr = clazz.getConstructor(String.class);
            return (RoleGroup)ctr.newInstance(name);
         }
      });
   }

   public static void setSimpleRoleClass(String fqn) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(RoleFactory.class.getName() + ".setSimpleRoleClass"));
      }

      SIMPLE_ROLE_CLASS = fqn;
   }

   public static void setSimpleRoleGroupClass(String fqn) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(RoleFactory.class.getName() + ".setSimpleRoleGroupClass"));
      }

      SIMPLE_ROLEGROUP_CLASS = fqn;
   }
}
