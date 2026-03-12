package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.MoreObjects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Supplier;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.collect.Lists;
import fr.xephi.authme.libs.com.google.common.collect.MapMaker;
import fr.xephi.authme.libs.com.google.common.math.IntMath;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public abstract class Striped<L> {
   private static final int LARGE_LAZY_CUTOFF = 1024;
   private static final int ALL_SET = -1;

   private Striped() {
   }

   public abstract L get(Object var1);

   public abstract L getAt(int var1);

   abstract int indexFor(Object var1);

   public abstract int size();

   public Iterable<L> bulkGet(Iterable<? extends Object> keys) {
      List<Object> result = Lists.newArrayList(keys);
      if (result.isEmpty()) {
         return ImmutableList.of();
      } else {
         int[] stripes = new int[result.size()];

         int previousStripe;
         for(previousStripe = 0; previousStripe < result.size(); ++previousStripe) {
            stripes[previousStripe] = this.indexFor(result.get(previousStripe));
         }

         Arrays.sort(stripes);
         previousStripe = stripes[0];
         result.set(0, this.getAt(previousStripe));

         for(int i = 1; i < result.size(); ++i) {
            int currentStripe = stripes[i];
            if (currentStripe == previousStripe) {
               result.set(i, result.get(i - 1));
            } else {
               result.set(i, this.getAt(currentStripe));
               previousStripe = currentStripe;
            }
         }

         return Collections.unmodifiableList(result);
      }
   }

   static <L> Striped<L> custom(int stripes, Supplier<L> supplier) {
      return new Striped.CompactStriped(stripes, supplier);
   }

   public static Striped<Lock> lock(int stripes) {
      return custom(stripes, Striped.PaddedLock::new);
   }

   public static Striped<Lock> lazyWeakLock(int stripes) {
      return lazy(stripes, () -> {
         return new ReentrantLock(false);
      });
   }

   private static <L> Striped<L> lazy(int stripes, Supplier<L> supplier) {
      return (Striped)(stripes < 1024 ? new Striped.SmallLazyStriped(stripes, supplier) : new Striped.LargeLazyStriped(stripes, supplier));
   }

   public static Striped<Semaphore> semaphore(int stripes, int permits) {
      return custom(stripes, () -> {
         return new Striped.PaddedSemaphore(permits);
      });
   }

   public static Striped<Semaphore> lazyWeakSemaphore(int stripes, int permits) {
      return lazy(stripes, () -> {
         return new Semaphore(permits, false);
      });
   }

   public static Striped<ReadWriteLock> readWriteLock(int stripes) {
      return custom(stripes, ReentrantReadWriteLock::new);
   }

   public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int stripes) {
      return lazy(stripes, Striped.WeakSafeReadWriteLock::new);
   }

   private static int ceilToPowerOfTwo(int x) {
      return 1 << IntMath.log2(x, RoundingMode.CEILING);
   }

   private static int smear(int hashCode) {
      hashCode ^= hashCode >>> 20 ^ hashCode >>> 12;
      return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
   }

   // $FF: synthetic method
   Striped(Object x0) {
      this();
   }

   private static class PaddedSemaphore extends Semaphore {
      long unused1;
      long unused2;
      long unused3;

      PaddedSemaphore(int permits) {
         super(permits, false);
      }
   }

   private static class PaddedLock extends ReentrantLock {
      long unused1;
      long unused2;
      long unused3;

      PaddedLock() {
         super(false);
      }
   }

   @VisibleForTesting
   static class LargeLazyStriped<L> extends Striped.PowerOfTwoStriped<L> {
      final ConcurrentMap<Integer, L> locks;
      final Supplier<L> supplier;
      final int size;

      LargeLazyStriped(int stripes, Supplier<L> supplier) {
         super(stripes);
         this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
         this.supplier = supplier;
         this.locks = (new MapMaker()).weakValues().makeMap();
      }

      public L getAt(int index) {
         if (this.size != Integer.MAX_VALUE) {
            Preconditions.checkElementIndex(index, this.size());
         }

         L existing = this.locks.get(index);
         if (existing != null) {
            return existing;
         } else {
            L created = this.supplier.get();
            existing = this.locks.putIfAbsent(index, created);
            return MoreObjects.firstNonNull(existing, created);
         }
      }

      public int size() {
         return this.size;
      }
   }

   @VisibleForTesting
   static class SmallLazyStriped<L> extends Striped.PowerOfTwoStriped<L> {
      final AtomicReferenceArray<Striped.SmallLazyStriped.ArrayReference<? extends L>> locks;
      final Supplier<L> supplier;
      final int size;
      final ReferenceQueue<L> queue = new ReferenceQueue();

      SmallLazyStriped(int stripes, Supplier<L> supplier) {
         super(stripes);
         this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
         this.locks = new AtomicReferenceArray(this.size);
         this.supplier = supplier;
      }

      public L getAt(int index) {
         if (this.size != Integer.MAX_VALUE) {
            Preconditions.checkElementIndex(index, this.size());
         }

         Striped.SmallLazyStriped.ArrayReference<? extends L> existingRef = (Striped.SmallLazyStriped.ArrayReference)this.locks.get(index);
         L existing = existingRef == null ? null : existingRef.get();
         if (existing != null) {
            return existing;
         } else {
            L created = this.supplier.get();
            Striped.SmallLazyStriped.ArrayReference newRef = new Striped.SmallLazyStriped.ArrayReference(created, index, this.queue);

            do {
               if (this.locks.compareAndSet(index, existingRef, newRef)) {
                  this.drainQueue();
                  return created;
               }

               existingRef = (Striped.SmallLazyStriped.ArrayReference)this.locks.get(index);
               existing = existingRef == null ? null : existingRef.get();
            } while(existing == null);

            return existing;
         }
      }

      private void drainQueue() {
         Reference ref;
         while((ref = this.queue.poll()) != null) {
            Striped.SmallLazyStriped.ArrayReference<? extends L> arrayRef = (Striped.SmallLazyStriped.ArrayReference)ref;
            this.locks.compareAndSet(arrayRef.index, arrayRef, (Object)null);
         }

      }

      public int size() {
         return this.size;
      }

      private static final class ArrayReference<L> extends WeakReference<L> {
         final int index;

         ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
            super(referent, queue);
            this.index = index;
         }
      }
   }

   private static class CompactStriped<L> extends Striped.PowerOfTwoStriped<L> {
      private final Object[] array;

      private CompactStriped(int stripes, Supplier<L> supplier) {
         super(stripes);
         Preconditions.checkArgument(stripes <= 1073741824, "Stripes must be <= 2^30)");
         this.array = new Object[this.mask + 1];

         for(int i = 0; i < this.array.length; ++i) {
            this.array[i] = supplier.get();
         }

      }

      public L getAt(int index) {
         return this.array[index];
      }

      public int size() {
         return this.array.length;
      }

      // $FF: synthetic method
      CompactStriped(int x0, Supplier x1, Object x2) {
         this(x0, x1);
      }
   }

   private abstract static class PowerOfTwoStriped<L> extends Striped<L> {
      final int mask;

      PowerOfTwoStriped(int stripes) {
         super(null);
         Preconditions.checkArgument(stripes > 0, "Stripes must be positive");
         this.mask = stripes > 1073741824 ? -1 : Striped.ceilToPowerOfTwo(stripes) - 1;
      }

      final int indexFor(Object key) {
         int hash = Striped.smear(key.hashCode());
         return hash & this.mask;
      }

      public final L get(Object key) {
         return this.getAt(this.indexFor(key));
      }
   }

   private static final class WeakSafeCondition extends ForwardingCondition {
      private final Condition delegate;
      private final Striped.WeakSafeReadWriteLock strongReference;

      WeakSafeCondition(Condition delegate, Striped.WeakSafeReadWriteLock strongReference) {
         this.delegate = delegate;
         this.strongReference = strongReference;
      }

      Condition delegate() {
         return this.delegate;
      }
   }

   private static final class WeakSafeLock extends ForwardingLock {
      private final Lock delegate;
      private final Striped.WeakSafeReadWriteLock strongReference;

      WeakSafeLock(Lock delegate, Striped.WeakSafeReadWriteLock strongReference) {
         this.delegate = delegate;
         this.strongReference = strongReference;
      }

      Lock delegate() {
         return this.delegate;
      }

      public Condition newCondition() {
         return new Striped.WeakSafeCondition(this.delegate.newCondition(), this.strongReference);
      }
   }

   private static final class WeakSafeReadWriteLock implements ReadWriteLock {
      private final ReadWriteLock delegate = new ReentrantReadWriteLock();

      WeakSafeReadWriteLock() {
      }

      public Lock readLock() {
         return new Striped.WeakSafeLock(this.delegate.readLock(), this);
      }

      public Lock writeLock() {
         return new Striped.WeakSafeLock(this.delegate.writeLock(), this);
      }
   }
}
