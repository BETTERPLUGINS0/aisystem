package fr.xephi.authme.libs.org.jboss.security.util;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.util.Iterator;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

public class MBeanServerLocator {
   private static MBeanServer instance = null;

   private MBeanServerLocator() {
   }

   public static void setJBoss(MBeanServer server) {
      Class var1 = MBeanServerLocator.class;
      synchronized(MBeanServerLocator.class) {
         instance = server;
      }
   }

   public static MBeanServer locate(String agentID) {
      MBeanServer server = (MBeanServer)MBeanServerFactory.findMBeanServer(agentID).iterator().next();
      return server;
   }

   public static MBeanServer locate() {
      return locate((String)null);
   }

   public static MBeanServer locateJBoss() {
      Class var0 = MBeanServerLocator.class;
      synchronized(MBeanServerLocator.class) {
         if (instance != null) {
            return instance;
         }
      }

      Iterator i = MBeanServerFactory.findMBeanServer((String)null).iterator();

      MBeanServer server;
      String domain;
      do {
         do {
            if (!i.hasNext()) {
               throw PicketBoxMessages.MESSAGES.unableToLocateMBeanServer();
            }

            server = (MBeanServer)i.next();
            domain = server.getDefaultDomain();
         } while(domain == null);
      } while(!domain.equals("jboss") && !domain.equals("DefaultDomain"));

      return server;
   }
}
