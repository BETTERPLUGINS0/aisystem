package com.nisovin.shopkeepers.util.java;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class MutableLong implements Comparable<Number> {
   private long value;

   public MutableLong() {
   }

   public MutableLong(long value) {
      this.value = value;
   }

   public long getValue() {
      return this.value;
   }

   public void setValue(long value) {
      this.value = value;
   }

   public void increment(long amount) {
      this.value += amount;
   }

   public void decrement(long amount) {
      this.value -= amount;
   }

   public int compareTo(Number o) {
      return Long.compare(this.value, o.longValue());
   }

   public int hashCode() {
      return Long.hashCode(this.value);
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof MutableLong)) {
         return false;
      } else {
         MutableLong other = (MutableLong)obj;
         return this.value == other.value;
      }
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("MutableLong [value=");
      builder.append(this.value);
      builder.append("]");
      return builder.toString();
   }
}
