package org.apache.commons.lang3.tuple;

import java.util.Map.Entry;

public final class ImmutablePair<L, R> extends Pair<L, R> {
   public static final ImmutablePair<?, ?>[] EMPTY_ARRAY = new ImmutablePair[0];
   private static final ImmutablePair NULL = of((Object)null, (Object)null);
   private static final long serialVersionUID = 4954918890077093841L;
   public final L left;
   public final R right;

   public static <L, R> ImmutablePair<L, R>[] emptyArray() {
      return (ImmutablePair[])EMPTY_ARRAY;
   }

   public static <L, R> Pair<L, R> left(L var0) {
      return of(var0, (Object)null);
   }

   public static <L, R> ImmutablePair<L, R> nullPair() {
      return NULL;
   }

   public static <L, R> ImmutablePair<L, R> of(L var0, R var1) {
      return new ImmutablePair(var0, var1);
   }

   public static <L, R> ImmutablePair<L, R> of(Entry<L, R> var0) {
      Object var1;
      Object var2;
      if (var0 != null) {
         var1 = var0.getKey();
         var2 = var0.getValue();
      } else {
         var1 = null;
         var2 = null;
      }

      return new ImmutablePair(var1, var2);
   }

   public static <L, R> Pair<L, R> right(R var0) {
      return of((Object)null, var0);
   }

   public ImmutablePair(L var1, R var2) {
      this.left = var1;
      this.right = var2;
   }

   public L getLeft() {
      return this.left;
   }

   public R getRight() {
      return this.right;
   }

   public R setValue(R var1) {
      throw new UnsupportedOperationException();
   }
}
