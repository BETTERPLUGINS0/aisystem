package com.bergerkiller.bukkit.tc.utils;

import java.util.function.Consumer;

public interface CircularFIFOQueue<E> {
   int capacity();

   void abort();

   boolean isAborted();

   default boolean isEmpty() {
      return this.runIfEmpty(() -> {
      });
   }

   void setWakeCallback(Runnable var1);

   boolean runIfEmpty(Runnable var1);

   default E take() throws CircularFIFOQueue.EmptyQueueException {
      return this.take(Long.MAX_VALUE);
   }

   E take(long var1) throws CircularFIFOQueue.EmptyQueueException;

   void put(E var1);

   static <E> CircularFIFOQueue<E> forward(final Consumer<E> consumer) {
      return new CircularFIFOQueue<E>() {
         public int capacity() {
            return 0;
         }

         public void abort() {
         }

         public boolean isAborted() {
            return true;
         }

         public void setWakeCallback(Runnable callback) {
         }

         public boolean isEmpty() {
            return true;
         }

         public boolean runIfEmpty(Runnable runnable) {
            runnable.run();
            return true;
         }

         public E take(long timeoutMillis) throws CircularFIFOQueue.EmptyQueueException {
            throw CircularFIFOQueue.EmptyQueueException.INSTANCE;
         }

         public void put(E value) {
            consumer.accept(value);
         }
      };
   }

   public static final class EmptyQueueException extends Exception {
      public static final CircularFIFOQueue.EmptyQueueException INSTANCE = new CircularFIFOQueue.EmptyQueueException();
      private static final long serialVersionUID = 1824696362338789498L;

      public Throwable fillInStackTrace() {
         return this;
      }
   }
}
