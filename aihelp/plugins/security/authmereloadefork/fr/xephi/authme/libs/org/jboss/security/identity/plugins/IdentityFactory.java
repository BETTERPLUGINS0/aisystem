package fr.xephi.authme.libs.org.jboss.security.identity.plugins;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import java.lang.reflect.Constructor;
import java.security.Principal;
import java.security.acl.Group;

public class IdentityFactory {
   public static final String IDENTITY_CLASS = "fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleIdentity";
   public static final String PRINCIPAL_CLASS = "fr.xephi.authme.libs.org.jboss.security.SimplePrincipal";
   public static final String GROUP_CLASS = "fr.xephi.authme.libs.org.jboss.security.SimpleGroup";

   public static Principal createPrincipal(String name) throws Exception {
      return (Principal)loadClass("fr.xephi.authme.libs.org.jboss.security.SimplePrincipal", name);
   }

   public static Group createGroup(String name) throws Exception {
      return (Group)loadClass("fr.xephi.authme.libs.org.jboss.security.SimpleGroup", name);
   }

   public static Identity createIdentity(String name) throws Exception {
      return (Identity)loadClass("fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleIdentity", name);
   }

   public static Identity createIdentity(String identityClass, String name) throws Exception {
      return (Identity)loadClass(identityClass, name);
   }

   public static Identity createIdentityWithRole(String name, String roleName) throws Exception {
      return (Identity)loadClass("fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleIdentity", name, roleName);
   }

   public static Identity createIdentityWithRole(String identityClass, String name, String roleName) throws Exception {
      return (Identity)loadClass(identityClass, name, roleName);
   }

   public static Identity createIdentityWithRole(String name, Role role) throws Exception {
      return (Identity)loadClass("fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleIdentity", name, role);
   }

   public static Identity createIdentityWithRole(String identityClass, String name, Role role) throws Exception {
      return (Identity)loadClass(identityClass, name, role);
   }

   private static Object loadClass(String className, String ctorArg) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(IdentityFactory.class.getName() + ".loadClass"));
      }

      Class<?> clazz = SecurityActions.getClass(className);
      Constructor<?> ctr = clazz.getConstructor(String.class);
      return ctr.newInstance(ctorArg);
   }

   private static Object loadClass(String className, String ctorArg1, String ctorArg2) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(IdentityFactory.class.getName() + ".loadClass"));
      }

      Class<?> clazz = SecurityActions.getClass(className);
      Constructor<?> ctr = clazz.getConstructor(String.class, String.class);
      return ctr.newInstance(ctorArg1, ctorArg2);
   }

   private static Object loadClass(String className, String ctorArg1, Role ctorArg2) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(IdentityFactory.class.getName() + ".loadClass"));
      }

      Class<?> clazz = SecurityActions.getClass(className);
      Constructor<?> ctr = clazz.getConstructor(String.class, Role.class);
      return ctr.newInstance(ctorArg1, ctorArg2);
   }
}
