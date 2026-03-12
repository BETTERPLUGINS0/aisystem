package org.apache.commons.lang3.tuple;

import java.util.Map.Entry;

public class MutablePair<L, R> extends Pair<L, R> {
   public static final MutablePair<?, ?>[] EMPTY_ARRAY = new MutablePair[0];
   private static final long serialVersionUID = 4954918890077093841L;
   public L left;
   public R right;

   public static <L, R> MutablePair<L, R>[] emptyArray() {
      return (MutablePair[])EMPTY_ARRAY;
   }

   public static <L, R> MutablePair<L, R> of(L var0, R var1) {
      return new MutablePair(var0, var1);
   }

   public static <L, R> MutablePair<L, R> of(Entry<L, R> var0) {
      Object var1;
      Object var2;
      if (var0 != null) {
         var1 = var0.getKey();
         var2 = var0.getValue();
      } else {
         var1 = null;
         var2 = null;
      }

      return new MutablePair(var1, var2);
   }

   public MutablePair() {
   }

   public MutablePair(L var1, R var2) {
      this.left = var1;
      this.right = var2;
   }

   public L getLeft() {
      return this.left;
   }

   public R getRight() {
      return this.right;
   }

   public void setLeft(L var1) {
      this.left = var1;
   }

   public void setRight(R var1) {
      this.right = var1;
   }

   public R setValue(R var1) {
      Object var2 = this.getRight();
      this.setRight(var1);
      return var2;
   }
}
