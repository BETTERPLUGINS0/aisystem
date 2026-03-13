package com.volmit.iris.core.nms.container;

import lombok.Generated;

public class Pair<A, B> {
   private A a;
   private B b;

   @Generated
   public A getA() {
      return this.a;
   }

   @Generated
   public B getB() {
      return this.b;
   }

   @Generated
   public void setA(final A a) {
      this.a = var1;
   }

   @Generated
   public void setB(final B b) {
      this.b = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Pair)) {
         return false;
      } else {
         Pair var2 = (Pair)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            Object var3 = this.getA();
            Object var4 = var2.getA();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            Object var5 = this.getB();
            Object var6 = var2.getB();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof Pair;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Object var3 = this.getA();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      Object var4 = this.getB();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getA());
      return "Pair(a=" + var10000 + ", b=" + String.valueOf(this.getB()) + ")";
   }

   @Generated
   public Pair(final A a, final B b) {
      this.a = var1;
      this.b = var2;
   }

   @Generated
   public Pair() {
   }
}
