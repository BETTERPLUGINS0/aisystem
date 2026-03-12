package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.util.NumberConversions;

public final class Vulcan_iN {
   public static final double Vulcan_o;
   private static AbstractCheck[] Vulcan_y;
   private static final long a = Vulcan_n.a(-5441129807915112556L, 1595597685481313960L, MethodHandles.lookup().lookupClass()).a(87023213808334L);
   private static final String[] b;

   public static double Vulcan_E(Object[] var0) {
      double var1 = (Double)var0[0];
      double var3 = (Double)var0[1];
      return Math.sqrt(var1 * var1 + var3 * var3);
   }

   public static double Vulcan_x(Object[] var0) {
      double var1 = (Double)var0[0];
      double var3 = (Double)var0[1];
      double var5 = (Double)var0[2];
      return Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
   }

   public static String Vulcan_Q(Object[] var0) {
      double var1 = (Double)var0[0];
      return (new DecimalFormat(b[0])).format(var1);
   }

   public static float Vulcan_M(Object[] var0) {
      float var1 = (Float)var0[0];
      float var2 = (Float)var0[1];
      float var3 = var1 % 360.0F;
      float var4 = var2 % 360.0F;
      float var5 = Math.abs(var3 - var4);
      return (float)Math.abs(Math.min(360.0D - (double)var5, (double)var5));
   }

   public static double Vulcan_F(Object[] var0) {
      long var2 = (Long)var0[0];
      Collection var1 = (Collection)var0[1];
      var2 ^= a;
      int var5 = 0;
      int[] var10000 = Vulcan_eG.Vulcan_R();
      double var6 = 0.0D;
      double var8 = 0.0D;
      Iterator var12 = var1.iterator();
      int[] var4 = var10000;

      Number var13;
      double var14;
      while(true) {
         if (var12.hasNext()) {
            var13 = (Number)var12.next();
            if (var2 > 0L) {
               var14 = var6 + var13.doubleValue();
               if (var4 != null) {
                  break;
               }

               var6 = var14;
               ++var5;
            }

            if (var4 == null) {
               continue;
            }
         }

         var14 = var6;
         if (var2 > 0L) {
            var14 = var6 / (double)var5;
         }
         break;
      }

      double var10 = var14;
      var12 = var1.iterator();

      while(true) {
         if (var12.hasNext()) {
            var13 = (Number)var12.next();
            if (var2 >= 0L) {
               var14 = var8 + Math.pow(var13.doubleValue() - var10, 2.0D);
               if (var4 != null) {
                  break;
               }

               var8 = var14;
            }

            if (var4 == null) {
               continue;
            }
         }

         var14 = var8;
         break;
      }

      return var14;
   }

   public static double Vulcan_B(Object[] var0) {
      long var1 = (Long)var0[0];
      Collection var3 = (Collection)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 108101071067387L;
      double var6 = Vulcan_F(new Object[]{var4, var3});
      return Math.sqrt(var6);
   }

   public static double Vulcan_W(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_s(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Number Vulcan_k(Object[] var0) {
      Collection var1 = (Collection)var0[0];
      HashMap var2 = new HashMap();
      var1.forEach(Vulcan_iN::lambda$getModeElevated$0);
      return (Number)((Vulcan_V)var2.keySet().stream().map(Vulcan_iN::lambda$getModeElevated$1).max(Comparator.comparing(Vulcan_V::Vulcan_R, Comparator.naturalOrder())).orElseThrow(NullPointerException::new)).Vulcan_I(new Object[0]);
   }

   public static Double Vulcan_M(Object[] var0) {
      double[] var1 = (double[])var0[0];
      HashMap var2 = new HashMap();
      Arrays.stream(var1).forEach(Vulcan_iN::lambda$getMode$2);
      return (Double)((Vulcan_V)var2.keySet().stream().map(Vulcan_iN::lambda$getMode$3).max(Comparator.comparing(Vulcan_V::Vulcan_R, Comparator.naturalOrder())).orElseThrow(NullPointerException::new)).Vulcan_I(new Object[0]);
   }

   public static double Vulcan_J(Object[] var0) {
      double var1 = (Double)var0[0];
      int var3 = (Integer)var0[1];
      long var4 = (Long)var0[2];
      long var10000 = a ^ var4;

      try {
         if (var3 < 0) {
            throw new IllegalArgumentException();
         }
      } catch (NullPointerException var6) {
         throw a(var6);
      }

      return (new BigDecimal(var1)).setScale(var3, RoundingMode.HALF_UP).doubleValue();
   }

   public static int Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static double Vulcan_H(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static long Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_A(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_z(Object[] var0) {
      double var1 = (Double)var0[0];
      double var3 = (Double)var0[1];
      double var5 = Math.abs(var1 % 360.0D - var3 % 360.0D);
      return Math.abs(Math.min(360.0D - var5, var5));
   }

   public static double Vulcan_U(Object[] var0) {
      double var1 = (Double)var0[0];
      double var3 = (Double)var0[1];
      return Math.abs(var1 % 360.0D - var3 % 360.0D);
   }

   public static double Vulcan_Z(Object[] var0) {
      Collection var3 = (Collection)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 115186401036010L;
      return 20.0D / Vulcan_T(new Object[]{var3, var4}) * 50.0D;
   }

   public static int Vulcan_u(Object[] var0) {
      Collection var1 = (Collection)var0[0];
      return (int)((long)var1.size() - var1.stream().distinct().count());
   }

   public static int Vulcan_W(Object[] var0) {
      Collection var1 = (Collection)var0[0];
      return (int)var1.stream().distinct().count();
   }

   public static int Vulcan_f(Object[] var0) {
      Vulcan_iE var1 = (Vulcan_iE)var0[0];
      return NumberConversions.floor((double)var1.Vulcan_P(new Object[0]).Vulcan_e(new Object[0]) / 50.0D) + 3;
   }

   private Vulcan_iN() {
      throw new UnsupportedOperationException(b[1]);
   }

   private static Vulcan_V lambda$getMode$3(Map var0, Double var1) {
      return new Vulcan_V(var1, var0.get(var1));
   }

   private static void lambda$getMode$2(Map var0, double var1) {
      int var3 = (Integer)var0.getOrDefault(var1, 0);
      var0.put(var1, var3 + 1);
   }

   private static Vulcan_V lambda$getModeElevated$1(Map var0, Number var1) {
      return new Vulcan_V(var1, var0.get(var1));
   }

   private static void lambda$getModeElevated$0(Map var0, Number var1) {
      int var2 = (Integer)var0.getOrDefault(var1, 0);
      var0.put(var1, var2 + 1);
   }

   static {
      long var0 = a ^ 60956874915733L;
      Vulcan_j((AbstractCheck[])null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[2];
      int var7 = 0;
      String var6 = "»\u0001\u0013\u0080\u0016\u0000ú\n8\u0019+\u0002øM\u0013«\u00adÈ\u0006Z¨\u001b|\u0080\u0018\u001e?¦LÆ\u0012\u001eÓ¤\u0011èb\u008a=\u0080é\u000emð8ÚwêâÈÎ\u0082\u00851\u0098\t\tnîymÂg^%";
      int var8 = "»\u0001\u0013\u0080\u0016\u0000ú\n8\u0019+\u0002øM\u0013«\u00adÈ\u0006Z¨\u001b|\u0080\u0018\u001e?¦LÆ\u0012\u001eÓ¤\u0011èb\u008a=\u0080é\u000emð8ÚwêâÈÎ\u0082\u00851\u0098\t\tnîymÂg^%".length();
      char var5 = '\b';
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var12 = a(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var12;
         if ((var4 += var5) >= var8) {
            b = var9;
            Vulcan_o = Math.pow(2.0D, 24.0D);
            return;
         }

         var5 = var6.charAt(var4);
      }
   }

   public static void Vulcan_j(AbstractCheck[] var0) {
      Vulcan_y = var0;
   }

   public static AbstractCheck[] Vulcan_p() {
      return Vulcan_y;
   }

   private static NullPointerException a(NullPointerException var0) {
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
