package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.LinkedList;
import me.frep.vulcan.spigot.check.AbstractCheck;

public final class Vulcan_c extends LinkedList {
   private final int Vulcan_F;
   private static AbstractCheck[] Vulcan_V;
   private static final long a = Vulcan_n.a(5953869985291768399L, -8136884721259330536L, MethodHandles.lookup().lookupClass()).a(48719333635763L);

   public Vulcan_c(int var1) {
      this.Vulcan_F = var1;
   }

   public Vulcan_c(Collection var1, int var2) {
      super(var1);
      this.Vulcan_F = var2;
   }

   public boolean add(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_L(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_U(Object[] var1) {
      return this.Vulcan_F;
   }

   public static void Vulcan_U(AbstractCheck[] var0) {
      Vulcan_V = var0;
   }

   public static AbstractCheck[] Vulcan_U() {
      return Vulcan_V;
   }

   static {
      if (Vulcan_U() != null) {
         Vulcan_U(new AbstractCheck[4]);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
