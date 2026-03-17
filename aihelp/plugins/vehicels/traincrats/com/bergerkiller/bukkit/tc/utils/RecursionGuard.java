package com.bergerkiller.bukkit.tc.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public final class RecursionGuard<T> {
   private final AtomicBoolean opened = new AtomicBoolean(false);
   private static final RecursionGuard.Token INACTIVE_TOKEN = () -> {
   };
   private final RecursionGuard.Token ACTIVE_TOKEN = () -> {
      this.opened.set(false);
   };
   private final RecursionGuard.Handler<T> handler;

   private RecursionGuard(RecursionGuard.Handler<T> handler) {
      this.handler = handler;
   }

   public static <T> RecursionGuard<T> handleOnce(final RecursionGuard.Handler<T> handler) {
      return handle(new RecursionGuard.Handler<T>() {
         private final AtomicBoolean handled = new AtomicBoolean(false);

         public void onRecursion(T value) {
            if (this.handled.compareAndSet(false, true)) {
               handler.onRecursion(value);
            }

         }
      });
   }

   public static <T> RecursionGuard<T> handle(RecursionGuard.Handler<T> handler) {
      return new RecursionGuard(handler);
   }

   public RecursionGuard.Token open(T value) {
      if (this.opened.compareAndSet(false, true)) {
         return this.ACTIVE_TOKEN;
      } else {
         this.handler.onRecursion(value);
         return INACTIVE_TOKEN;
      }
   }

   public interface Token extends AutoCloseable {
      void close();
   }

   @FunctionalInterface
   public interface Handler<T> {
      void onRecursion(T var1);
   }
}
