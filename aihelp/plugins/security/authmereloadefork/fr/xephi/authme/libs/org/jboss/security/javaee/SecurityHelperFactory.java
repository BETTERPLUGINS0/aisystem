package fr.xephi.authme.libs.org.jboss.security.javaee;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;

public class SecurityHelperFactory {
   private static String WebAuthorizationHelperClass = "fr.xephi.authme.libs.org.jboss.security.plugins.javaee.WebAuthorizationHelper";
   private static String EjbAuthorizationHelperClass = "fr.xephi.authme.libs.org.jboss.security.plugins.javaee.EJBAuthorizationHelper";
   private static Class<?> webAuthorizationHelperClazz;
   private static Class<?> ejbAuthorizationHelperClazz;

   public static EJBAuthenticationHelper getEJBAuthenticationHelper(SecurityContext sc) {
      return new EJBAuthenticationHelper(sc);
   }

   public static AbstractWebAuthorizationHelper getWebAuthorizationHelper(SecurityContext sc) throws Exception {
      if (webAuthorizationHelperClazz == null) {
         webAuthorizationHelperClazz = SecurityActions.loadClass(WebAuthorizationHelperClass);
      }

      AbstractWebAuthorizationHelper awh = (AbstractWebAuthorizationHelper)webAuthorizationHelperClazz.newInstance();
      awh.setSecurityContext(sc);
      return awh;
   }

   public static AbstractEJBAuthorizationHelper getEJBAuthorizationHelper(SecurityContext sc) throws Exception {
      if (ejbAuthorizationHelperClazz == null) {
         ejbAuthorizationHelperClazz = SecurityActions.loadClass(EjbAuthorizationHelperClass);
      }

      AbstractEJBAuthorizationHelper awh = (AbstractEJBAuthorizationHelper)ejbAuthorizationHelperClazz.newInstance();
      awh.setSecurityContext(sc);
      return awh;
   }

   public static void setEJBAuthorizationHelperClass(String fqn) {
      EjbAuthorizationHelperClass = fqn;
      ejbAuthorizationHelperClazz = null;
   }

   public static void setWebAuthorizationHelperClass(String fqn) {
      WebAuthorizationHelperClass = fqn;
      webAuthorizationHelperClazz = null;
   }
}
