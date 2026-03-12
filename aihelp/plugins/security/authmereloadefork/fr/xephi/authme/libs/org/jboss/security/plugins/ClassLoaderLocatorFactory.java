package fr.xephi.authme.libs.org.jboss.security.plugins;

public class ClassLoaderLocatorFactory {
   private static ClassLoaderLocator theLocator = null;

   public static void set(ClassLoaderLocator cl) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(ClassLoaderLocatorFactory.class.getName() + ".set"));
      }

      theLocator = cl;
   }

   public static ClassLoaderLocator get() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(ClassLoaderLocatorFactory.class.getName() + ".get"));
      }

      return theLocator;
   }
}
