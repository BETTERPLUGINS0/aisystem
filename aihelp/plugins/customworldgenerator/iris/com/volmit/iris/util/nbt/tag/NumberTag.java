package com.volmit.iris.util.nbt.tag;

public abstract class NumberTag<T extends Number & Comparable<T>> extends Tag<T> {
   public NumberTag(T value) {
      super(var1);
   }

   public byte asByte() {
      return ((Number)this.getValue()).byteValue();
   }

   public short asShort() {
      return ((Number)this.getValue()).shortValue();
   }

   public int asInt() {
      return ((Number)this.getValue()).intValue();
   }

   public long asLong() {
      return ((Number)this.getValue()).longValue();
   }

   public float asFloat() {
      return ((Number)this.getValue()).floatValue();
   }

   public double asDouble() {
      return ((Number)this.getValue()).doubleValue();
   }

   public String valueToString(int maxDepth) {
      return ((Number)this.getValue()).toString();
   }
}
