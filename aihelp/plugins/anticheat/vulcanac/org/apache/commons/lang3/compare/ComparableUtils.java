package org.apache.commons.lang3.compare;

import java.util.function.Predicate;

public class ComparableUtils {
   public static <A extends Comparable<A>> Predicate<A> between(A var0, A var1) {
      return (var2) -> {
         return is(var2).between(var0, var1);
      };
   }

   public static <A extends Comparable<A>> Predicate<A> betweenExclusive(A var0, A var1) {
      return (var2) -> {
         return is(var2).betweenExclusive(var0, var1);
      };
   }

   public static <A extends Comparable<A>> Predicate<A> ge(A var0) {
      return (var1) -> {
         return is(var1).greaterThanOrEqualTo(var0);
      };
   }

   public static <A extends Comparable<A>> Predicate<A> gt(A var0) {
      return (var1) -> {
         return is(var1).greaterThan(var0);
      };
   }

   public static <A extends Comparable<A>> ComparableUtils.ComparableCheckBuilder<A> is(A var0) {
      return new ComparableUtils.ComparableCheckBuilder(var0);
   }

   public static <A extends Comparable<A>> Predicate<A> le(A var0) {
      return (var1) -> {
         return is(var1).lessThanOrEqualTo(var0);
      };
   }

   public static <A extends Comparable<A>> Predicate<A> lt(A var0) {
      return (var1) -> {
         return is(var1).lessThan(var0);
      };
   }

   private ComparableUtils() {
   }

   public static class ComparableCheckBuilder<A extends Comparable<A>> {
      private final A a;

      private ComparableCheckBuilder(A var1) {
         this.a = var1;
      }

      public boolean between(A var1, A var2) {
         return this.betweenOrdered(var1, var2) || this.betweenOrdered(var2, var1);
      }

      public boolean betweenExclusive(A var1, A var2) {
         return this.betweenOrderedExclusive(var1, var2) || this.betweenOrderedExclusive(var2, var1);
      }

      private boolean betweenOrdered(A var1, A var2) {
         return this.greaterThanOrEqualTo(var1) && this.lessThanOrEqualTo(var2);
      }

      private boolean betweenOrderedExclusive(A var1, A var2) {
         return this.greaterThan(var1) && this.lessThan(var2);
      }

      public boolean equalTo(A var1) {
         return this.a.compareTo(var1) == 0;
      }

      public boolean greaterThan(A var1) {
         return this.a.compareTo(var1) > 0;
      }

      public boolean greaterThanOrEqualTo(A var1) {
         return this.a.compareTo(var1) >= 0;
      }

      public boolean lessThan(A var1) {
         return this.a.compareTo(var1) < 0;
      }

      public boolean lessThanOrEqualTo(A var1) {
         return this.a.compareTo(var1) <= 0;
      }

      // $FF: synthetic method
      ComparableCheckBuilder(Comparable var1, Object var2) {
         this(var1);
      }
   }
}
