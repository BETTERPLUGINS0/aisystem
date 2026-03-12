package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import me.frep.vulcan.spigot.check.AbstractCheck;

public final class Vulcan_ir extends HashMap {
   private final int Vulcan_L;
   private final Deque Vulcan_q = new LinkedList();
   private static final long a = Vulcan_n.a(1891582563634698137L, -3136742184787024561L, MethodHandles.lookup().lookupClass()).a(173874300089392L);

   public boolean remove(Object var1, Object var2) {
      this.Vulcan_q.remove(var1);
      return super.remove(var1, var2);
   }

   public Object putIfAbsent(Object param1, Object param2) {
      // $FF: Couldn't be decompiled
   }

   public Object put(Object var1, Object var2) {
      long var3 = a ^ 12857362014976L;
      long var5 = var3 ^ 20830014165247L;
      this.Vulcan_m(new Object[]{var5});
      this.Vulcan_q.addLast(var1);
      return super.put(var1, var2);
   }

   public void putAll(Map var1) {
      var1.forEach(this::put);
   }

   public void clear() {
      this.Vulcan_q.clear();
      super.clear();
   }

   public Object remove(Object var1) {
      this.Vulcan_q.remove(var1);
      return super.remove(var1);
   }

   private boolean Vulcan_m(Object[] var1) {
      long var2 = (Long)var1[0];
      long var10000 = a ^ var2;
      AbstractCheck[] var4 = Vulcan_c.Vulcan_U();

      label28: {
         int var7;
         try {
            var7 = this.Vulcan_q.size();
            if (var4 != null) {
               return (boolean)var7;
            }

            if (var7 >= this.Vulcan_L) {
               break label28;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var7 = 0;
         return (boolean)var7;
      }

      Object var5 = this.Vulcan_q.removeFirst();
      this.remove(var5);
      return true;
   }

   public Vulcan_ir(int var1) {
      this.Vulcan_L = var1;
   }

   public int Vulcan_z(Object[] var1) {
      return this.Vulcan_L;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
