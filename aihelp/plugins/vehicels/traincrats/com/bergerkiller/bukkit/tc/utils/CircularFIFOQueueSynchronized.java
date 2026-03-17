package com.bergerkiller.bukkit.tc.utils;

public class CircularFIFOQueueSynchronized<E> implements CircularFIFOQueue<E> {
   private int writePos;
   private int readPos;
   private Object[] buffer;
   private boolean aborted;
   private boolean waiting;
   private Runnable wakeCallback;

   public CircularFIFOQueueSynchronized() {
      this(64);
   }

   public CircularFIFOQueueSynchronized(int initialCapacity) {
      this.aborted = false;
      this.waiting = false;
      this.wakeCallback = () -> {
      };
      this.writePos = 0;
      this.readPos = 0;
      this.buffer = new Object[initialCapacity];
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

   public synchronized boolean runIfEmpty(Runnable runnable) {
      if (this.writePos == this.readPos) {
         runnable.run();
         return true;
      } else {
         return false;
      }
   }

   public synchronized E take(long timeoutMillis) throws CircularFIFOQueue.EmptyQueueException {
      int rpos;
      if ((rpos = this.readPos) == this.writePos) {
         if (timeoutMillis <= 0L || this.aborted) {
            throw CircularFIFOQueue.EmptyQueueException.INSTANCE;
         }

         if (timeoutMillis == Long.MAX_VALUE) {
            try {
               label220: {
                  this.waiting = true;

                  while(!this.aborted) {
                     try {
                        this.wait();
                     } catch (InterruptedException var19) {
                     }

                     if ((rpos = this.readPos) != this.writePos) {
                        break label220;
                     }
                  }

                  throw CircularFIFOQueue.EmptyQueueException.INSTANCE;
               }
            } finally {
               this.waiting = false;
            }
         } else {
            try {
               label222: {
                  this.waiting = true;
                  long deadline = System.currentTimeMillis() + timeoutMillis;
                  long remaining = timeoutMillis;

                  while(!this.aborted) {
                     try {
                        this.wait(remaining);
                     } catch (InterruptedException var18) {
                     }

                     if ((rpos = this.readPos) != this.writePos) {
                        break label222;
                     }

                     if ((remaining = deadline - System.currentTimeMillis()) < 0L) {
                        throw CircularFIFOQueue.EmptyQueueException.INSTANCE;
                     }
                  }

                  throw CircularFIFOQueue.EmptyQueueException.INSTANCE;
               }
            } finally {
               this.waiting = false;
            }
         }
      }

      Object[] buffer = this.buffer;
      --rpos;
      if (rpos < 0) {
         rpos = buffer.length - 1;
      }

      this.readPos = rpos;
      Object value = buffer[rpos];
      buffer[rpos] = null;
      return value;
   }

   public synchronized void put(E value) {
      Object[] buffer = this.buffer;
      int read_pos = this.readPos;
      int curr_pos = this.writePos;
      int next_pos = curr_pos - 1;
      if (next_pos < 0) {
         next_pos = buffer.length - 1;
      }

      if (next_pos == read_pos) {
         Object[] new_buffer = new Object[buffer.length * 4 / 3];

         int index;
         for(index = new_buffer.length; read_pos != curr_pos; new_buffer[index] = buffer[read_pos]) {
            --read_pos;
            if (read_pos < 0) {
               read_pos = buffer.length - 1;
            }

            --index;
         }

         buffer = new_buffer;
         this.buffer = new_buffer;
         read_pos = 0;
         this.readPos = 0;
         curr_pos = index;
         this.writePos = index;
         next_pos = index - 1;
         if (next_pos < 0) {
            next_pos = new_buffer.length - 1;
         }
      }

      buffer[next_pos] = value;
      this.writePos = next_pos;
      if (curr_pos == read_pos) {
         if (this.waiting) {
            this.notifyAll();
         } else {
            this.wakeCallback.run();
         }
      }

   }
}
