package me.frep.vulcan.spigot;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public enum Vulcan_e2 implements Predicate, Iterable {
   public static final Vulcan_e2 HORIZONTAL;
   public static final Vulcan_e2 VERTICAL;
   private static final Vulcan_e2[] Vulcan__;
   private static final String Vulcan_R;
   private static final Vulcan_e2[] Vulcan_y;
   private static final long a = Vulcan_n.a(4286951270150222190L, -7011562921247218904L, MethodHandles.lookup().lookupClass()).a(251190032109223L);
   private static final String[] b;

   private Vulcan_e2(String var3, int var4) {
   }

   public Vulcan_Xy[] Vulcan_o() {
      long var1 = a ^ 59260048708642L;
      String var3 = Vulcan_il.Vulcan_U();

      int var10000;
      label41: {
         try {
            var10000 = Vulcan_ew.Vulcan_g[this.ordinal()];
            if (var3 == null) {
               break label41;
            }

            switch(var10000) {
            case 1:
               break;
            case 2:
               return new Vulcan_Xy[]{Vulcan_Xy.UP, Vulcan_Xy.DOWN};
            default:
               throw new Error(b[2]);
            }
         } catch (RuntimeException var4) {
            throw a(var4);
         }

         var10000 = 4;
      }

      Vulcan_Xy[] var5 = new Vulcan_Xy[var10000];
      var5[0] = Vulcan_Xy.NORTH;
      var5[1] = Vulcan_Xy.EAST;
      var5[2] = Vulcan_Xy.SOUTH;
      var5[3] = Vulcan_Xy.WEST;
      return var5;
   }

   public Vulcan_Xy Vulcan_U(Random var1) {
      Vulcan_Xy[] var2 = this.Vulcan_o();
      return var2[var1.nextInt(var2.length)];
   }

   public boolean Vulcan_W(Vulcan_Xy var1) {
      long var2 = a ^ 81286243550985L;
      String var4 = Vulcan_il.Vulcan_U();

      boolean var7;
      label28: {
         Vulcan_Xy var10000;
         label27: {
            try {
               var10000 = var1;
               if (var4 == null) {
                  break label27;
               }

               if (var1 == null) {
                  break label28;
               }
            } catch (RuntimeException var6) {
               throw a(var6);
            }

            var10000 = var1;
         }

         try {
            if (var10000.Vulcan_F().Vulcan_v() == this) {
               var7 = true;
               return var7;
            }
         } catch (RuntimeException var5) {
            throw a(var5);
         }
      }

      var7 = false;
      return var7;
   }

   public Iterator iterator() {
      return Iterators.forArray(this.Vulcan_o());
   }

   public boolean apply(Object var1) {
      return this.Vulcan_W((Vulcan_Xy)var1);
   }

   static {
      long var0 = a ^ 94908105694825L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[6];
      int var7 = 0;
      String var6 = "½ÐQ\u008aÎ\u008el\u0086U\u009d\u00022L¿½,\u0010\u009ci(ë¨qX=ÊàSd\u000e2H\u008f0\u0087ïFÕÀÚ\bÜ °\f`¥Á\u0091á\u001e\u009btú©$¬l)Â\u0002\u0004ÅVWbT©·÷ð\u0084R¡§~M]ÎËau\u0010hÌ2]\u001cÑ]¢(\u001d\u007fü®¸ã\r";
      int var8 = "½ÐQ\u008aÎ\u008el\u0086U\u009d\u00022L¿½,\u0010\u009ci(ë¨qX=ÊàSd\u000e2H\u008f0\u0087ïFÕÀÚ\bÜ °\f`¥Á\u0091á\u001e\u009btú©$¬l)Â\u0002\u0004ÅVWbT©·÷ð\u0084R¡§~M]ÎËau\u0010hÌ2]\u001cÑ]¢(\u001d\u007fü®¸ã\r".length();
      char var5 = 16;
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
                  Vulcan_R = b[1];
                  String[] var11 = b;
                  HORIZONTAL = new Vulcan_e2(var11[0], 0, var11[4], 0);
                  VERTICAL = new Vulcan_e2(var11[5], 1, var11[3], 1);
                  Vulcan_y = new Vulcan_e2[]{HORIZONTAL, VERTICAL};
                  Vulcan__ = new Vulcan_e2[]{HORIZONTAL, VERTICAL};
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

               var6 = "½ÐQ\u008aÎ\u008el\u0086U\u009d\u00022L¿½,\u0010hÌ2]\u001cÑ]¢(\u001d\u007fü®¸ã\r";
               var8 = "½ÐQ\u008aÎ\u008el\u0086U\u009d\u00022L¿½,\u0010hÌ2]\u001cÑ]¢(\u001d\u007fü®¸ã\r".length();
               var5 = 16;
               var4 = -1;
            }

            ++var4;
            var12 = var6.substring(var4, var4 + var5);
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
