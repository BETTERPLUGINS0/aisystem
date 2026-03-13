package com.volmit.iris.util.cache;

public class UByteCache extends ByteCache {
   public UByteCache(int width, int height) {
      super(var1, var2);
   }

   public void set(int i, Integer v) {
      super.set(var1, var2 + -128);
   }

   public Integer get(int i) {
      return super.get(var1) - -128;
   }

   public void iset(int i, int v) {
      this.set(var1, var2);
   }
}
