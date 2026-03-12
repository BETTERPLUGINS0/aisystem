package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import me.frep.vulcan.spigot.check.AbstractCheck;

public class Vulcan_U {
   private final int Vulcan_s;
   private final long Vulcan_P;
   private final List Vulcan_l;
   private static int Vulcan_f;
   private static final long a = Vulcan_n.a(7873027602759509108L, 2422789593872933583L, MethodHandles.lookup().lookupClass()).a(204453669197727L);

   public Vulcan_U(int var1, long var2) {
      long var4 = a ^ 111657489370672L;
      int var10000 = Vulcan_z();
      super();
      this.Vulcan_s = var1;
      this.Vulcan_P = var2;
      int var6 = var10000;
      this.Vulcan_l = new ArrayList();
      if (var6 != 0) {
         int var7 = AbstractCheck.Vulcan_V();
         ++var7;
         AbstractCheck.Vulcan_H(var7);
      }

   }

   public boolean Vulcan_o(Runnable var1) {
      long var2 = a ^ 125446323264057L;
      int var10000 = Vulcan_c();
      this.Vulcan_l.add(var1);
      int var4 = var10000;

      try {
         if (AbstractCheck.Vulcan_V() == 0) {
            ++var4;
            Vulcan_F(var4);
         }

         return true;
      } catch (RuntimeException var5) {
         throw a(var5);
      }
   }

   public void Vulcan_v(Object[] var1) {
      this.Vulcan_l.forEach(Runnable::run);
   }

   public int Vulcan_S(Object[] var1) {
      return this.Vulcan_s;
   }

   public long Vulcan_X(Object[] var1) {
      return this.Vulcan_P;
   }

   public List Vulcan_p(Object[] var1) {
      return this.Vulcan_l;
   }

   public static void Vulcan_F(int var0) {
      Vulcan_f = var0;
   }

   public static int Vulcan_c() {
      return Vulcan_f;
   }

   public static int Vulcan_z() {
      int var0 = Vulcan_c();

      try {
         return var0 == 0 ? 6 : 0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   static {
      if (Vulcan_z() != 0) {
         Vulcan_F(85);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
