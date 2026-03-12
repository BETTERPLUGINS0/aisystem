package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;

final class BoundedBuffer<E> extends StripedBuffer<E> {
   static final int BUFFER_SIZE = 16;
   static final int MASK = 15;

   protected Buffer<E> create(E e) {
      return new BoundedBuffer.RingBuffer(e);
   }

   static final class RingBuffer<E> extends BBHeader.ReadAndWriteCounterRef implements Buffer<E> {
      final AtomicReferenceArray<E> buffer = new AtomicReferenceArray(16);

      public RingBuffer(E e) {
         this.buffer.lazySet(0, e);
      }

      public int offer(E e) {
         long head = this.readCounter;
         long tail = this.relaxedWriteCounter();
         long size = tail - head;
         if (size >= 16L) {
            return 1;
         } else if (this.casWriteCounter(tail, tail + 1L)) {
            int index = (int)(tail & 15L);
            this.buffer.lazySet(index, e);
            return 0;
         } else {
            return -1;
         }
      }

      public void drainTo(Consumer<E> consumer) {
         long head = this.readCounter;
         long tail = this.relaxedWriteCounter();
         long size = tail - head;
         if (size != 0L) {
            do {
               int index = (int)(head & 15L);
               E e = this.buffer.get(index);
               if (e == null) {
                  break;
               }

               this.buffer.lazySet(index, (Object)null);
               consumer.accept(e);
               ++head;
            } while(head != tail);

            this.lazySetReadCounter(head);
         }
      }

      public int reads() {
         return (int)this.readCounter;
      }

      public int writes() {
         return (int)this.writeCounter;
      }
   }
}
