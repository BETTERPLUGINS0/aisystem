package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonObject;
import java.lang.invoke.MethodHandles;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.Vulcan_n;
import org.bukkit.Bukkit;

public abstract class Vulcan_i7 {
   final String Vulcan_m;
   private static int[] Vulcan_T;
   private static final long a = Vulcan_n.a(9028648926964492069L, 1911003546591315439L, MethodHandles.lookup().lookupClass()).a(136172191560345L);
   private static final String[] b;

   Vulcan_i7(String param1) {
      // $FF: Couldn't be decompiled
   }

   private JsonObject Vulcan_T(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 73151370290670L;
      int[] var10000 = Vulcan_S();
      JsonObject var7 = new JsonObject();
      String[] var9 = b;
      var7.addProperty(var9[0], this.Vulcan_m);
      int[] var6 = var10000;

      try {
         JsonObject var14 = this.Vulcan_X(new Object[]{var4});

         JsonObject var13;
         label40: {
            try {
               var13 = var14;
               if (var6 == null) {
                  break label40;
               }

               if (var14 == null) {
                  return null;
               }
            } catch (Throwable var11) {
               throw a(var11);
            }

            var13 = var7;
         }

         var9 = b;
         var13.add(var9[1], var14);
         return var7;
      } catch (Throwable var12) {
         Throwable var8 = var12;

         try {
            if (var2 > 0L && Metrics.access$100()) {
               Bukkit.getLogger().log(Level.WARNING, b[3] + this.Vulcan_m, var8);
            }

            return null;
         } catch (Throwable var10) {
            throw a(var10);
         }
      }
   }

   protected abstract JsonObject Vulcan_X(Object[] var1);

   static JsonObject Vulcan_e(Object[] var0) {
      long var2 = (Long)var0[0];
      Vulcan_i7 var1 = (Vulcan_i7)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 100073162864478L;
      return var1.Vulcan_T(new Object[]{var4});
   }

   public static void Vulcan_Z(int[] var0) {
      Vulcan_T = var0;
   }

   public static int[] Vulcan_S() {
      return Vulcan_T;
   }

   static {
      int[] var10000 = new int[3];
      long var0 = a ^ 85786467631993L;
      Vulcan_Z(var10000);
      Cipher var2;
      Cipher var11 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var11.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[4];
      int var7 = 0;
      String var6 = "\u0002õ¹ªz!lò\b1ð\u0094³\u00adDÐ³";
      int var8 = "\u0002õ¹ªz!lò\b1ð\u0094³\u00adDÐ³".length();
      char var5 = '\b';
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var12 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var12.getBytes("ISO-8859-1"));
            String var16 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var16;
               if ((var4 += var5) >= var8) {
                  b = var9;
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var16;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label27;
               }

               var6 = "2¹\u00913|Ý\u0019M/î\u0097rÕÈ\u0080@(ñ×Â0\t_cí[ÆYaN\u00adV¬bca§J\u0084j0µZ\u001a \u009cC\u0090ËÝ\u001anØ\u0015S¶ÏgN/'À\u0002®¶¶Z¾`JKæsÈêCû%«Âö¢\u0095fíº<¨K";
               var8 = "2¹\u00913|Ý\u0019M/î\u0097rÕÈ\u0080@(ñ×Â0\t_cí[ÆYaN\u00adV¬bca§J\u0084j0µZ\u001a \u009cC\u0090ËÝ\u001anØ\u0015S¶ÏgN/'À\u0002®¶¶Z¾`JKæsÈêCû%«Âö¢\u0095fíº<¨K".length();
               var5 = '(';
               var4 = -1;
            }

            ++var4;
            var12 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static Throwable a(Throwable var0) {
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
