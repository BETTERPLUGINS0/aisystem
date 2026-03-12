package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;

public class HostThreadLocal {
   private static ThreadLocal<String> host = new ThreadLocal();

   public static String get() {
      String hostName = (String)host.get();
      PicketBoxLogger.LOGGER.traceHostThreadLocalGet(hostName, Thread.currentThread().getId());
      return hostName;
   }

   public static void set(String hostVal) {
      PicketBoxLogger.LOGGER.traceHostThreadLocalSet(hostVal, Thread.currentThread().getId());
      host.set(hostVal);
   }
}
