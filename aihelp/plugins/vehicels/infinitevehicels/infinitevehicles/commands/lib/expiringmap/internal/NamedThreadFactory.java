package me.PM2.infinitevehicles.commands.lib.expiringmap.internal;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
   private final AtomicInteger threadNumber = new AtomicInteger(1);
   private final String nameFormat;

   public NamedThreadFactory(String var1) {
      this.nameFormat = var1;
   }

   public Thread newThread(Runnable var1) {
      Thread var2 = new Thread(var1, String.format(this.nameFormat, this.threadNumber.getAndIncrement()));
      var2.setDaemon(true);
      return var2;
   }
}
