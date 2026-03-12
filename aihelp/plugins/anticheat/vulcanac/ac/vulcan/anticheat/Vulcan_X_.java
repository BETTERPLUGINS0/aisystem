package ac.vulcan.anticheat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class Vulcan_X_ {
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-7427159737611700744L, 1868621707019674945L, (Object)null).a(269793738134866L);
   private static final String[] b;

   public static Object Vulcan_A(Object[] var0) {
      long var1 = (Long)var0[0];
      Serializable var3 = (Serializable)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 54180208922948L;
      long var6 = var1 ^ 92526109406680L;
      return Vulcan_b(new Object[]{new Long(var4), Vulcan_O(new Object[]{var3, new Long(var6)})});
   }

   public static void Vulcan_L(Object[] var0) {
      long var1 = (Long)var0[0];
      Serializable var4 = (Serializable)var0[1];
      OutputStream var3 = (OutputStream)var0[2];
      long var10000 = a ^ var1;
      String[] var5 = Vulcan_XL.Vulcan_v();

      try {
         if (var3 == null) {
            throw new IllegalArgumentException(b[2]);
         }
      } catch (IOException var22) {
         throw a(var22);
      }

      ObjectOutputStream var6 = null;
      boolean var16 = false;

      ObjectOutputStream var23;
      try {
         var16 = true;
         var6 = new ObjectOutputStream(var3);
         var6.writeObject(var4);
         var16 = false;
      } catch (IOException var17) {
         throw new Vulcan_Xd(var17);
      } finally {
         if (var16) {
            try {
               label99: {
                  label98: {
                     try {
                        var23 = var6;
                        if (var5 == null) {
                           break label98;
                        }

                        if (var6 == null) {
                           break label99;
                        }
                     } catch (IOException var18) {
                        throw a(var18);
                     }

                     var23 = var6;
                  }

                  var23.close();
               }
            } catch (IOException var19) {
            }

         }
      }

      try {
         var23 = var6;
         if (var5 != null) {
            if (var6 == null) {
               return;
            }

            var23 = var6;
         }

         var23.close();
      } catch (IOException var21) {
      }

   }

   public static byte[] Vulcan_O(Object[] var0) {
      Serializable var3 = (Serializable)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 14822584433446L;
      ByteArrayOutputStream var6 = new ByteArrayOutputStream(512);
      Vulcan_L(new Object[]{new Long(var4), var3, var6});
      return var6.toByteArray();
   }

   public static Object Vulcan_p(Object[] var0) {
      InputStream var1 = (InputStream)var0[0];
      long var2 = (Long)var0[1];
      long var10000 = a ^ var2;
      String[] var4 = Vulcan_XL.Vulcan_v();

      try {
         if (var1 == null) {
            throw new IllegalArgumentException(b[0]);
         }
      } catch (IOException var23) {
         throw a(var23);
      }

      ObjectInputStream var5 = null;

      Object var6;
      try {
         var5 = new ObjectInputStream(var1);
         var6 = var5.readObject();
      } catch (ClassNotFoundException var18) {
         throw new Vulcan_Xd(var18);
      } catch (IOException var19) {
         throw new Vulcan_Xd(var19);
      } finally {
         try {
            label103: {
               ObjectInputStream var24;
               label102: {
                  try {
                     var24 = var5;
                     if (var4 == null) {
                        break label102;
                     }

                     if (var5 == null) {
                        break label103;
                     }
                  } catch (IOException var20) {
                     throw a(var20);
                  }

                  var24 = var5;
               }

               var24.close();
            }
         } catch (IOException var21) {
         }

      }

      return var6;
   }

   public static Object Vulcan_b(Object[] var0) {
      long var1 = (Long)var0[0];
      byte[] var3 = (byte[])var0[1];
      var1 ^= a;
      long var4 = var1 ^ 58609590409884L;

      try {
         if (var3 == null) {
            throw new IllegalArgumentException(b[1]);
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }

      ByteArrayInputStream var6 = new ByteArrayInputStream(var3);
      return Vulcan_p(new Object[]{var6, new Long(var4)});
   }

   static {
      String[] var5 = new String[3];
      int var3 = 0;
      String var2 = "#e1\\_y^\u0002y\u0007\bdrO\u001a-9\tec\u000e\u0019b \\tr\u000e\u0019x8\u0010\u001b#e1\\tnZ\u0012V\t\\{b]\u0003-:\u0013b7L\u0012-:\tz{!#e1\\YbZ\u0007x /beK\u0016`t\u0011cdZWc;\b6uKWc!\u0010z";
      int var4 = "#e1\\_y^\u0002y\u0007\bdrO\u001a-9\tec\u000e\u0019b \\tr\u000e\u0019x8\u0010\u001b#e1\\tnZ\u0012V\t\\{b]\u0003-:\u0013b7L\u0012-:\tz{!#e1\\YbZ\u0007x /beK\u0016`t\u0011cdZWc;\b6uKWc!\u0010z".length();
      char var1 = ' ';
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 100;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 100;
               var10005 = var7;
            } else {
               var10 = 100;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 100;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 19;
                  break;
               case 1:
                  var23 = 105;
                  break;
               case 2:
                  var23 = 48;
                  break;
               case 3:
                  var23 = 24;
                  break;
               case 4:
                  var23 = 114;
                  break;
               case 5:
                  var23 = 115;
                  break;
               default:
                  var23 = 74;
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

   private static Exception a(Exception var0) {
      return var0;
   }
}
