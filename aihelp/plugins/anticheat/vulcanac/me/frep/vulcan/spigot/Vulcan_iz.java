package me.frep.vulcan.spigot;

import ac.vulcan.anticheat.Vulcan_iF;
import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Vulcan_iz implements Runnable {
   private boolean Vulcan_L = false;
   private int Vulcan_q = 0;
   private int Vulcan_O = 0;
   private static Vulcan_iF Vulcan_b;
   private long Vulcan_s = Long.MAX_VALUE;
   private long Vulcan_x = Long.MAX_VALUE;
   private long Vulcan_F = Long.MAX_VALUE;
   static final boolean Vulcan_J;
   private static int Vulcan_c;
   private static final long a = Vulcan_n.a(1303839833229145769L, -4274421418011560033L, MethodHandles.lookup().lookupClass()).a(240863361312420L);
   private static final String[] b;

   public void Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_w(Object[] var1) {
      long var2 = (Long)var1[0];
      long var10000 = a ^ var2;
      int var4 = Vulcan_J();

      Vulcan_iF var6;
      label22: {
         try {
            var6 = Vulcan_b;
            if (var4 == 0) {
               break label22;
            }

            if (var6 == null) {
               return;
            }
         } catch (RuntimeException var5) {
            throw a(var5);
         }

         var6 = Vulcan_b;
      }

      var6.Vulcan_q(new Object[0]);
      Vulcan_b = null;
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_U(Object[] var1) {
      return this.Vulcan_L;
   }

   public int Vulcan_z(Object[] var1) {
      return this.Vulcan_q;
   }

   public int Vulcan_y(Object[] var1) {
      return this.Vulcan_O;
   }

   public long Vulcan_q(Object[] var1) {
      return this.Vulcan_s;
   }

   public long Vulcan_E(Object[] var1) {
      return this.Vulcan_x;
   }

   public long Vulcan_S(Object[] var1) {
      return this.Vulcan_F;
   }

   public void Vulcan_N(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_L = var2;
   }

   private static void lambda$run$0(String var0) {
      Vulcan_eG.Vulcan__(var0.replaceAll(b[1], Integer.toString(Vulcan_X7.Vulcan_J(new Object[0]))));
   }

   static {
      long var0 = a ^ 58009452448415L;
      Vulcan_o(79);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[4];
      int var7 = 0;
      String var6 = "Gê ¸\u00108?\u001a\u0002»\"ù\u001e¯_â\u0010\u001f\u009fÝ+Ù :_ÅªAü\u0080Z4â";
      int var8 = "Gê ¸\u00108?\u001a\u0002»\"ù\u001e¯_â\u0010\u001f\u009fÝ+Ù :_ÅªAü\u0080Z4â".length();
      char var5 = 16;
      int var4 = -1;

      label42:
      while(true) {
         ++var4;
         String var12 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var12.getBytes("ISO-8859-1"));
            String var17 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var17;
               if ((var4 += var5) >= var8) {
                  b = var9;

                  boolean var14;
                  label29: {
                     try {
                        if (!Vulcan_iz.class.desiredAssertionStatus()) {
                           var14 = true;
                           break label29;
                        }
                     } catch (RuntimeException var11) {
                        throw a(var11);
                     }

                     var14 = false;
                  }

                  Vulcan_J = var14;
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var17;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label42;
               }

               var6 = "!ñøO\u008d.\u0011£\u000b3âàïª9Fgõ\räo\rÙ\u0006èU\u0004²\u0013\u009b\u008cÃëÛ\u0003ù\u008aw7P(·\u009dyûÍkìû´\"f«\u0015 ¶b²¢\u0083\u000b¾¾z_HÛþóÕ]J\u009f®üPiHÍnÔ";
               var8 = "!ñøO\u008d.\u0011£\u000b3âàïª9Fgõ\räo\rÙ\u0006èU\u0004²\u0013\u009b\u008cÃëÛ\u0003ù\u008aw7P(·\u009dyûÍkìû´\"f«\u0015 ¶b²¢\u0083\u000b¾¾z_HÛþóÕ]J\u009f®üPiHÍnÔ".length();
               var5 = '(';
               var4 = -1;
            }

            ++var4;
            var12 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   public static void Vulcan_o(int var0) {
      Vulcan_c = var0;
   }

   public static int Vulcan_J() {
      return Vulcan_c;
   }

   public static int Vulcan_k() {
      int var0 = Vulcan_J();

      try {
         return var0 == 0 ? 44 : 0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   private static String a(byte[] var0) {
      int var1 = 0;
      int var2;
      char[] var3 = new char[var2 = var0.length];

      for(int var4 = 0; var4 < var2; ++var4) {
         int var5;
         if ((var5 = 255 & var0[var4]) < 192) {
            var3[var1++] = (char)var5;
         } else {
            char var6;
            byte var7;
            if (var5 < 224) {
               var6 = (char)((char)(var5 & 31) << 6);
               ++var4;
               var7 = var0[var4];
               var6 |= (char)(var7 & 63);
               var3[var1++] = var6;
            } else if (var4 < var2 - 2) {
               var6 = (char)((char)(var5 & 15) << 12);
               ++var4;
               var7 = var0[var4];
               var6 = (char)(var6 | (char)(var7 & 63) << 6);
               ++var4;
               var7 = var0[var4];
               var6 |= (char)(var7 & 63);
               var3[var1++] = var6;
            }
         }
      }

      return new String(var3, 0, var1);
   }
}
