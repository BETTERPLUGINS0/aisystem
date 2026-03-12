package ac.vulcan.anticheat;

public abstract class Vulcan_eb {
   private static final Vulcan_eb Vulcan_E;
   private static final Vulcan_eb Vulcan_G;
   private static final Vulcan_eb Vulcan_x;
   private static final Vulcan_eb Vulcan_e;
   private static final Vulcan_eb Vulcan_u;
   private static final Vulcan_eb Vulcan_X;
   private static final Vulcan_eb Vulcan_k;
   private static final Vulcan_eb Vulcan_q;
   private static final Vulcan_eb Vulcan_s;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-461084756422434196L, 8968108748574986422L, (Object)null).a(71388258356726L);

   public static Vulcan_eb Vulcan_q(Object[] var0) {
      return Vulcan_E;
   }

   public static Vulcan_eb Vulcan_R(Object[] var0) {
      return Vulcan_G;
   }

   public static Vulcan_eb Vulcan_u(Object[] var0) {
      return Vulcan_x;
   }

   public static Vulcan_eb Vulcan_m(Object[] var0) {
      return Vulcan_e;
   }

   public static Vulcan_eb Vulcan_l(Object[] var0) {
      return Vulcan_u;
   }

   public static Vulcan_eb Vulcan_K(Object[] var0) {
      return Vulcan_X;
   }

   public static Vulcan_eb Vulcan_w(Object[] var0) {
      return Vulcan_k;
   }

   public static Vulcan_eb Vulcan_z(Object[] var0) {
      return Vulcan_q;
   }

   public static Vulcan_eb Vulcan_N(Object[] var0) {
      return Vulcan_s;
   }

   public static Vulcan_eb Vulcan_O(Object[] var0) {
      int var1 = (Integer)var0[0];
      return new Vulcan_eh((char)var1);
   }

   public static Vulcan_eb Vulcan_M(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Vulcan_eb Vulcan_V(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Vulcan_eb Vulcan__(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   protected Vulcan_eb() {
   }

   public abstract int Vulcan_g(Object[] var1);

   public int Vulcan_q(Object[] var1) {
      long var3 = (Long)var1[0];
      char[] var2 = (char[])var1[1];
      int var5 = (Integer)var1[2];
      var3 ^= a;
      long var6 = var3 ^ 66374175043585L;
      return this.Vulcan_g(new Object[]{var2, new Integer(var5), new Integer(0), new Integer(var2.length), new Long(var6)});
   }

   static {
      String[] var6 = new String[2];
      int var4 = 0;
      String var3 = "RR\u0005Uy6\u0003\"";
      int var5 = "RR\u0005Uy6\u0003\"".length();
      char var2 = 2;
      int var1 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var1;
            char[] var10002 = var3.substring(var1, var1 + var2).toCharArray();
            int var10003 = var10002.length;
            int var8 = 0;
            byte var11 = 121;
            var10001 = var10002;
            int var9 = var10003;
            byte var13;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var13 = 121;
               var10005 = var8;
            } else {
               var11 = 121;
               var9 = var10003;
               if (var10003 <= var8) {
                  break label41;
               }

               var10004 = var10002;
               var13 = 121;
               var10005 = var8;
            }

            while(true) {
               char var23 = var10004[var10005];
               byte var24;
               switch(var8 % 7) {
               case 0:
                  var24 = 12;
                  break;
               case 1:
                  var24 = 9;
                  break;
               case 2:
                  var24 = 69;
                  break;
               case 3:
                  var24 = 119;
                  break;
               case 4:
                  var24 = 87;
                  break;
               case 5:
                  var24 = 99;
                  break;
               default:
                  var24 = 34;
               }

               var10004[var10005] = (char)(var23 ^ var13 ^ var24);
               ++var8;
               if (var11 == 0) {
                  var10005 = var11;
                  var10004 = var10001;
                  var13 = var11;
               } else {
                  if (var9 <= var8) {
                     break;
                  }

                  var10004 = var10001;
                  var13 = var11;
                  var10005 = var8;
               }
            }
         }

         var6[var4++] = (new String(var10001)).intern();
         if ((var1 += var2) >= var5) {
            Vulcan_E = new Vulcan_eh(',');
            Vulcan_G = new Vulcan_eh('\t');
            Vulcan_x = new Vulcan_eh(' ');
            Vulcan_e = new Vulcan_eA(var6[1].toCharArray());
            Vulcan_u = new Vulcan_eX();
            Vulcan_X = new Vulcan_eh('\'');
            Vulcan_k = new Vulcan_eh('"');
            Vulcan_q = new Vulcan_eA(var6[0].toCharArray());
            Vulcan_s = new Vulcan_e9();
            return;
         }

         var2 = var3.charAt(var1);
      }
   }

   private static RuntimeException b(RuntimeException var0) {
      return var0;
   }
}
