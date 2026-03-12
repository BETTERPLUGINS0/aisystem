package ac.vulcan.anticheat;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Vulcan_Xc {
   private static List Vulcan_u;
   private static Set Vulcan_Y;
   private static final Map Vulcan_i;
   private static final Map Vulcan_D;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(1380845896083694781L, -677261290594254191L, (Object)null).a(108733415469872L);
   private static final String[] b;

   public static Locale Vulcan_V(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_S(Object[] var0) {
      long var2 = (Long)var0[0];
      Locale var1 = (Locale)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 59504333532590L;
      return Vulcan_t(new Object[]{new Long(var4), var1, var1});
   }

   public static List Vulcan_t(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_j(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static synchronized void Vulcan_d(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Set Vulcan_h(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static synchronized void Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_N(Object[] var0) {
      Locale var3 = (Locale)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 38168168552483L;
      return Vulcan_j(new Object[]{new Long(var4)}).contains(var3);
   }

   public static List Vulcan_w(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_m(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = "\u0006U`M;?3oWyO6:2o]y^:7#u\u001b\u0017\u0006U`M;?3oWyO6:2o]y^:7#u\u001b";
      int var4 = "\u0006U`M;?3oWyO6:2o]y^:7#u\u001b\u0017\u0006U`M;?3oWyO6:2o]y^:7#u\u001b".length();
      char var1 = 23;
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 85;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 85;
               var10005 = var7;
            } else {
               var10 = 85;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 85;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 26;
                  break;
               case 1:
                  var23 = 110;
                  break;
               case 2:
                  var23 = 67;
                  break;
               case 3:
                  var23 = 121;
                  break;
               case 4:
                  var23 = 2;
                  break;
               case 5:
                  var23 = 3;
                  break;
               default:
                  var23 = 2;
               }

               var10004[var10005] = (char)(var22 ^ var12 ^ var23);
               ++var7;
               if (var10 == 0) {
                  var10005 = var10;
                  var10004 = var10001;
                  var12 = var10;
               } else {
                  if (var8 <= var7) {
                     break;
                  }

                  var10004 = var10001;
                  var12 = var10;
                  var10005 = var7;
               }
            }
         }

         var5[var3++] = (new String(var10001)).intern();
         if ((var0 += var1) >= var4) {
            b = var5;
            Vulcan_i = Collections.synchronizedMap(new HashMap());
            Vulcan_D = Collections.synchronizedMap(new HashMap());
            return;
         }

         var1 = var2.charAt(var0);
      }
   }

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
