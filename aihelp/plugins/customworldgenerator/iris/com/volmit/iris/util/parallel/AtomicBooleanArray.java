package com.volmit.iris.util.parallel;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class AtomicBooleanArray implements Serializable {
   private static final VarHandle AA = MethodHandles.arrayElementVarHandle(boolean[].class);
   private final boolean[] array;

   public AtomicBooleanArray(int length) {
      this.array = new boolean[var1];
   }

   public final int length() {
      return this.array.length;
   }

   public final boolean get(int index) {
      return AA.getVolatile(this.array, var1);
   }

   public final void set(int index, boolean newValue) {
      AA.setVolatile(this.array, var1, var2);
   }

   public final boolean getAndSet(int index, boolean newValue) {
      return AA.getAndSet(this.array, var1, var2);
   }

   public final boolean compareAndSet(int index, boolean expectedValue, boolean newValue) {
      return AA.compareAndSet(this.array, var1, var2, var3);
   }

   public String toString() {
      int var1 = this.array.length - 1;
      if (var1 == -1) {
         return "[]";
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append('[');
         int var3 = 0;

         while(true) {
            var2.append(this.get(var3));
            if (var3 == var1) {
               return var2.append(']').toString();
            }

            var2.append(',').append(' ');
            ++var3;
         }
      }
   }
}
