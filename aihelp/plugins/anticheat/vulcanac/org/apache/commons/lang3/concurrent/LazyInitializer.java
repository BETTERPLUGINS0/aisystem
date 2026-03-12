package org.apache.commons.lang3.concurrent;

public abstract class LazyInitializer<T> implements ConcurrentInitializer<T> {
   private static final Object NO_INIT = new Object();
   private volatile T object;

   public LazyInitializer() {
      this.object = NO_INIT;
   }

   public T get() {
      Object var1 = this.object;
      if (var1 == NO_INIT) {
         synchronized(this) {
            var1 = this.object;
            if (var1 == NO_INIT) {
               this.object = var1 = this.initialize();
            }
         }
      }

      return var1;
   }

   protected abstract T initialize();
}
