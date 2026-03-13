package com.volmit.iris.util.collection;

import java.util.Iterator;

public class StateList {
   private final KList<String> states;

   public StateList(String... states) {
      this.states = new KList(var1);
      if (this.getBits() > 64) {
         throw new RuntimeException("StateLists cannot exceed 64 bits! You are trying to use " + this.getBits() + " bits!");
      }
   }

   public StateList(Enum<?>... states) {
      this.states = (new KList()).convert(Enum::name);
      if (this.getBits() > 64) {
         throw new RuntimeException("StateLists cannot exceed 64 bits! You are trying to use " + this.getBits() + " bits!");
      }
   }

   public long max() {
      return (long)(Math.pow(2.0D, (double)this.getBits()) - 1.0D);
   }

   public KList<String> getEnabled(long list) {
      KList var3 = new KList();
      Iterator var4 = this.states.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (this.is(var1, var5)) {
            var3.add((Object)var5);
         }
      }

      return var3;
   }

   public long of(String... enabledStates) {
      long var2 = 0L;
      String[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         var2 |= this.getBit(var7);
      }

      return var2;
   }

   public long set(long list, String state, boolean enabled) {
      long var5 = this.getBit(var3);
      boolean var7 = this.is(var1, var3);
      if (var4 && !var7) {
         return var1 | var5;
      } else {
         return !var4 && var7 ? var1 ^ var5 : var1;
      }
   }

   public boolean is(long list, String state) {
      long var4 = this.getBit(var3);
      return var4 > 0L && (var1 & var4) == var4;
   }

   public boolean hasBit(String state) {
      return this.getBit(var1) > 0L;
   }

   public long getBit(String state) {
      return this.getBit(this.states.indexOf(var1));
   }

   public long getBit(int index) {
      return (long)(var1 < 0 ? -1.0D : Math.pow(2.0D, (double)var1));
   }

   public int getBytes() {
      return this.getBits() == 0 ? 0 : (this.getBits() >> 2) + 1;
   }

   public int getBits() {
      return this.states.size();
   }
}
