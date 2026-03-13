package com.volmit.iris.util.scheduling.jobs;

import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.concurrent.CompletableFuture;

public interface Job {
   String getName();

   void execute();

   void completeWork();

   int getTotalWork();

   default int getWorkRemaining() {
      return this.getTotalWork() - this.getWorkCompleted();
   }

   int getWorkCompleted();

   default String getProgressString() {
      return Form.pc(this.getProgress(), 0);
   }

   default double getProgress() {
      return (double)this.getWorkCompleted() / (double)this.getTotalWork();
   }

   default void execute(VolmitSender sender) {
      this.execute(sender, () -> {
      });
   }

   default void execute(VolmitSender sender, Runnable whenComplete) {
      this.execute(sender, false, whenComplete);
   }

   default void execute(VolmitSender sender, boolean silentMsg, Runnable whenComplete) {
      PrecisionStopwatch p = PrecisionStopwatch.start();
      CompletableFuture<?> f = J.afut(this::execute);
      int c = J.ar(() -> {
         if (sender.isPlayer()) {
            sender.sendProgress(this.getProgress(), this.getName());
         } else {
            String var10001 = this.getName();
            sender.sendMessage(var10001 + ": " + this.getProgressString());
         }

      }, sender.isPlayer() ? 0 : 20);
      f.whenComplete((fs, ff) -> {
         J.car(c);
         if (!silentMsg) {
            String var10001 = String.valueOf(C.AQUA);
            sender.sendMessage(var10001 + "Completed " + this.getName() + " in " + Form.duration(p.getMilliseconds(), 1));
         }

         whenComplete.run();
      });
   }
}
