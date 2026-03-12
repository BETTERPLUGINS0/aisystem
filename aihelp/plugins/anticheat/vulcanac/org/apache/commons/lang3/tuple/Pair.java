package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import java.util.Objects;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Pair<L, R> implements Entry<L, R>, Comparable<Pair<L, R>>, Serializable {
   private static final long serialVersionUID = 4954918890077093841L;
   public static final Pair<?, ?>[] EMPTY_ARRAY = new Pair.PairAdapter[0];

   public static <L, R> Pair<L, R>[] emptyArray() {
      return (Pair[])EMPTY_ARRAY;
   }

   public static <L, R> Pair<L, R> of(L var0, R var1) {
      return ImmutablePair.of(var0, var1);
   }

   public static <L, R> Pair<L, R> of(Entry<L, R> var0) {
      return ImmutablePair.of(var0);
   }

   public int compareTo(Pair<L, R> var1) {
      return (new CompareToBuilder()).append(this.getLeft(), var1.getLeft()).append(this.getRight(), var1.getRight()).toComparison();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Entry)) {
         return false;
      } else {
         Entry var2 = (Entry)var1;
         return Objects.equals(this.getKey(), var2.getKey()) && Objects.equals(this.getValue(), var2.getValue());
      }
   }

   public final L getKey() {
      return this.getLeft();
   }

   public abstract L getLeft();

   public abstract R getRight();

   public R getValue() {
      return this.getRight();
   }

   public int hashCode() {
      return Objects.hashCode(this.getKey()) ^ Objects.hashCode(this.getValue());
   }

   public String toString() {
      return "(" + this.getLeft() + ',' + this.getRight() + ')';
   }

   public String toString(String var1) {
      return String.format(var1, this.getLeft(), this.getRight());
   }

   private static final class PairAdapter<L, R> extends Pair<L, R> {
      private static final long serialVersionUID = 1L;

      public L getLeft() {
         return null;
      }

      public R getRight() {
         return null;
      }

      public R setValue(R var1) {
         return null;
      }
   }
}
