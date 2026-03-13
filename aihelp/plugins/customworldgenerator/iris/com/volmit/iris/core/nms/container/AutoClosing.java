package com.volmit.iris.core.nms.container;

import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.function.NastyRunnable;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;

public class AutoClosing implements AutoCloseable {
   private static final KMap<Thread, AutoClosing> CONTEXTS = new KMap();
   private final AtomicBoolean closed = new AtomicBoolean();
   private final NastyRunnable action;

   public void close() {
      if (!this.closed.getAndSet(true)) {
         try {
            this.removeContext();
            this.action.run();
         } catch (Throwable var2) {
            throw new RuntimeException(var2);
         }
      }
   }

   public void storeContext() {
      CONTEXTS.put(Thread.currentThread(), this);
   }

   public void removeContext() {
      CONTEXTS.values().removeIf((var1) -> {
         return var1 == this;
      });
   }

   public static void closeContext() {
      AutoClosing var0 = (AutoClosing)CONTEXTS.remove(Thread.currentThread());
      if (var0 != null) {
         var0.close();
      }
   }

   @Generated
   public AutoClosing(final NastyRunnable action) {
      this.action = var1;
   }
}
