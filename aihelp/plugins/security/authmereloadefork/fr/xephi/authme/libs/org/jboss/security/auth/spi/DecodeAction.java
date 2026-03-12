package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.util.MBeanServerLocator;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.management.MBeanServer;
import javax.management.ObjectName;

class DecodeAction implements PrivilegedExceptionAction<Object> {
   private static final RuntimePermission decodePermission = new RuntimePermission("fr.xephi.authme.libs.org.jboss.security.auth.spi.DecodeAction.decode");
   String password;
   ObjectName serviceName;

   DecodeAction(String password, ObjectName serviceName) {
      this.password = password;
      this.serviceName = serviceName;
   }

   public Object run() throws Exception {
      byte[] secret = this.decode64(this.password);
      String secretPassword = new String(secret, "UTF-8");
      return secretPassword.toCharArray();
   }

   private byte[] decode64(String secret) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(decodePermission);
      }

      MBeanServer server = MBeanServerLocator.locateJBoss();
      return (byte[])((byte[])server.invoke(this.serviceName, "decode64", new Object[]{secret}, new String[]{String.class.getName()}));
   }

   static char[] decode(String password, ObjectName serviceName) throws Exception {
      DecodeAction action = new DecodeAction(password, serviceName);

      try {
         char[] decode = (char[])((char[])AccessController.doPrivileged(action));
         return decode;
      } catch (PrivilegedActionException var4) {
         throw var4.getException();
      }
   }
}
