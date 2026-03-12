package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Vulcan_bn {
   Vector Vulcan_O;
   Vector Vulcan_F;
   private static String Vulcan_X;
   private static final long a = Vulcan_n.a(-3997200862286130784L, 6196126412076958962L, MethodHandles.lookup().lookupClass()).a(233308537903641L);

   public Vulcan_bn(Vector var1, Vector var2) {
      this.Vulcan_O = var1;
      this.Vulcan_F = var2;
   }

   public static boolean Vulcan_j(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public Vector Vulcan_G(Object[] var1) {
      double var2 = (Double)var1[0];
      return this.Vulcan_O.clone().add(this.Vulcan_F.clone().multiply(var2));
   }

   public boolean Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public List Vulcan_Z(Object[] var1) {
      double var2 = (Double)var1[0];
      long var4 = (Long)var1[1];
      double var6 = (Double)var1[2];
      var4 ^= a;
      String var10000 = Vulcan_s();
      ArrayList var9 = new ArrayList();
      double var10 = 0.0D;
      String var8 = var10000;

      ArrayList var12;
      label25:
      while(true) {
         if (var10 <= var2) {
            var12 = var9;
            if (var8 == null) {
               break;
            }

            var9.add(this.Vulcan_G(new Object[]{var10}));
            var10 += var6;
            if (var8 != null) {
               continue;
            }
         }

         while(var4 < 0L) {
            if (var8 != null) {
               continue label25;
            }
         }

         var12 = var9;
         break;
      }

      return var12;
   }

   public List Vulcan_G(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      long var6 = (Long)var1[2];
      double var8 = (Double)var1[3];
      var6 ^= a;
      String var10000 = Vulcan_s();
      ArrayList var11 = new ArrayList();
      double var12 = var2;
      String var10 = var10000;

      ArrayList var14;
      label25:
      while(true) {
         if (var12 <= var4) {
            var14 = var11;
            if (var10 == null) {
               break;
            }

            var11.add(this.Vulcan_G(new Object[]{var12}));
            var12 += var8;
            if (var10 != null) {
               continue;
            }
         }

         while(var6 < 0L) {
            if (var10 != null) {
               continue label25;
            }
         }

         var14 = var11;
         break;
      }

      return var14;
   }

   public List Vulcan_y(Object[] var1) {
      World var2 = (World)var1[0];
      double var3 = (Double)var1[1];
      long var5 = (Long)var1[2];
      double var7 = (Double)var1[3];
      var5 ^= a;
      long var9 = var5 ^ 85177093691639L;
      Vulcan_s();
      ArrayList var12 = new ArrayList();
      this.Vulcan_Z(new Object[]{var3, var9, var7}).stream().filter(Vulcan_bn::lambda$getBlocks$0).forEach(Vulcan_bn::lambda$getBlocks$1);

      try {
         if (AbstractCheck.Vulcan_V() == 0) {
            Vulcan_P("FDZoAb");
         }

         return var12;
      } catch (RuntimeException var13) {
         throw a(var13);
      }
   }

   public Vector Vulcan_o(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_f(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_a(Object[] var1) {
      World var2 = (World)var1[0];
      double var3 = (Double)var1[1];
      double var5 = (Double)var1[2];
      long var7 = (Long)var1[3];
      var7 ^= a;
      long var9 = var7 ^ 42248233675374L;
      String var10000 = Vulcan_s();
      Iterator var12 = this.Vulcan_Z(new Object[]{var3, var9, var5}).iterator();
      String var11 = var10000;

      while(var12.hasNext()) {
         Vector var13 = (Vector)var12.next();
         var2.playEffect(var13.toLocation(var2), Effect.ENDEREYE_LAUNCH, 5);
         if (var11 == null) {
            break;
         }
      }

   }

   private static void lambda$getBlocks$1(List var0, World var1, Vector var2) {
      var0.add(var2.toLocation(var1).getBlock());
   }

   private static boolean lambda$getBlocks$0(World var0, Vector var1) {
      return var1.toLocation(var0).getBlock().getType().isSolid();
   }

   public static void Vulcan_P(String var0) {
      Vulcan_X = var0;
   }

   public static String Vulcan_s() {
      return Vulcan_X;
   }

   static {
      if (Vulcan_s() == null) {
         Vulcan_P("WBD9Lc");
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
