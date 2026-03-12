package ac.vulcan.anticheat;

import java.util.Random;

public class Vulcan_et {
   private static final Random Vulcan_T;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(2206374771989513355L, 5127494921081209004L, (Object)null).a(277868695899921L);
   private static final String[] b;

   public static String Vulcan_Z(Object[] var0) {
      long var1 = (Long)var0[0];
      int var3 = (Integer)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 81748039467103L;
      return Vulcan_N(new Object[]{new Integer(var3), new Boolean(false), new Long(var4), new Boolean(false)});
   }

   public static String Vulcan__(Object[] var0) {
      int var3 = (Integer)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 16959776934644L;
      return Vulcan_b(new Object[]{new Long(var4), new Integer(var3), new Integer(32), new Integer(127), new Boolean(false), new Boolean(false)});
   }

   public static String Vulcan_d(Object[] var0) {
      int var1 = (Integer)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 73455953907002L;
      return Vulcan_N(new Object[]{new Integer(var1), new Boolean(true), new Long(var4), new Boolean(false)});
   }

   public static String Vulcan_j(Object[] var0) {
      int var3 = (Integer)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 34985794930791L;
      return Vulcan_N(new Object[]{new Integer(var3), new Boolean(true), new Long(var4), new Boolean(true)});
   }

   public static String Vulcan_R(Object[] var0) {
      int var3 = (Integer)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 31835471707152L;
      return Vulcan_N(new Object[]{new Integer(var3), new Boolean(false), new Long(var4), new Boolean(true)});
   }

   public static String Vulcan_N(Object[] var0) {
      int var5 = (Integer)var0[0];
      boolean var1 = (Boolean)var0[1];
      long var2 = (Long)var0[2];
      boolean var4 = (Boolean)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 4514987416619L;
      return Vulcan_b(new Object[]{new Long(var6), new Integer(var5), new Integer(0), new Integer(0), new Boolean(var1), new Boolean(var4)});
   }

   public static String Vulcan_b(Object[] var0) {
      long var6 = (Long)var0[0];
      int var4 = (Integer)var0[1];
      int var2 = (Integer)var0[2];
      int var5 = (Integer)var0[3];
      boolean var1 = (Boolean)var0[4];
      boolean var3 = (Boolean)var0[5];
      var6 ^= a;
      long var8 = var6 ^ 92967653434507L;
      Random var10007 = Vulcan_T;
      return Vulcan_I(new Object[]{new Integer(var4), new Integer(var2), new Long(var8), new Integer(var5), new Boolean(var1), new Boolean(var3), null, var10007});
   }

   public static String Vulcan_Y(Object[] var0) {
      int var3 = (Integer)var0[0];
      int var7 = (Integer)var0[1];
      int var6 = (Integer)var0[2];
      boolean var4 = (Boolean)var0[3];
      boolean var8 = (Boolean)var0[4];
      char[] var5 = (char[])var0[5];
      long var1 = (Long)var0[6];
      var1 ^= a;
      long var9 = var1 ^ 94957604232817L;
      Random var10007 = Vulcan_T;
      return Vulcan_I(new Object[]{new Integer(var3), new Integer(var7), new Long(var9), new Integer(var6), new Boolean(var4), new Boolean(var8), var5, var10007});
   }

   public static String Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_S(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_Q(Object[] var0) {
      int var4 = (Integer)var0[0];
      long var2 = (Long)var0[1];
      char[] var1 = (char[])var0[2];
      var2 ^= a;
      long var5 = var2 ^ 15335804597033L;

      Random var10007;
      try {
         if (var1 == null) {
            var10007 = Vulcan_T;
            return Vulcan_I(new Object[]{new Integer(var4), new Integer(0), new Long(var5), new Integer(0), new Boolean(false), new Boolean(false), null, var10007});
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }

      int var10003 = var1.length;
      var10007 = Vulcan_T;
      return Vulcan_I(new Object[]{new Integer(var4), new Integer(0), new Long(var5), new Integer(var10003), new Boolean(false), new Boolean(false), var1, var10007});
   }

   static {
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = "!\"8V\u0017\u0002S\u0016#iQ\u0013\u001fC\u001c*iP\u0006\u0003N\u001d iO\u0017\u001f@\u0007/i\u0010S.:\u0003\u001e\u0014T\u0000g=K\u0013\u001f\u0007Ci";
      int var4 = "!\"8V\u0017\u0002S\u0016#iQ\u0013\u001fC\u001c*iP\u0006\u0003N\u001d iO\u0017\u001f@\u0007/i\u0010S.:\u0003\u001e\u0014T\u0000g=K\u0013\u001f\u0007Ci".length();
      char var1 = 31;
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 64;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 64;
               var10005 = var7;
            } else {
               var10 = 64;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 64;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 51;
                  break;
               case 1:
                  var23 = 7;
                  break;
               case 2:
                  var23 = 9;
                  break;
               case 3:
                  var23 = 99;
                  break;
               case 4:
                  var23 = 50;
                  break;
               case 5:
                  var23 = 49;
                  break;
               default:
                  var23 = 103;
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
            Vulcan_T = new Random();
            return;
         }

         var1 = var2.charAt(var0);
      }
   }

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
