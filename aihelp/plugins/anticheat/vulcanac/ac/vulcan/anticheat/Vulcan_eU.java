package ac.vulcan.anticheat;

/** @deprecated */
public final class Vulcan_eU {
   private final Number Vulcan_z;
   private final Number Vulcan_G;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-647555314811920120L, -3080447721011986049L, (Object)null).a(123493542323446L);
   private static final String[] b;

   public Vulcan_eU(Number param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_eU(Number param1, Number param2) {
      // $FF: Couldn't be decompiled
   }

   public Number Vulcan_i(Object[] var1) {
      return this.Vulcan_z;
   }

   public Number Vulcan_p(Object[] var1) {
      return this.Vulcan_G;
   }

   public boolean Vulcan_P(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_X(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_e(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      byte var1 = 17;
      int var2 = 37 * var1 + this.Vulcan_z.hashCode();
      var2 = 37 * var2 + this.Vulcan_G.hashCode();
      return var2;
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[3];
      int var3 = 0;
      String var2 = "h?B_Q/\\U:R\u0012\u001c0SP\"B_Q3AHwI\u0010HfPYwI\nP*\"h?B_Q'JU:R\u0012\u001c0SP\"B_Q3AHwI\u0010HfPYwI\nP*\u001bh?B_R3_^2U_Q3AHwI\u0010HfPYwI\nP*";
      int var4 = "h?B_Q/\\U:R\u0012\u001c0SP\"B_Q3AHwI\u0010HfPYwI\nP*\"h?B_Q'JU:R\u0012\u001c0SP\"B_Q3AHwI\u0010HfPYwI\nP*\u001bh?B_R3_^2U_Q3AHwI\u0010HfPYwI\nP*".length();
      char var1 = '"';
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 106;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 106;
               var10005 = var7;
            } else {
               var10 = 106;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 106;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 86;
                  break;
               case 1:
                  var23 = 61;
                  break;
               case 2:
                  var23 = 77;
                  break;
               case 3:
                  var23 = 21;
                  break;
               case 4:
                  var23 = 86;
                  break;
               case 5:
                  var23 = 44;
                  break;
               default:
                  var23 = 88;
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
            return;
         }

         var1 = var2.charAt(var0);
      }
   }

   private static NullPointerException a(NullPointerException var0) {
      return var0;
   }
}
