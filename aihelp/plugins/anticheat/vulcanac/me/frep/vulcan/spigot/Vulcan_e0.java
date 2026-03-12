package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class Vulcan_e0 {
   private static final String Vulcan_C;
   public static final Class Vulcan_y;
   static Class Vulcan_q;
   private static final long a = Vulcan_n.a(1631596583276554206L, -5833461405813459456L, MethodHandles.lookup().lookupClass()).a(907979758967L);

   public static void Vulcan_n(Object[] var0) {
      try {
         Field var1 = Reflection.getField(Vulcan_y, List.class, 0);
         var1.setAccessible(true);
         Object var2 = Proxy.newProxyInstance(Vulcan_q.getClassLoader(), new Class[]{Vulcan_q}, Vulcan_e0::lambda$registerStartEvent$0);
         List var3 = (List)var1.get(Vulcan_L(new Object[]{Bukkit.getServer()}));
         var3.add(var2);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static Class Vulcan_k(Object[] var0) {
      String var1 = (String)var0[0];
      return SpigotReflectionUtil.getNMSClass(var1);
   }

   public static Object Vulcan_L(Object[] var0) {
      Server var1 = (Server)var0[0];
      return SpigotReflectionUtil.getMinecraftServerInstance(var1);
   }

   private static Object lambda$registerStartEvent$0(Object var0, Method var1, Object[] var2) {
      long var3 = a ^ 27040264799865L;
      long var5 = var3 ^ 22838769086791L;
      int[] var10000 = Vulcan_eG.Vulcan_R();
      Iterator var8 = Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_U(new Object[0]).iterator();
      int[] var7 = var10000;

      while(var8.hasNext()) {
         Vulcan_iE var9 = (Vulcan_iE)var8.next();
         var9.Vulcan_P(new Object[0]).Vulcan_J(new Object[]{var5, true});
         if (var7 != null) {
            break;
         }
      }

      return null;
   }

   static {
      long var1 = a ^ 108660665388738L;
      Cipher var3;
      Cipher var10000 = var3 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var1 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var4 = 1; var4 < 8; ++var4) {
         var10003[var4] = (byte)((int)(var1 << var4 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var10 = new String[6];
      int var8 = 0;
      String var7 = "\u0014µï\r\\FÐ\u0012\u0080æÓv'¸p\u009b\b\t!\u0000 `\u0001§ª\u0018\u009eI «VÉ?eÌ³¨\u0013\u009fm\fð\u0004\u0098zëZ¡Eç\u0010z\u008fâd¡¤\u0010>q\u0089i<\u0098ÿnÁ";
      int var9 = "\u0014µï\r\\FÐ\u0012\u0080æÓv'¸p\u009b\b\t!\u0000 `\u0001§ª\u0018\u009eI «VÉ?eÌ³¨\u0013\u009fm\fð\u0004\u0098zëZ¡Eç\u0010z\u008fâd¡¤\u0010>q\u0089i<\u0098ÿnÁ".length();
      char var6 = 16;
      int var5 = -1;

      label101:
      while(true) {
         ++var5;
         String var23 = var7.substring(var5, var5 + var6);
         byte var10001 = -1;

         while(true) {
            byte[] var11 = var3.doFinal(var23.getBytes("ISO-8859-1"));
            String var27 = a(var11).intern();
            switch(var10001) {
            case 0:
               var10[var8++] = var27;
               if ((var5 += var6) >= var9) {
                  String[] var0 = var10;
                  Vulcan_q = null;
                  String var12 = Bukkit.getServer().getClass().getPackage().getName();
                  String[] var13 = var12.split(var10[1]);

                  label88: {
                     try {
                        if (var13.length > 3) {
                           Vulcan_C = var13[3];
                           break label88;
                        }
                     } catch (Throwable var22) {
                        throw a(var22);
                     }

                     Vulcan_C = "";
                  }

                  Class var14 = null;

                  try {
                     if (!Vulcan_C.isEmpty()) {
                        var14 = Vulcan_k(new Object[]{var0[3]});
                     }
                  } catch (Throwable var17) {
                  }

                  if (var14 == null) {
                     try {
                        var14 = Class.forName(var0[4]);
                     } catch (Throwable var16) {
                     }
                  }

                  try {
                     if (var14 == null) {
                        throw new RuntimeException(var0[5]);
                     }
                  } catch (Throwable var21) {
                     throw a(var21);
                  }

                  Vulcan_y = var14;

                  try {
                     label112: {
                        ServerVersion var15 = PacketEvents.getAPI().getServerManager().getVersion();

                        try {
                           if (var15.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
                              Vulcan_q = Vulcan_k(new Object[]{var0[2]});
                              break label112;
                           }
                        } catch (Throwable var19) {
                           throw a(var19);
                        }

                        try {
                           if (var15.isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
                              Vulcan_q = Vulcan_k(new Object[]{var0[0]});
                              break label112;
                           }
                        } catch (Throwable var18) {
                           throw a(var18);
                        }

                        Vulcan_q = Runnable.class;
                     }
                  } catch (ClassNotFoundException var20) {
                     var20.printStackTrace();
                  }

                  return;
               }

               var6 = var7.charAt(var5);
               break;
            default:
               var10[var8++] = var27;
               if ((var5 += var6) < var9) {
                  var6 = var7.charAt(var5);
                  continue label101;
               }

               var7 = "µq2x\u0096U\rþ2ö%\u0095¦\u001a<É\\\u0090ñÅêTJ\u000b\u0002¡¦cÂ¯Ybi×0\u008d¬|\u009e-HptJz\u007f¦P\u001cG/:D¶øÓ?\u008c^\u001eÊ\u0019câ1Yc®\u001fõ?\u001c\u0095\u009c\u001bû\u00100P³;>Ì4\u008a',Ï¢\r\u0099ðÒ¶oO\u0011\u0014Ô>nÇ@átÒmV&M\u0096\u0002þ";
               var9 = "µq2x\u0096U\rþ2ö%\u0095¦\u001a<É\\\u0090ñÅêTJ\u000b\u0002¡¦cÂ¯Ybi×0\u008d¬|\u009e-HptJz\u007f¦P\u001cG/:D¶øÓ?\u008c^\u001eÊ\u0019câ1Yc®\u001fõ?\u001c\u0095\u009c\u001bû\u00100P³;>Ì4\u008a',Ï¢\r\u0099ðÒ¶oO\u0011\u0014Ô>nÇ@átÒmV&M\u0096\u0002þ".length();
               var6 = '(';
               var5 = -1;
            }

            ++var5;
            var23 = var7.substring(var5, var5 + var6);
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
