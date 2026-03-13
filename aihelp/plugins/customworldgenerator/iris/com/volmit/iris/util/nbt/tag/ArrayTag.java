package com.volmit.iris.util.nbt.tag;

import java.lang.reflect.Array;

public abstract class ArrayTag<T> extends Tag<T> {
   public ArrayTag(T value) {
      super(var1);
      if (!var1.getClass().isArray()) {
         throw new UnsupportedOperationException("type of array tag must be an array");
      }
   }

   public int length() {
      return Array.getLength(this.getValue());
   }

   public T getValue() {
      return super.getValue();
   }

   public void setValue(T value) {
      super.setValue(var1);
   }

   public String valueToString(int maxDepth) {
      return this.arrayToString("", "");
   }

   protected String arrayToString(String prefix, String suffix) {
      StringBuilder var3 = (new StringBuilder("[")).append(var1).append("".equals(var1) ? "" : ";");

      for(int var4 = 0; var4 < this.length(); ++var4) {
         var3.append(var4 == 0 ? "" : ",").append(Array.get(this.getValue(), var4)).append(var2);
      }

      var3.append("]");
      return var3.toString();
   }
}
