package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.GuardedBy;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class ExecutionList {
   private static final Logger log = Logger.getLogger(ExecutionList.class.getName());
   @CheckForNull
   @GuardedBy("this")
   private ExecutionList.RunnableExecutorPair runnables;
   @GuardedBy("this")
   private boolean executed;

   public void add(Runnable runnable, Executor executor) {
      Preconditions.checkNotNull(runnable, "Runnable was null.");
      Preconditions.checkNotNull(executor, "Executor was null.");
      synchronized(this) {
         if (!this.executed) {
            this.runnables = new ExecutionList.RunnableExecutorPair(runnable, executor, this.runnables);
            return;
         }
      }

      executeListener(runnable, executor);
   }

   public void execute() {
      ExecutionList.RunnableExecutorPair list;
      synchronized(this) {
         if (this.executed) {
            return;
         }

         this.executed = true;
         list = this.runnables;
         this.runnables = null;
      }

      ExecutionList.RunnableExecutorPair reversedList;
      ExecutionList.RunnableExecutorPair tmp;
      for(reversedList = null; list != null; reversedList = tmp) {
         tmp = list;
         list = list.next;
         tmp.next = reversedList;
      }

      while(reversedList != null) {
         executeListener(reversedList.runnable, reversedList.executor);
         reversedList = reversedList.next;
      }

   }

   private static void executeListener(Runnable runnable, Executor executor) {
      try {
         executor.execute(runnable);
      } catch (RuntimeException var5) {
         Logger var10000 = log;
         Level var10001 = Level.SEVERE;
         String var3 = String.valueOf(runnable);
         String var4 = String.valueOf(executor);
         var10000.log(var10001, (new StringBuilder(57 + String.valueOf(var3).length() + String.valueOf(var4).length())).append("RuntimeException while executing runnable ").append(var3).append(" with executor ").append(var4).toString(), var5);
      }

   }

   private static final class RunnableExecutorPair {
      final Runnable runnable;
      final Executor executor;
      @CheckForNull
      ExecutionList.RunnableExecutorPair next;

      RunnableExecutorPair(Runnable runnable, Executor executor, @CheckForNull ExecutionList.RunnableExecutorPair next) {
         this.runnable = runnable;
         this.executor = executor;
         this.next = next;
      }
   }
}
