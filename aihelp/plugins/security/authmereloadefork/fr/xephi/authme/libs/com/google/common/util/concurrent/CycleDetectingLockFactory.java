package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.MoreObjects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.com.google.common.collect.Lists;
import fr.xephi.authme.libs.com.google.common.collect.MapMaker;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import fr.xephi.authme.libs.com.google.common.collect.Sets;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.j2objc.annotations.Weak;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@CanIgnoreReturnValue
@GwtIncompatible
public class CycleDetectingLockFactory {
   private static final ConcurrentMap<Class<? extends Enum<?>>, Map<? extends Enum<?>, CycleDetectingLockFactory.LockGraphNode>> lockGraphNodesPerType = (new MapMaker()).weakKeys().makeMap();
   private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
   final CycleDetectingLockFactory.Policy policy;
   private static final ThreadLocal<ArrayList<CycleDetectingLockFactory.LockGraphNode>> acquiredLocks = new ThreadLocal<ArrayList<CycleDetectingLockFactory.LockGraphNode>>() {
      protected ArrayList<CycleDetectingLockFactory.LockGraphNode> initialValue() {
         return Lists.newArrayListWithCapacity(3);
      }
   };

   public static CycleDetectingLockFactory newInstance(CycleDetectingLockFactory.Policy policy) {
      return new CycleDetectingLockFactory(policy);
   }

   public ReentrantLock newReentrantLock(String lockName) {
      return this.newReentrantLock(lockName, false);
   }

   public ReentrantLock newReentrantLock(String lockName, boolean fair) {
      return (ReentrantLock)(this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantLock(new CycleDetectingLockFactory.LockGraphNode(lockName), fair));
   }

   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName) {
      return this.newReentrantReadWriteLock(lockName, false);
   }

   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair) {
      return (ReentrantReadWriteLock)(this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this, new CycleDetectingLockFactory.LockGraphNode(lockName), fair));
   }

   public static <E extends Enum<E>> CycleDetectingLockFactory.WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, CycleDetectingLockFactory.Policy policy) {
      Preconditions.checkNotNull(enumClass);
      Preconditions.checkNotNull(policy);
      Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes = getOrCreateNodes(enumClass);
      return new CycleDetectingLockFactory.WithExplicitOrdering(policy, lockGraphNodes);
   }

   private static <E extends Enum<E>> Map<? extends E, CycleDetectingLockFactory.LockGraphNode> getOrCreateNodes(Class<E> clazz) {
      Map<E, CycleDetectingLockFactory.LockGraphNode> existing = (Map)lockGraphNodesPerType.get(clazz);
      if (existing != null) {
         return existing;
      } else {
         Map<E, CycleDetectingLockFactory.LockGraphNode> created = createNodes(clazz);
         existing = (Map)lockGraphNodesPerType.putIfAbsent(clazz, created);
         return (Map)MoreObjects.firstNonNull(existing, created);
      }
   }

   @VisibleForTesting
   static <E extends Enum<E>> Map<E, CycleDetectingLockFactory.LockGraphNode> createNodes(Class<E> clazz) {
      EnumMap<E, CycleDetectingLockFactory.LockGraphNode> map = Maps.newEnumMap(clazz);
      E[] keys = (Enum[])clazz.getEnumConstants();
      int numKeys = keys.length;
      ArrayList<CycleDetectingLockFactory.LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
      Enum[] var5 = keys;
      int var6 = keys.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         E key = var5[var7];
         CycleDetectingLockFactory.LockGraphNode node = new CycleDetectingLockFactory.LockGraphNode(getLockName(key));
         nodes.add(node);
         map.put(key, node);
      }

      int i;
      for(i = 1; i < numKeys; ++i) {
         ((CycleDetectingLockFactory.LockGraphNode)nodes.get(i)).checkAcquiredLocks(CycleDetectingLockFactory.Policies.THROW, nodes.subList(0, i));
      }

      for(i = 0; i < numKeys - 1; ++i) {
         ((CycleDetectingLockFactory.LockGraphNode)nodes.get(i)).checkAcquiredLocks(CycleDetectingLockFactory.Policies.DISABLED, nodes.subList(i + 1, numKeys));
      }

      return Collections.unmodifiableMap(map);
   }

   private static String getLockName(Enum<?> rank) {
      String var1 = rank.getDeclaringClass().getSimpleName();
      String var2 = rank.name();
      return (new StringBuilder(1 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(".").append(var2).toString();
   }

   private CycleDetectingLockFactory(CycleDetectingLockFactory.Policy policy) {
      this.policy = (CycleDetectingLockFactory.Policy)Preconditions.checkNotNull(policy);
   }

   private void aboutToAcquire(CycleDetectingLockFactory.CycleDetectingLock lock) {
      if (!lock.isAcquiredByCurrentThread()) {
         ArrayList<CycleDetectingLockFactory.LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
         CycleDetectingLockFactory.LockGraphNode node = lock.getLockGraphNode();
         node.checkAcquiredLocks(this.policy, acquiredLockList);
         acquiredLockList.add(node);
      }

   }

   private static void lockStateChanged(CycleDetectingLockFactory.CycleDetectingLock lock) {
      if (!lock.isAcquiredByCurrentThread()) {
         ArrayList<CycleDetectingLockFactory.LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
         CycleDetectingLockFactory.LockGraphNode node = lock.getLockGraphNode();

         for(int i = acquiredLockList.size() - 1; i >= 0; --i) {
            if (acquiredLockList.get(i) == node) {
               acquiredLockList.remove(i);
               break;
            }
         }
      }

   }

   // $FF: synthetic method
   CycleDetectingLockFactory(CycleDetectingLockFactory.Policy x0, Object x1) {
      this(x0);
   }

   private class CycleDetectingReentrantWriteLock extends WriteLock {
      @Weak
      final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;

      CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
         super(readWriteLock);
         this.readWriteLock = readWriteLock;
      }

      public void lock() {
         CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);

         try {
            super.lock();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

      }

      public void lockInterruptibly() throws InterruptedException {
         CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);

         try {
            super.lockInterruptibly();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

      }

      public boolean tryLock() {
         CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);

         boolean var1;
         try {
            var1 = super.tryLock();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

         return var1;
      }

      public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
         CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);

         boolean var4;
         try {
            var4 = super.tryLock(timeout, unit);
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

         return var4;
      }

      public void unlock() {
         try {
            super.unlock();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

      }
   }

   private class CycleDetectingReentrantReadLock extends ReadLock {
      @Weak
      final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;

      CycleDetectingReentrantReadLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
         super(readWriteLock);
         this.readWriteLock = readWriteLock;
      }

      public void lock() {
         CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);

         try {
            super.lock();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

      }

      public void lockInterruptibly() throws InterruptedException {
         CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);

         try {
            super.lockInterruptibly();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

      }

      public boolean tryLock() {
         CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);

         boolean var1;
         try {
            var1 = super.tryLock();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

         return var1;
      }

      public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
         CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);

         boolean var4;
         try {
            var4 = super.tryLock(timeout, unit);
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

         return var4;
      }

      public void unlock() {
         try {
            super.unlock();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
         }

      }
   }

   final class CycleDetectingReentrantReadWriteLock extends ReentrantReadWriteLock implements CycleDetectingLockFactory.CycleDetectingLock {
      private final CycleDetectingLockFactory.CycleDetectingReentrantReadLock readLock;
      private final CycleDetectingLockFactory.CycleDetectingReentrantWriteLock writeLock;
      private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;

      private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory this$0, CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
         super(fair);
         this.readLock = this$0.new CycleDetectingReentrantReadLock(this);
         this.writeLock = this$0.new CycleDetectingReentrantWriteLock(this);
         this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
      }

      public ReadLock readLock() {
         return this.readLock;
      }

      public WriteLock writeLock() {
         return this.writeLock;
      }

      public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
         return this.lockGraphNode;
      }

      public boolean isAcquiredByCurrentThread() {
         return this.isWriteLockedByCurrentThread() || this.getReadHoldCount() > 0;
      }

      // $FF: synthetic method
      CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory x0, CycleDetectingLockFactory.LockGraphNode x1, boolean x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLockFactory.CycleDetectingLock {
      private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;

      private CycleDetectingReentrantLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
         super(fair);
         this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
      }

      public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
         return this.lockGraphNode;
      }

      public boolean isAcquiredByCurrentThread() {
         return this.isHeldByCurrentThread();
      }

      public void lock() {
         CycleDetectingLockFactory.this.aboutToAcquire(this);

         try {
            super.lock();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this);
         }

      }

      public void lockInterruptibly() throws InterruptedException {
         CycleDetectingLockFactory.this.aboutToAcquire(this);

         try {
            super.lockInterruptibly();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this);
         }

      }

      public boolean tryLock() {
         CycleDetectingLockFactory.this.aboutToAcquire(this);

         boolean var1;
         try {
            var1 = super.tryLock();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this);
         }

         return var1;
      }

      public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
         CycleDetectingLockFactory.this.aboutToAcquire(this);

         boolean var4;
         try {
            var4 = super.tryLock(timeout, unit);
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this);
         }

         return var4;
      }

      public void unlock() {
         try {
            super.unlock();
         } finally {
            CycleDetectingLockFactory.lockStateChanged(this);
         }

      }

      // $FF: synthetic method
      CycleDetectingReentrantLock(CycleDetectingLockFactory.LockGraphNode x1, boolean x2, Object x3) {
         this(x1, x2);
      }
   }

   private static class LockGraphNode {
      final Map<CycleDetectingLockFactory.LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> allowedPriorLocks = (new MapMaker()).weakKeys().makeMap();
      final Map<CycleDetectingLockFactory.LockGraphNode, CycleDetectingLockFactory.PotentialDeadlockException> disallowedPriorLocks = (new MapMaker()).weakKeys().makeMap();
      final String lockName;

      LockGraphNode(String lockName) {
         this.lockName = (String)Preconditions.checkNotNull(lockName);
      }

      String getLockName() {
         return this.lockName;
      }

      void checkAcquiredLocks(CycleDetectingLockFactory.Policy policy, List<CycleDetectingLockFactory.LockGraphNode> acquiredLocks) {
         Iterator var3 = acquiredLocks.iterator();

         while(var3.hasNext()) {
            CycleDetectingLockFactory.LockGraphNode acquiredLock = (CycleDetectingLockFactory.LockGraphNode)var3.next();
            this.checkAcquiredLock(policy, acquiredLock);
         }

      }

      void checkAcquiredLock(CycleDetectingLockFactory.Policy policy, CycleDetectingLockFactory.LockGraphNode acquiredLock) {
         Preconditions.checkState(this != acquiredLock, "Attempted to acquire multiple locks with the same rank %s", (Object)acquiredLock.getLockName());
         if (!this.allowedPriorLocks.containsKey(acquiredLock)) {
            CycleDetectingLockFactory.PotentialDeadlockException previousDeadlockException = (CycleDetectingLockFactory.PotentialDeadlockException)this.disallowedPriorLocks.get(acquiredLock);
            if (previousDeadlockException != null) {
               CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace());
               policy.handlePotentialDeadlock(exception);
            } else {
               Set<CycleDetectingLockFactory.LockGraphNode> seen = Sets.newIdentityHashSet();
               CycleDetectingLockFactory.ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
               if (path == null) {
                  this.allowedPriorLocks.put(acquiredLock, new CycleDetectingLockFactory.ExampleStackTrace(acquiredLock, this));
               } else {
                  CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, path);
                  this.disallowedPriorLocks.put(acquiredLock, exception);
                  policy.handlePotentialDeadlock(exception);
               }

            }
         }
      }

      @CheckForNull
      private CycleDetectingLockFactory.ExampleStackTrace findPathTo(CycleDetectingLockFactory.LockGraphNode node, Set<CycleDetectingLockFactory.LockGraphNode> seen) {
         if (!seen.add(this)) {
            return null;
         } else {
            CycleDetectingLockFactory.ExampleStackTrace found = (CycleDetectingLockFactory.ExampleStackTrace)this.allowedPriorLocks.get(node);
            if (found != null) {
               return found;
            } else {
               Iterator var4 = this.allowedPriorLocks.entrySet().iterator();

               Entry entry;
               CycleDetectingLockFactory.LockGraphNode preAcquiredLock;
               do {
                  if (!var4.hasNext()) {
                     return null;
                  }

                  entry = (Entry)var4.next();
                  preAcquiredLock = (CycleDetectingLockFactory.LockGraphNode)entry.getKey();
                  found = preAcquiredLock.findPathTo(node, seen);
               } while(found == null);

               CycleDetectingLockFactory.ExampleStackTrace path = new CycleDetectingLockFactory.ExampleStackTrace(preAcquiredLock, this);
               path.setStackTrace(((CycleDetectingLockFactory.ExampleStackTrace)entry.getValue()).getStackTrace());
               path.initCause(found);
               return path;
            }
         }
      }
   }

   private interface CycleDetectingLock {
      CycleDetectingLockFactory.LockGraphNode getLockGraphNode();

      boolean isAcquiredByCurrentThread();
   }

   @Beta
   public static final class PotentialDeadlockException extends CycleDetectingLockFactory.ExampleStackTrace {
      private final CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace;

      private PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2, CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace) {
         super(node1, node2);
         this.conflictingStackTrace = conflictingStackTrace;
         this.initCause(conflictingStackTrace);
      }

      public CycleDetectingLockFactory.ExampleStackTrace getConflictingStackTrace() {
         return this.conflictingStackTrace;
      }

      public String getMessage() {
         StringBuilder message = new StringBuilder((String)Objects.requireNonNull(super.getMessage()));

         for(Object t = this.conflictingStackTrace; t != null; t = ((Throwable)t).getCause()) {
            message.append(", ").append(((Throwable)t).getMessage());
         }

         return message.toString();
      }

      // $FF: synthetic method
      PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode x0, CycleDetectingLockFactory.LockGraphNode x1, CycleDetectingLockFactory.ExampleStackTrace x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   private static class ExampleStackTrace extends IllegalStateException {
      static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
      static final ImmutableSet<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class.getName(), CycleDetectingLockFactory.ExampleStackTrace.class.getName(), CycleDetectingLockFactory.LockGraphNode.class.getName());

      ExampleStackTrace(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2) {
         String var3 = node1.getLockName();
         String var4 = node2.getLockName();
         super((new StringBuilder(4 + String.valueOf(var3).length() + String.valueOf(var4).length())).append(var3).append(" -> ").append(var4).toString());
         StackTraceElement[] origStackTrace = this.getStackTrace();
         int i = 0;

         for(int n = origStackTrace.length; i < n; ++i) {
            if (CycleDetectingLockFactory.WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName())) {
               this.setStackTrace(EMPTY_STACK_TRACE);
               break;
            }

            if (!EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
               this.setStackTrace((StackTraceElement[])Arrays.copyOfRange(origStackTrace, i, n));
               break;
            }
         }

      }
   }

   @Beta
   public static final class WithExplicitOrdering<E extends Enum<E>> extends CycleDetectingLockFactory {
      private final Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes;

      @VisibleForTesting
      WithExplicitOrdering(CycleDetectingLockFactory.Policy policy, Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes) {
         super(policy, null);
         this.lockGraphNodes = lockGraphNodes;
      }

      public ReentrantLock newReentrantLock(E rank) {
         return this.newReentrantLock(rank, false);
      }

      public ReentrantLock newReentrantLock(E rank, boolean fair) {
         return (ReentrantLock)(this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantLock((CycleDetectingLockFactory.LockGraphNode)Objects.requireNonNull((CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(rank)), fair));
      }

      public ReentrantReadWriteLock newReentrantReadWriteLock(E rank) {
         return this.newReentrantReadWriteLock(rank, false);
      }

      public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair) {
         return (ReentrantReadWriteLock)(this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this, (CycleDetectingLockFactory.LockGraphNode)Objects.requireNonNull((CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(rank)), fair));
      }
   }

   @Beta
   public static enum Policies implements CycleDetectingLockFactory.Policy {
      THROW {
         public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {
            throw e;
         }
      },
      WARN {
         public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {
            CycleDetectingLockFactory.logger.log(Level.SEVERE, "Detected potential deadlock", e);
         }
      },
      DISABLED {
         public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {
         }
      };

      private Policies() {
      }

      // $FF: synthetic method
      private static CycleDetectingLockFactory.Policies[] $values() {
         return new CycleDetectingLockFactory.Policies[]{THROW, WARN, DISABLED};
      }

      // $FF: synthetic method
      Policies(Object x2) {
         this();
      }
   }

   @Beta
   public interface Policy {
      void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException var1);
   }
}
