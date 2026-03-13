package com.volmit.iris.util.scheduling;

public class Wrapper<T> {
   private T t;

   public Wrapper(T t) {
      this.set(var1);
   }

   public void set(T t) {
      this.t = var1;
   }

   public T get() {
      return this.t;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = 31 * var2 + (this.t == null ? 0 : this.t.hashCode());
      return var3;
   }

   public boolean equals(Object obj) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (var1 instanceof Wrapper) {
         Wrapper var2 = (Wrapper)var1;
         if (this.t == null) {
            return var2.t == null;
         } else {
            return this.t.equals(var2.t);
         }
      } else {
         return false;
      }
   }

   public String toString() {
      return this.t != null ? this.get().toString() : super.toString() + " (null)";
   }
}
