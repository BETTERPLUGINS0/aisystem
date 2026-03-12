package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.concurrent.Executor;

@ElementTypesAreNonnullByDefault
@GwtCompatible
enum DirectExecutor implements Executor {
   INSTANCE;

   public void execute(Runnable command) {
      command.run();
   }

   public String toString() {
      return "MoreExecutors.directExecutor()";
   }

   // $FF: synthetic method
   private static DirectExecutor[] $values() {
      return new DirectExecutor[]{INSTANCE};
   }
}
