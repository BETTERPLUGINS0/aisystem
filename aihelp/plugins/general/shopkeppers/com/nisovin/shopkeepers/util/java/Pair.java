package com.nisovin.shopkeepers.util.java;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Pair<V1, V2> {
   private final V1 first;
   private final V2 second;

   public static <V1, V2> Pair<V1, V2> of(V1 first, V2 second) {
      return new Pair(first, second);
   }

   @SafeVarargs
   public static <V1, V2> Map<V1, V2> toMap(Pair<V1, V2>... pairs) {
      Map<V1, V2> map = new LinkedHashMap();
      Pair[] var2 = pairs;
      int var3 = pairs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pair<V1, V2> pair = var2[var4];
         map.put(pair.getFirst(), pair.getSecond());
      }

      return map;
   }

   protected Pair(V1 first, V2 second) {
      this.first = first;
      this.second = second;
   }

   public V1 getFirst() {
      return this.first;
   }

   public V2 getSecond() {
      return this.second;
   }

   public boolean equals(@Nullable Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Pair)) {
         return false;
      } else {
         Pair<?, ?> other = (Pair)obj;
         if (!Objects.equals(this.first, other.getFirst())) {
            return false;
         } else {
            return Objects.equals(this.second, other.getSecond());
         }
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.first) + 17 * Objects.hashCode(this.second);
   }

   public String toString() {
      return "" + '(' + this.first + ',' + this.second + ')';
   }
}
