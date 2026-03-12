package ac.vulcan.anticheat;

import java.io.Serializable;
import java.util.Iterator;

public final class Vulcan_XX implements Serializable {
   private static final long serialVersionUID = 8270183163158333422L;
   private final char Vulcan_m;
   private final char Vulcan_y;
   private final boolean Vulcan_j;
   private transient String Vulcan_s;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(3747323723078082979L, 1160802290977761247L, (Object)null).a(227562379939917L);
   private static final String b;

   public static Vulcan_XX Vulcan_M(Object[] var0) {
      int var1 = (Integer)var0[0];
      return new Vulcan_XX((char)var1, (char)var1, false);
   }

   public static Vulcan_XX Vulcan_H(Object[] var0) {
      int var1 = (Integer)var0[0];
      return new Vulcan_XX((char)var1, (char)var1, true);
   }

   public static Vulcan_XX Vulcan_R(Object[] var0) {
      int var2 = (Integer)var0[0];
      int var1 = (Integer)var0[1];
      return new Vulcan_XX((char)var2, (char)var1, false);
   }

   public static Vulcan_XX Vulcan_v(Object[] var0) {
      int var2 = (Integer)var0[0];
      int var1 = (Integer)var0[1];
      return new Vulcan_XX((char)var2, (char)var1, true);
   }

   public Vulcan_XX(char var1) {
      this(var1, var1, false);
   }

   public Vulcan_XX(char var1, boolean var2) {
      this(var1, var1, var2);
   }

   public Vulcan_XX(char var1, char var2) {
      this(var1, var2, false);
   }

   public Vulcan_XX(char var1, char var2, boolean var3) {
      long var4 = a ^ 123422985007211L;
      super();
      if (var1 > var2) {
         char var6 = var1;
         var1 = var2;
         var2 = var6;
      }

      this.Vulcan_m = var1;
      this.Vulcan_y = var2;
      this.Vulcan_j = var3;
   }

   public char Vulcan_p(Object[] var1) {
      return this.Vulcan_m;
   }

   public char Vulcan_R(Object[] var1) {
      return this.Vulcan_y;
   }

   public boolean Vulcan_m(Object[] var1) {
      return this.Vulcan_j;
   }

   public boolean Vulcan_C(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_U(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      long var1 = a ^ 17730117195952L;
      String[] var3 = Vulcan_XL.Vulcan_v();

      int var10000;
      byte var10001;
      label32: {
         try {
            var10000 = 83 + this.Vulcan_m + 7 * this.Vulcan_y;
            var10001 = this.Vulcan_j;
            if (var3 == null) {
               return var10000 + var10001;
            }

            if (var10001 != 0) {
               break label32;
            }
         } catch (IllegalArgumentException var4) {
            throw a(var4);
         }

         var10001 = 0;
         return var10000 + var10001;
      }

      var10001 = 1;
      return var10000 + var10001;
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }

   public Iterator Vulcan_j(Object[] var1) {
      return new Vulcan_g(this, (Vulcan_XI)null);
   }

   static boolean Vulcan_L(Object[] var0) {
      Vulcan_XX var1 = (Vulcan_XX)var0[0];
      return var1.Vulcan_j;
   }

   static char Vulcan_s(Object[] var0) {
      Vulcan_XX var1 = (Vulcan_XX)var0[0];
      return var1.Vulcan_m;
   }

   static char Vulcan__(Object[] var0) {
      Vulcan_XX var1 = (Vulcan_XX)var0[0];
      return var1.Vulcan_y;
   }

   static {
      char[] var10002 = "zfEe^:MIk\u0000(y(W\u000e`O1,9F\u000e`U)`".toCharArray();
      int var10003 = var10002.length;
      int var1 = 0;
      byte var13 = 127;
      char[] var10001 = var10002;
      int var11 = var10003;
      char[] var10004;
      int var10005;
      byte var14;
      if (var10003 <= 1) {
         var10004 = var10002;
         var14 = 127;
         var10005 = var1;
      } else {
         var13 = 127;
         var11 = var10003;
         if (var10003 <= var1) {
            b = (new String(var10002)).intern();
            return;
         }

         var10004 = var10002;
         var14 = 127;
         var10005 = var1;
      }

      while(true) {
         char var9 = var10004[var10005];
         byte var10;
         switch(var1 % 7) {
         case 0:
            var10 = 81;
            break;
         case 1:
            var10 = 113;
            break;
         case 2:
            var10 = 95;
            break;
         case 3:
            var10 = 58;
            break;
         case 4:
            var10 = 115;
            break;
         case 5:
            var10 = 36;
            break;
         default:
            var10 = 92;
         }

         var10004[var10005] = (char)(var9 ^ var14 ^ var10);
         ++var1;
         if (var13 == 0) {
            var10005 = var13;
            var10004 = var10001;
            var14 = var13;
         } else {
            if (var11 <= var1) {
               b = (new String(var10001)).intern();
               return;
            }

            var10004 = var10001;
            var14 = var13;
            var10005 = var1;
         }
      }
   }

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
