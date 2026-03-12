package org.apache.commons.lang3.concurrent.locks;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Supplier;
import org.apache.commons.lang3.function.Failable;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableFunction;

public class LockingVisitors {
   public static <O> LockingVisitors.ReadWriteLockVisitor<O> reentrantReadWriteLockVisitor(O var0) {
      return new LockingVisitors.ReadWriteLockVisitor(var0, new ReentrantReadWriteLock());
   }

   public static <O> LockingVisitors.StampedLockVisitor<O> stampedLockVisitor(O var0) {
      return new LockingVisitors.StampedLockVisitor(var0, new StampedLock());
   }

   public static class StampedLockVisitor<O> extends LockingVisitors.LockVisitor<O, StampedLock> {
      protected StampedLockVisitor(O var1, StampedLock var2) {
         super(var1, var2, var2::asReadLock, var2::asWriteLock);
      }
   }

   public static class ReadWriteLockVisitor<O> extends LockingVisitors.LockVisitor<O, ReadWriteLock> {
      protected ReadWriteLockVisitor(O var1, ReadWriteLock var2) {
         super(var1, var2, var2::readLock, var2::writeLock);
      }
   }

   public static class LockVisitor<O, L> {
      private final L lock;
      private final O object;
      private final Supplier<Lock> readLockSupplier;
      private final Supplier<Lock> writeLockSupplier;

      protected LockVisitor(O var1, L var2, Supplier<Lock> var3, Supplier<Lock> var4) {
         this.object = Objects.requireNonNull(var1, "object");
         this.lock = Objects.requireNonNull(var2, "lock");
         this.readLockSupplier = (Supplier)Objects.requireNonNull(var3, "readLockSupplier");
         this.writeLockSupplier = (Supplier)Objects.requireNonNull(var4, "writeLockSupplier");
      }

      public void acceptReadLocked(FailableConsumer<O, ?> var1) {
         this.lockAcceptUnlock(this.readLockSupplier, var1);
      }

      public void acceptWriteLocked(FailableConsumer<O, ?> var1) {
         this.lockAcceptUnlock(this.writeLockSupplier, var1);
      }

      public <T> T applyReadLocked(FailableFunction<O, T, ?> var1) {
         return this.lockApplyUnlock(this.readLockSupplier, var1);
      }

      public <T> T applyWriteLocked(FailableFunction<O, T, ?> var1) {
         return this.lockApplyUnlock(this.writeLockSupplier, var1);
      }

      public L getLock() {
         return this.lock;
      }

      public O getObject() {
         return this.object;
      }

      protected void lockAcceptUnlock(Supplier<Lock> var1, FailableConsumer<O, ?> var2) {
         Lock var3 = (Lock)var1.get();
         var3.lock();

         try {
            var2.accept(this.object);
         } catch (Throwable var8) {
            throw Failable.rethrow(var8);
         } finally {
            var3.unlock();
         }

      }

      protected <T> T lockApplyUnlock(Supplier<Lock> var1, FailableFunction<O, T, ?> var2) {
         Lock var3 = (Lock)var1.get();
         var3.lock();

         Object var4;
         try {
            var4 = var2.apply(this.object);
         } catch (Throwable var8) {
            throw Failable.rethrow(var8);
         } finally {
            var3.unlock();
         }

         return var4;
      }
   }
}
