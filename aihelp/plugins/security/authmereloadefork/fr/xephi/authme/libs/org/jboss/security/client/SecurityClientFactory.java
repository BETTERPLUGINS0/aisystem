package fr.xephi.authme.libs.org.jboss.security.client;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

public class SecurityClientFactory {
   private static String defaultClient = "fr.xephi.authme.libs.org.jboss.security.client.JBossSecurityClient";

   public static SecurityClient getSecurityClient() throws Exception {
      return getSC(defaultClient);
   }

   public static SecurityClient getSecurityClient(String client) throws Exception {
      return getSC(client);
   }

   public static SecurityClient getSecurityClient(Class<?> clazz) throws Exception {
      if (!SecurityClient.class.isAssignableFrom(clazz)) {
         throw PicketBoxMessages.MESSAGES.invalidType(SecurityClient.class.getName());
      } else {
         Constructor<?> ctr = clazz.getConstructor();
         return (SecurityClient)ctr.newInstance();
      }
   }

   private static SecurityClient getSC(final String client) throws PrivilegedActionException {
      return (SecurityClient)AccessController.doPrivileged(new PrivilegedExceptionAction() {
         public Object run() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(client);
            return clazz.newInstance();
         }
      });
   }
}
