package me.frep.vulcan.spigot.check.data;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.api.check.ICheckData;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;

public class CheckData implements ICheckData {
   private boolean Vulcan_R;
   private boolean Vulcan_L;
   private boolean Vulcan_U;
   private boolean Vulcan_G;
   private boolean Vulcan_N;
   private int Vulcan_V;
   private int Vulcan_T;
   private int Vulcan_H;
   private int Vulcan_I;
   private int Vulcan_r;
   private int Vulcan_e;
   private int Vulcan_E;
   private int Vulcan_A;
   private double Vulcan_s;
   private double Vulcan_f;
   private double Vulcan_j;
   private double Vulcan_z;
   private List Vulcan_O;
   private String Vulcan_w;
   private static AbstractCheck[] Vulcan_h;
   private static final long a = Vulcan_n.a(7233949128177500384L, 1640478931103737130L, MethodHandles.lookup().lookupClass()).a(19453917554841L);
   private static final String[] b;

   public CheckData(String var1) {
      long var2 = a ^ 54185354784082L;
      super();
      Vulcan_m();
      this.Vulcan_w = var1;

      try {
         if (AbstractCheck.Vulcan_m() != 0) {
            Vulcan_P(new AbstractCheck[2]);
         }

      } catch (RuntimeException var5) {
         throw a(var5);
      }
   }

   public String toString() {
      long var1 = a ^ 17128489036604L;
      AbstractCheck[] var3 = Vulcan_m();
      StringBuilder var10000 = new StringBuilder();
      String[] var5 = b;
      String var6 = var10000.append(var5[2]).append(this.Vulcan_w).append(var5[0]).append(this.Vulcan_R).append(var5[3]).append(this.Vulcan_L).append(var5[1]).append(this.Vulcan_U).append("}").toString();
      if (var3 != null) {
         int var4 = AbstractCheck.Vulcan_V();
         ++var4;
         AbstractCheck.Vulcan_H(var4);
      }

      return var6;
   }

   public boolean isEnabled() {
      return this.Vulcan_R;
   }

   public boolean isPunishable() {
      return this.Vulcan_L;
   }

   public boolean isBroadcastPunishment() {
      return this.Vulcan_U;
   }

   public boolean isHotbarShuffle() {
      return this.Vulcan_G;
   }

   public boolean isRandomRotation() {
      return this.Vulcan_N;
   }

   public int getMaxViolations() {
      return this.Vulcan_V;
   }

   public int getAlertInterval() {
      return this.Vulcan_T;
   }

   public int getMinimumVlToNotify() {
      return this.Vulcan_H;
   }

   public int getMaxPing() {
      return this.Vulcan_I;
   }

   public int getMinimumVlToRandomlyRotate() {
      return this.Vulcan_r;
   }

   public int getMinimumVlToShuffleHotbar() {
      return this.Vulcan_e;
   }

   public double getMinimumTps() {
      return this.Vulcan_s;
   }

   public double getMaxBuffer() {
      return this.Vulcan_f;
   }

   public double getBufferDecay() {
      return this.Vulcan_j;
   }

   public double getBufferMultiple() {
      return this.Vulcan_z;
   }

   public List getPunishmentCommands() {
      return this.Vulcan_O;
   }

   public int getRandomRotationInterval() {
      return this.Vulcan_E;
   }

   public int Vulcan_o(Object[] var1) {
      return this.Vulcan_I;
   }

   public int Vulcan_L(Object[] var1) {
      return this.Vulcan_r;
   }

   public int Vulcan_n(Object[] var1) {
      return this.Vulcan_e;
   }

   public int Vulcan_l(Object[] var1) {
      return this.Vulcan_A;
   }

   public String Vulcan_n(Object[] var1) {
      return this.Vulcan_w;
   }

   public void Vulcan_X(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_R = var2;
   }

   public void Vulcan_V(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_L = var2;
   }

   public void Vulcan_U(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_U = var2;
   }

   public void Vulcan_P(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_G = var2;
   }

   public void Vulcan_d(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_N = var2;
   }

   public void Vulcan__(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_V = var2;
   }

   public void Vulcan_Z(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_T = var2;
   }

   public void Vulcan_m(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_H = var2;
   }

   public void Vulcan_s(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_I = var2;
   }

   public void Vulcan_l(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_r = var2;
   }

   public void Vulcan_k(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_e = var2;
   }

   public void Vulcan_O(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_E = var2;
   }

   public void Vulcan_g(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_A = var2;
   }

   public void Vulcan_R(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_s = var2;
   }

   public void Vulcan_r(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_f = var2;
   }

   public void Vulcan_J(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_j = var2;
   }

   public void Vulcan_w(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_z = var2;
   }

   public void Vulcan_T(Object[] var1) {
      List var2 = (List)var1[0];
      this.Vulcan_O = var2;
   }

   public void Vulcan_v(Object[] var1) {
      String var2 = (String)var1[0];
      this.Vulcan_w = var2;
   }

   public static void Vulcan_P(AbstractCheck[] var0) {
      Vulcan_h = var0;
   }

   public static AbstractCheck[] Vulcan_m() {
      return Vulcan_h;
   }

   static {
      long var0 = a ^ 19339688113478L;
      Vulcan_P((AbstractCheck[])null);
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
      String var6 = "QÁØ¹Öï\u00967æH+!\u0016ÆD\\\u0018\u0014¥<\u009b.,\u000b¤\u0085\u0015sç\u008fñuþaD\u0091Ê²\u0081\u0001%";
      int var8 = "QÁØ¹Öï\u00967æH+!\u0016ÆD\\\u0018\u0014¥<\u009b.,\u000b¤\u0085\u0015sç\u008fñuþaD\u0091Ê²\u0081\u0001%".length();
      char var5 = 16;
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var11 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var11.getBytes("ISO-8859-1"));
            String var15 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var15;
               if ((var4 += var5) >= var8) {
                  b = var9;
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var15;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label27;
               }

               var6 = "\tB´\u009d$Ù\u0092\u0017Ú\u0083\u0081iY\u008b\u008cÎ\u0010\u001e\u009a½\u00892^X\bÑ\f\u0085\bt°Ö¢";
               var8 = "\tB´\u009d$Ù\u0092\u0017Ú\u0083\u0081iY\u008b\u008cÎ\u0010\u001e\u009a½\u00892^X\bÑ\f\u0085\bt°Ö¢".length();
               var5 = 16;
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
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
