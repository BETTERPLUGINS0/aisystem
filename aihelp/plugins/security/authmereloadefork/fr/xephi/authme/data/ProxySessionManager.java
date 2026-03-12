package fr.xephi.authme.data;

import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.util.expiring.ExpiringSet;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ProxySessionManager implements HasCleanup {
   private final ExpiringSet<String> activeProxySessions;

   @Inject
   public ProxySessionManager() {
      long countTimeout = 5L;
      this.activeProxySessions = new ExpiringSet(countTimeout, TimeUnit.SECONDS);
   }

   private void setActiveSession(String name) {
      this.activeProxySessions.add(name.toLowerCase(Locale.ROOT));
   }

   public void processProxySessionMessage(String name) {
      this.setActiveSession(name);
   }

   public boolean shouldResumeSession(String name) {
      return this.activeProxySessions.contains(name);
   }

   public void performCleanup() {
      this.activeProxySessions.removeExpiredEntries();
   }
}
