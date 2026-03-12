package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Vulcan_f {
   private static final long a = Vulcan_n.a(3026335522397869285L, -5277053946815983208L, MethodHandles.lookup().lookupClass()).a(281003055394632L);
   private static final String[] b;

   public static boolean Vulcan_w(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      long var10000 = a ^ var1;

      boolean var5;
      try {
         if (Vulcan_L(new Object[]{var3}) != null) {
            var5 = true;
            return var5;
         }
      } catch (UnsupportedOperationException var4) {
         throw a((Exception)var4);
      }

      var5 = false;
      return var5;
   }

   public static boolean Vulcan_s(@NotNull Class var0, String var1, long var2, Class[] var4) {
      var2 ^= a;
      long var5 = var2 ^ 10884112148062L;

      boolean var10000;
      try {
         if (Vulcan_V(var0, var1, var5, var4) != null) {
            var10000 = true;
            return var10000;
         }
      } catch (UnsupportedOperationException var7) {
         throw a((Exception)var7);
      }

      var10000 = false;
      return var10000;
   }

   @Nullable
   public static Method Vulcan_V(@NotNull Class param0, @NotNull String param1, long param2, Class[] param4) {
      // $FF: Couldn't be decompiled
   }

   @Nullable
   public static Class Vulcan_L(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         return Class.forName(Vulcan_s.a(var1));
      } catch (ClassNotFoundException var3) {
         return null;
      }
   }

   public static Field Vulcan_x(Object[] var0) {
      Class var4 = (Class)var0[0];
      long var2 = (Long)var0[1];
      String[] var1 = (String[])var0[2];
      var2 ^= a;
      int[] var10000 = Vulcan_eG.Vulcan_R();
      String[] var6 = var1;
      int var7 = var1.length;
      int[] var5 = var10000;
      int var8 = 0;

      label71:
      while(true) {
         int var18 = var8;

         label68:
         while(var18 < var7) {
            String var9 = var6[var8];
            Field[] var10 = var4.getDeclaredFields();
            int var11 = var10.length;
            var18 = 0;

            label66:
            while(true) {
               int var12 = var18;

               while(var12 < var11) {
                  Field var13 = var10[var12];
                  String var14 = var13.getType().getSimpleName();

                  label60: {
                     label77: {
                        try {
                           var10000 = var5;
                           if (var2 <= 0L) {
                              break label60;
                           }

                           if (var5 != null) {
                              break label77;
                           }

                           var18 = var9.equals(var14);
                           if (var5 != null) {
                              continue label68;
                           }
                        } catch (NoSuchFieldException var16) {
                           throw a((Exception)var16);
                        }

                        if (var2 <= 0L) {
                           continue label66;
                        }

                        try {
                           if (var18 != 0) {
                              var13.setAccessible(true);
                              return var13;
                           }
                        } catch (NoSuchFieldException var17) {
                           throw a((Exception)var17);
                        }

                        ++var12;
                     }

                     var10000 = var5;
                  }

                  if (var10000 != null) {
                     break;
                  }
               }

               ++var8;
               if (var2 > 0L && var5 == null) {
                  continue label71;
               }
               break label68;
            }
         }

         StringBuilder var10002 = new StringBuilder();
         String[] var15 = b;
         throw new NoSuchFieldException(var10002.append(var15[5]).append(var4.getName()).append(var15[1]).append(Arrays.toString(var1)).toString());
      }
   }

   public static Field Vulcan_W(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Object Vulcan_f(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private Vulcan_f() {
      throw new UnsupportedOperationException(b[2]);
   }

   static {
      long var0 = a ^ 115404171951633L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[7];
      int var7 = 0;
      String var6 = "Üú\u0095Ôå¢\u0016×\u008bm>ÙÊ9§\u0080e\u0012óoØM®S¦i\u0017TWA\u009c\u0003\u0010M\nvÆ\u001a×¨Å\u00039Ú¢wxöç8as\u0080Àñ#Î\u009dòÜU¨Ì~¡Öe¿©ê\u008f\u001e\u0083í|Ä\u0016ÛY R\u008fum¾\f%\u00ad\u0083S²$\u0015ñ\u001cÊ³Ání \fBL\u009e\u0096\u0010Aþ\u0012\u0013\u0018Sê~UTØ\u009b\u0012\rXê(Üú\u0095Ôå¢\u0016×\u0080a\u00ad\u0017Mfcoú(9\u0098cøÛÐgÎê«\u007fW#A\u0087Ú\t_Z¬åM";
      int var8 = "Üú\u0095Ôå¢\u0016×\u008bm>ÙÊ9§\u0080e\u0012óoØM®S¦i\u0017TWA\u009c\u0003\u0010M\nvÆ\u001a×¨Å\u00039Ú¢wxöç8as\u0080Àñ#Î\u009dòÜU¨Ì~¡Öe¿©ê\u008f\u001e\u0083í|Ä\u0016ÛY R\u008fum¾\f%\u00ad\u0083S²$\u0015ñ\u001cÊ³Ání \fBL\u009e\u0096\u0010Aþ\u0012\u0013\u0018Sê~UTØ\u009b\u0012\rXê(Üú\u0095Ôå¢\u0016×\u0080a\u00ad\u0017Mfcoú(9\u0098cøÛÐgÎê«\u007fW#A\u0087Ú\t_Z¬åM".length();
      char var5 = ' ';
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

               var6 = "Üú\u0095Ôå¢\u0016×\u008bm>ÙÊ9§\u0080e\u0012óoØM®S¦i\u0017TWA\u009c\u0003\u0010Aþ\u0012\u0013\u0018Sê~UTØ\u009b\u0012\rXê";
               var8 = "Üú\u0095Ôå¢\u0016×\u008bm>ÙÊ9§\u0080e\u0012óoØM®S¦i\u0017TWA\u009c\u0003\u0010Aþ\u0012\u0013\u0018Sê~UTØ\u009b\u0012\rXê".length();
               var5 = ' ';
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static Exception a(Exception var0) {
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
