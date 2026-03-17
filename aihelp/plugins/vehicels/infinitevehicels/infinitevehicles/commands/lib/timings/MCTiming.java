package me.PM2.infinitevehicles.commands.lib.timings;

public abstract class MCTiming implements AutoCloseable {
   public abstract MCTiming startTiming();

   public abstract void stopTiming();

   public void close() {
      this.stopTiming();
   }
}
