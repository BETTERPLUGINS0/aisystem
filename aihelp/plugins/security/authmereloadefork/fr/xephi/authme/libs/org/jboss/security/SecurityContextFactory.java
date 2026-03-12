package fr.xephi.authme.libs.org.jboss.security;

import java.lang.reflect.Constructor;
import java.security.Principal;
import javax.security.auth.Subject;

public class SecurityContextFactory {
   private static final Class[] CONTEXT_CONSTRUCTOR_TYPES = new Class[]{String.class};
   private static final Class[] CONTEXT_UTIL_CONSTRUCTOR_TYPES = new Class[]{SecurityContext.class};
   private static String defaultFQN = "fr.xephi.authme.libs.org.jboss.security.plugins.JBossSecurityContext";
   private static String defaultUtilClassFQN = "fr.xephi.authme.libs.org.jboss.security.plugins.JBossSecurityContextUtil";
   private static Class<? extends SecurityContext> defaultSecurityContextClass = null;
   private static Constructor<SecurityContext> defaultSecurityContextConstructor = null;
   private static Class<? extends SecurityContextUtil> defaultUtilClass = null;
   private static Constructor<SecurityContextUtil> defaultUtilConstructor = null;

   public static SecurityContext createSecurityContext(String securityDomain) throws Exception {
      if (defaultSecurityContextConstructor != null) {
         return createSecurityContext(securityDomain, defaultSecurityContextConstructor);
      } else {
         return defaultSecurityContextClass != null ? createSecurityContext(securityDomain, defaultSecurityContextClass) : createSecurityContext(securityDomain, defaultFQN, SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
      }
   }

   public static SecurityContext createSecurityContext(String securityDomain, ClassLoader classLoader) throws Exception {
      if (defaultSecurityContextConstructor != null) {
         return createSecurityContext(securityDomain, defaultSecurityContextConstructor);
      } else {
         return defaultSecurityContextClass != null ? createSecurityContext(securityDomain, defaultSecurityContextClass) : createSecurityContext(securityDomain, defaultFQN, classLoader);
      }
   }

   public static SecurityContext createSecurityContext(String securityDomain, String fqnClass) throws Exception {
      return createSecurityContext(securityDomain, fqnClass, SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
   }

   public static SecurityContext createSecurityContext(String securityDomain, String fqnClass, ClassLoader classLoader) throws Exception {
      if (securityDomain == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security domain");
      } else if (fqnClass == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("fqnClass");
      } else {
         defaultSecurityContextClass = getContextClass(fqnClass, classLoader);
         defaultSecurityContextConstructor = defaultSecurityContextClass.getConstructor(CONTEXT_CONSTRUCTOR_TYPES);
         return createSecurityContext(securityDomain, defaultSecurityContextConstructor);
      }
   }

   public static SecurityContext createSecurityContext(String securityDomain, Class<? extends SecurityContext> clazz) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createSecurityContext"));
      }

      if (securityDomain == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security domain");
      } else if (clazz == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("clazz");
      } else {
         Constructor<? extends SecurityContext> ctr = clazz.getConstructor(CONTEXT_CONSTRUCTOR_TYPES);
         return (SecurityContext)ctr.newInstance(securityDomain);
      }
   }

   private static SecurityContext createSecurityContext(String securityDomain, Constructor<SecurityContext> constructor) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createSecurityContext"));
      }

      if (securityDomain == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security domain");
      } else if (constructor == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("constructor");
      } else {
         return (SecurityContext)constructor.newInstance(securityDomain);
      }
   }

   public static SecurityContext createSecurityContext(Principal p, Object cred, Subject s, String securityDomain) throws Exception {
      return createSecurityContext(p, cred, s, securityDomain, SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
   }

   public static SecurityContext createSecurityContext(Principal p, Object cred, Subject s, String securityDomain, ClassLoader classLoader) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createSecurityContext"));
      }

      SecurityContext jsc = createSecurityContext(securityDomain, classLoader);
      jsc.getUtil().createSubjectInfo(p, cred, s);
      return jsc;
   }

   public static SecurityContext createSecurityContext(Principal p, Object cred, Subject s, String securityDomain, String fqnClass, ClassLoader classLoader) throws Exception {
      SecurityContext sc = createSecurityContext(securityDomain, fqnClass, classLoader);
      sc.getUtil().createSubjectInfo(p, cred, s);
      return sc;
   }

   public static SecurityContextUtil createUtil(SecurityContext sc) throws Exception {
      return createUtil(sc, SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
   }

   public static SecurityContextUtil createUtil(SecurityContext sc, ClassLoader classLoader) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createUtil"));
      }

      Constructor<SecurityContextUtil> ctr = defaultUtilConstructor;
      if (ctr == null) {
         Class<? extends SecurityContextUtil> clazz = loadClass(defaultUtilClassFQN, classLoader);
         defaultUtilClass = clazz;
         ctr = defaultUtilConstructor = clazz.getConstructor(CONTEXT_UTIL_CONSTRUCTOR_TYPES);
      }

      return (SecurityContextUtil)ctr.newInstance(sc);
   }

   public static SecurityContextUtil createUtil(SecurityContext sc, String utilFQN) throws Exception {
      return createUtil(sc, utilFQN, SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
   }

   public static SecurityContextUtil createUtil(SecurityContext sc, String utilFQN, ClassLoader classLoader) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createUtil"));
      }

      Class<?> clazz = loadClass(utilFQN, classLoader);
      Constructor<? extends SecurityContextUtil> ctr = clazz.getConstructor(CONTEXT_UTIL_CONSTRUCTOR_TYPES);
      return (SecurityContextUtil)ctr.newInstance(sc);
   }

   public static SecurityContextUtil createUtil(SecurityContext sc, Class<? extends SecurityContextUtil> utilClazz) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createUtil"));
      }

      Constructor<? extends SecurityContextUtil> ctr = utilClazz.getConstructor(SecurityContext.class);
      return (SecurityContextUtil)ctr.newInstance(sc);
   }

   public static void setDefaultSecurityContextFQN(String fqn) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".setDefaultSecurityContextFQN"));
      }

      defaultFQN = fqn;
      defaultSecurityContextClass = null;
      defaultSecurityContextConstructor = null;
   }

   public static void setDefaultSecurityContextUtilFQN(String fqn) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".setDefaultSecurityContextUtilFQN"));
      }

      defaultUtilClassFQN = fqn;
      defaultUtilClass = null;
      defaultUtilConstructor = null;
   }

   private static Class<?> loadClass(String fqn, ClassLoader classLoader) throws Exception {
      try {
         return classLoader.loadClass(fqn);
      } catch (Exception var4) {
         ClassLoader tcl = SecuritySPIActions.getContextClassLoader();
         return tcl.loadClass(fqn);
      }
   }

   private static Class<SecurityContext> getContextClass(String className, ClassLoader classLoader) throws Exception {
      try {
         return classLoader.loadClass(className);
      } catch (Exception var4) {
         ClassLoader tcl = SecuritySPIActions.getContextClassLoader();
         return tcl.loadClass(className);
      }
   }

   static {
      try {
         defaultSecurityContextClass = SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class).loadClass(defaultFQN);
      } catch (Exception var6) {
         try {
            defaultSecurityContextClass = SecuritySPIActions.getContextClassLoader().loadClass(defaultFQN);
         } catch (Exception var5) {
         }
      }

      try {
         defaultUtilClass = SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class).loadClass(defaultUtilClassFQN);
      } catch (Exception var4) {
         try {
            defaultUtilClass = SecuritySPIActions.getContextClassLoader().loadClass(defaultUtilClassFQN);
         } catch (Exception var3) {
         }
      }

      if (defaultSecurityContextClass != null) {
         try {
            defaultSecurityContextConstructor = defaultSecurityContextClass.getConstructor(CONTEXT_CONSTRUCTOR_TYPES);
         } catch (Exception var2) {
         }
      }

   }
}
