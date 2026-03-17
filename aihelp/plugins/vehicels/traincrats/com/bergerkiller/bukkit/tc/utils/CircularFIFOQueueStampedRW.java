package com.bergerkiller.bukkit.tc.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

public class CircularFIFOQueueStampedRW<E> implements CircularFIFOQueue<E> {
   private final AtomicInteger writePos;
   private int readPos;
   private Object[] buffer;
   private final StampedLock lock;
   private boolean aborted;
   private boolean waiting;
   private Runnable wakeCallback;
   private static final CircularFIFOQueueStampedRW.CompareAndExchangeFunc compareAndExchange = detectCompareAndExchangeFunc();

   public CircularFIFOQueueStampedRW() {
      this(64);
   }

   public CircularFIFOQueueStampedRW(int initialCapacity) {
      this.aborted = false;
      this.waiting = false;
      this.wakeCallback = () -> {
      };
      this.writePos = new AtomicInteger(0);
      this.readPos = 0;
      this.buffer = new Object[initialCapacity];
      this.lock = new StampedLock();
   }

   public int capacity() {
      return this.buffer.length;
   }

   public synchronized void abort() {
      this.aborted = true;
      this.notifyAll();
   }

   public boolean isAborted() {
      return this.aborted;
   }

   public synchronized void setWakeCallback(Runnable callback) {
      this.wakeCallback = callback;
   }

   public boolean runIfEmpty(Runnable runnable) {
      long slowCheckLock = this.lock.tryWriteLock();
      if (slowCheckLock == 0L) {
         return false;
      } else {
         boolean var4;
         try {
            if (this.readPos != this.writePos.get()) {
               var4 = false;
               return var4;
            }

            runnable.run();
            var4 = true;
         } finally {
            this.lock.unlockWrite(slowCheckLock);
         }

         return var4;
      }
   }

   public E take(long timeoutMillis) throws CircularFIFOQueue.EmptyQueueException {
      label241:
      while(true) {
         long slowTakeLock = this.lock.writeLock();
         int rpos = this.readPos;
         if (rpos != this.writePos.get()) {
            Object[] buffer = this.buffer;
            --rpos;
            if (rpos < 0) {
               rpos = buffer.length - 1;
            }

            this.readPos = rpos;
            Object value = buffer[rpos];
            buffer[rpos] = null;
            this.lock.unlockWrite(slowTakeLock);
            return value;
         }

         synchronized(this) {
            this.lock.unlockWrite(slowTakeLock);
            if (timeoutMillis <= 0L || this.aborted) {
               throw CircularFIFOQueue.EmptyQueueException.INSTANCE;
            }

            if (timeoutMillis == Long.MAX_VALUE) {
               try {
                  this.waiting = true;

                  while(!this.aborted) {
                     try {
                        this.wait();
                     } catch (InterruptedException var24) {
                     }

                     if (this.readPos != this.writePos.get()) {
                        continue label241;
                     }
                  }

                  throw CircularFIFOQueue.EmptyQueueException.INSTANCE;
               } finally {
                  this.waiting = false;
               }
            } else {
               try {
                  this.waiting = true;
                  long deadline = System.currentTimeMillis() + timeoutMillis;
                  long remaining = timeoutMillis;

                  while(!this.aborted) {
                     try {
                        this.wait(remaining);
                     } catch (InterruptedException var25) {
                     }

                     if (this.readPos != this.writePos.get()) {
                        continue label241;
                     }

                     if ((remaining = deadline - System.currentTimeMillis()) < 0L) {
                        throw CircularFIFOQueue.EmptyQueueException.INSTANCE;
                     }
                  }

                  throw CircularFIFOQueue.EmptyQueueException.INSTANCE;
               } finally {
                  this.waiting = false;
               }
            }
         }
      }
   }

   public void put(E value) {
      if (!this.fastPut(value)) {
         long slowPutLock = this.lock.writeLock();

         try {
            this.slowPut(value);
         } finally {
            this.lock.unlockWrite(slowPutLock);
         }
      }

   }

   private boolean fastPut(E value) {
      long fastPutLock = this.lock.readLock();
      Object[] buffer = this.buffer;
      int rpos = this.readPos;
      int exchangeResult = this.writePos.get();

      int currWrite;
      int nextWrite;
      do {
         currWrite = exchangeResult;
         nextWrite = exchangeResult - 1;
         if (nextWrite < 0) {
            nextWrite = buffer.length - 1;
         }

         if (nextWrite == rpos) {
            long slowOverflowPutLock = this.lock.tryConvertToWriteLock(fastPutLock);
            if (slowOverflowPutLock == 0L) {
               this.lock.unlockRead(fastPutLock);
               return false;
            }

            try {
               this.slowPut(value);
            } finally {
               this.lock.unlockWrite(slowOverflowPutLock);
            }

            return true;
         }
      } while((exchangeResult = compareAndExchange.call(this.writePos, exchangeResult, nextWrite)) != currWrite);

      buffer[nextWrite] = value;
      this.lock.unlockRead(fastPutLock);
      if (currWrite == rpos) {
         this.notifyEmpty();
      }

      return true;
   }

   private void slowPut(E value) {
      int currWrite = this.writePos.get();
      int nextWrite = currWrite - 1;
      if (nextWrite < 0) {
         nextWrite = this.buffer.length - 1;
      }

      int rpos = this.readPos;
      if (nextWrite == rpos) {
         Object[] old_buffer = this.buffer;
         Object[] new_buffer = new Object[old_buffer.length * 4 / 3];

         int index;
         for(index = new_buffer.length; rpos != currWrite; new_buffer[index] = old_buffer[rpos]) {
            --rpos;
            if (rpos < 0) {
               rpos = old_buffer.length - 1;
            }

            --index;
         }

         --index;
         new_buffer[index] = value;
         this.buffer = new_buffer;
         this.readPos = 0;
         this.writePos.set(index);
      } else {
         this.buffer[nextWrite] = value;
         this.writePos.set(nextWrite);
         if (currWrite == rpos) {
            this.notifyEmpty();
         }
      }

   }

   private void notifyEmpty() {
      synchronized(this) {
         if (this.waiting) {
            this.notifyAll();
            return;
         }
      }

      this.wakeCallback.run();
   }

   private static CircularFIFOQueueStampedRW.CompareAndExchangeFunc detectCompareAndExchangeFunc() {
      try {
         AtomicInteger.class.getDeclaredMethod("compareAndExchange", Integer.TYPE, Integer.TYPE);
         return AtomicInteger::compareAndExchangeAcquire;
      } catch (Throwable var1) {
         return (ai, expectedValue, newValue) -> {
            int realValue;
            do {
               if (ai.compareAndSet(expectedValue, newValue)) {
                  return expectedValue;
               }

               realValue = ai.get();
            } while(realValue == expectedValue);

            return realValue;
         };
      }
   }

   @FunctionalInterface
   private interface CompareAndExchangeFunc {
      int call(AtomicInteger var1, int var2, int var3);
   }
}
