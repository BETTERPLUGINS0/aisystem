package org.apache.commons.lang3;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.lang3.time.DurationUtils;

public class ThreadUtils {
   public static final ThreadUtils.AlwaysTruePredicate ALWAYS_TRUE_PREDICATE = new ThreadUtils.AlwaysTruePredicate();

   public static Thread findThreadById(long var0) {
      Collection var2 = findThreads(new ThreadUtils.ThreadIdPredicate(var0));
      return var2.isEmpty() ? null : (Thread)var2.iterator().next();
   }

   public static Thread findThreadById(long var0, String var2) {
      Validate.notNull(var2, "threadGroupName");
      Thread var3 = findThreadById(var0);
      return var3 != null && var3.getThreadGroup() != null && var3.getThreadGroup().getName().equals(var2) ? var3 : null;
   }

   public static Thread findThreadById(long var0, ThreadGroup var2) {
      Validate.notNull(var2, "threadGroup");
      Thread var3 = findThreadById(var0);
      return var3 != null && var2.equals(var3.getThreadGroup()) ? var3 : null;
   }

   public static Collection<ThreadGroup> findThreadGroups(ThreadGroup var0, boolean var1, ThreadUtils.ThreadGroupPredicate var2) {
      Validate.notNull(var0, "group");
      Validate.notNull(var2, "predicate");
      int var3 = var0.activeGroupCount();

      ThreadGroup[] var4;
      do {
         var4 = new ThreadGroup[var3 + var3 / 2 + 1];
         var3 = var0.enumerate(var4, var1);
      } while(var3 >= var4.length);

      ArrayList var5 = new ArrayList(var3);

      for(int var6 = 0; var6 < var3; ++var6) {
         if (var2.test(var4[var6])) {
            var5.add(var4[var6]);
         }
      }

      return Collections.unmodifiableCollection(var5);
   }

   public static Collection<ThreadGroup> findThreadGroups(ThreadUtils.ThreadGroupPredicate var0) {
      return findThreadGroups(getSystemThreadGroup(), true, var0);
   }

   public static Collection<ThreadGroup> findThreadGroupsByName(String var0) {
      return findThreadGroups(new ThreadUtils.NamePredicate(var0));
   }

   public static Collection<Thread> findThreads(ThreadGroup var0, boolean var1, ThreadUtils.ThreadPredicate var2) {
      Validate.notNull(var0, "The group must not be null");
      Validate.notNull(var2, "The predicate must not be null");
      int var3 = var0.activeCount();

      Thread[] var4;
      do {
         var4 = new Thread[var3 + var3 / 2 + 1];
         var3 = var0.enumerate(var4, var1);
      } while(var3 >= var4.length);

      ArrayList var5 = new ArrayList(var3);

      for(int var6 = 0; var6 < var3; ++var6) {
         if (var2.test(var4[var6])) {
            var5.add(var4[var6]);
         }
      }

      return Collections.unmodifiableCollection(var5);
   }

   public static Collection<Thread> findThreads(ThreadUtils.ThreadPredicate var0) {
      return findThreads(getSystemThreadGroup(), true, var0);
   }

   public static Collection<Thread> findThreadsByName(String var0) {
      return findThreads(new ThreadUtils.NamePredicate(var0));
   }

   public static Collection<Thread> findThreadsByName(String var0, String var1) {
      Validate.notNull(var0, "threadName");
      Validate.notNull(var1, "threadGroupName");
      Collection var2 = findThreadGroups(new ThreadUtils.NamePredicate(var1));
      if (var2.isEmpty()) {
         return Collections.emptyList();
      } else {
         ArrayList var3 = new ArrayList();
         ThreadUtils.NamePredicate var4 = new ThreadUtils.NamePredicate(var0);
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            ThreadGroup var6 = (ThreadGroup)var5.next();
            var3.addAll(findThreads(var6, false, var4));
         }

         return Collections.unmodifiableCollection(var3);
      }
   }

   public static Collection<Thread> findThreadsByName(String var0, ThreadGroup var1) {
      return findThreads(var1, false, new ThreadUtils.NamePredicate(var0));
   }

   public static Collection<ThreadGroup> getAllThreadGroups() {
      return findThreadGroups(ALWAYS_TRUE_PREDICATE);
   }

   public static Collection<Thread> getAllThreads() {
      return findThreads(ALWAYS_TRUE_PREDICATE);
   }

   public static ThreadGroup getSystemThreadGroup() {
      ThreadGroup var0;
      for(var0 = Thread.currentThread().getThreadGroup(); var0.getParent() != null; var0 = var0.getParent()) {
      }

      return var0;
   }

   public static void join(Thread var0, Duration var1) {
      DurationUtils.accept(var0::join, var1);
   }

   public static void sleep(Duration var0) {
      DurationUtils.accept(Thread::sleep, var0);
   }

   @FunctionalInterface
   public interface ThreadPredicate {
      boolean test(Thread var1);
   }

   public static class ThreadIdPredicate implements ThreadUtils.ThreadPredicate {
      private final long threadId;

      public ThreadIdPredicate(long var1) {
         if (var1 <= 0L) {
            throw new IllegalArgumentException("The thread id must be greater than zero");
         } else {
            this.threadId = var1;
         }
      }

      public boolean test(Thread var1) {
         return var1 != null && var1.getId() == this.threadId;
      }
   }

   @FunctionalInterface
   public interface ThreadGroupPredicate {
      boolean test(ThreadGroup var1);
   }

   public static class NamePredicate implements ThreadUtils.ThreadPredicate, ThreadUtils.ThreadGroupPredicate {
      private final String name;

      public NamePredicate(String var1) {
         Validate.notNull(var1, "name");
         this.name = var1;
      }

      public boolean test(Thread var1) {
         return var1 != null && var1.getName().equals(this.name);
      }

      public boolean test(ThreadGroup var1) {
         return var1 != null && var1.getName().equals(this.name);
      }
   }

   private static final class AlwaysTruePredicate implements ThreadUtils.ThreadPredicate, ThreadUtils.ThreadGroupPredicate {
      private AlwaysTruePredicate() {
      }

      public boolean test(Thread var1) {
         return true;
      }

      public boolean test(ThreadGroup var1) {
         return true;
      }

      // $FF: synthetic method
      AlwaysTruePredicate(Object var1) {
         this();
      }
   }
}
