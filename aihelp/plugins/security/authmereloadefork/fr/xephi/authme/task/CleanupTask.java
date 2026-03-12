package fr.xephi.authme.task;

import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.libs.ch.jalu.injector.factory.SingletonStore;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.UniversalRunnable;
import fr.xephi.authme.libs.javax.inject.Inject;

public class CleanupTask extends UniversalRunnable {
   @Inject
   private SingletonStore<HasCleanup> hasCleanupStore;

   CleanupTask() {
   }

   public void run() {
      this.hasCleanupStore.retrieveAllOfType().forEach(HasCleanup::performCleanup);
   }
}
