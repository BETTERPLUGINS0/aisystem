package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.function.Function;
import me.frep.vulcan.spigot.check.AbstractCheck;

public class Vulcan_iB {
   private final Vulcan_iE Vulcan_V;
   private static String[] Vulcan__;
   private static final long a = Vulcan_n.a(3714901004979409675L, -6915771323833203296L, MethodHandles.lookup().lookupClass()).a(230013543781259L);

   public boolean Vulcan_c(Object[] var1) {
      long var3 = (Long)var1[0];
      Vulcan_in var2 = (Vulcan_in)var1[1];
      long var10000 = a ^ var3;
      String[] var5 = Vulcan_w();

      try {
         boolean var7 = (Boolean)var2.Vulcan_I().apply(this.Vulcan_V);
         if (AbstractCheck.Vulcan_m() != 0) {
            Vulcan_X(new String[4]);
         }

         return var7;
      } catch (RuntimeException var6) {
         throw a(var6);
      }
   }

   public boolean Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_M(Object[] var1) {
      Function var2 = (Function)var1[0];
      return (Boolean)var2.apply(this.Vulcan_V);
   }

   public Vulcan_iB(Vulcan_iE var1) {
      this.Vulcan_V = var1;
   }

   public static void Vulcan_X(String[] var0) {
      Vulcan__ = var0;
   }

   public static String[] Vulcan_w() {
      return Vulcan__;
   }

   static {
      if (Vulcan_w() == null) {
         Vulcan_X(new String[5]);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
