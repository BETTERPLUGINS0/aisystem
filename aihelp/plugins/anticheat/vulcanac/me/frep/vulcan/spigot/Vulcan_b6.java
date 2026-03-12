package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;

public final class Vulcan_b6 {
   private Object Vulcan_A;
   private Object Vulcan_J;
   private static final long a = Vulcan_n.a(-809712329019124428L, -6162361412173069049L, MethodHandles.lookup().lookupClass()).a(189429446373345L);
   private static final String[] b;

   public Object Vulcan_w(Object[] var1) {
      return this.Vulcan_A;
   }

   public Object Vulcan_A(Object[] var1) {
      return this.Vulcan_J;
   }

   public void Vulcan_f(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_A = var2;
   }

   public void Vulcan__(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_J = var2;
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      long var1 = a ^ 38026725932287L;
      AbstractCheck[] var3 = Vulcan_c.Vulcan_U();

      try {
         String var10000 = b[0] + this.Vulcan_w(new Object[0]) + b[1] + this.Vulcan_A(new Object[0]) + ")";
         if (AbstractCheck.Vulcan_m() != 0) {
            Vulcan_c.Vulcan_U(new AbstractCheck[1]);
         }

         return var10000;
      } catch (RuntimeException var4) {
         throw a(var4);
      }
   }

   public Vulcan_b6(Object var1, Object var2) {
      this.Vulcan_A = var1;
      this.Vulcan_J = var2;
   }

   static {
      long var0 = a ^ 27968531843849L;
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
      String var6 = "(H×\u0097Á\u0018É!\b9,\u0092Ãö\u0091Ã\u0092";
      int var8 = "(H×\u0097Á\u0018É!\b9,\u0092Ãö\u0091Ã\u0092".length();
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
            return;
         }

         var5 = var6.charAt(var4);
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
