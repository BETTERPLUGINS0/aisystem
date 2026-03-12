package me.frep.vulcan.spigot.check.impl.combat.velocity;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.Vulcan_b6;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Velocity",
   type = 'D',
   complexType = "Horizontal",
   description = "Horizontal velocity modifications.",
   experimental = true
)
public class VelocityD extends AbstractCheck {
   private static final float Vulcan_x = 0.02F;
   private static final float Vulcan_F = 0.6F;
   private boolean Vulcan_h = false;
   private boolean Vulcan_o = false;
   private int Vulcan_n = 0;
   private static int[] Vulcan_Z;
   private static final long b = me.frep.vulcan.spigot.Vulcan_n.a(-731928397626114833L, -7251638748949372207L, MethodHandles.lookup().lookupClass()).a(254265777716899L);
   private static final String[] d;

   public VelocityD(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   private Vulcan_b6 Vulcan_w(Object[] var1) {
      long var2 = (Long)var1[0];
      int var4 = (Integer)var1[1];
      var2 ^= b;
      int[] var10000 = Vulcan_I();
      float var6 = 0.0F;
      int[] var5 = var10000;
      float var7 = 0.0F;

      label120: {
         label119: {
            label125: {
               label126: {
                  label116: {
                     label127: {
                        label114: {
                           label128: {
                              label112: {
                                 label129: {
                                    label110: {
                                       label130: {
                                          label108: {
                                             label131: {
                                                label106: {
                                                   label132: {
                                                      label104: {
                                                         try {
                                                            if (var5 != null) {
                                                               break label104;
                                                            }

                                                            switch(var4) {
                                                            case 0:
                                                               break;
                                                            case 1:
                                                               break label132;
                                                            case 2:
                                                               break label131;
                                                            case 3:
                                                               break label130;
                                                            case 4:
                                                               break label129;
                                                            case 5:
                                                               break label128;
                                                            case 6:
                                                               break label127;
                                                            case 7:
                                                               break label126;
                                                            case 8:
                                                               break label119;
                                                            default:
                                                               return new Vulcan_b6(var6, var7);
                                                            }
                                                         } catch (RuntimeException var8) {
                                                            throw a(var8);
                                                         }

                                                         var6 = 1.0F;
                                                      }

                                                      var10000 = var5;
                                                      if (var2 < 0L) {
                                                         break label106;
                                                      }

                                                      if (var5 == null) {
                                                         return new Vulcan_b6(var6, var7);
                                                      }
                                                   }

                                                   var6 = 1.0F;
                                                   var7 = -1.0F;
                                                   var10000 = var5;
                                                }

                                                if (var2 < 0L) {
                                                   break label108;
                                                }

                                                if (var10000 == null) {
                                                   return new Vulcan_b6(var6, var7);
                                                }
                                             }

                                             var7 = -1.0F;
                                             var10000 = var5;
                                          }

                                          if (var2 < 0L) {
                                             break label110;
                                          }

                                          if (var10000 == null) {
                                             return new Vulcan_b6(var6, var7);
                                          }
                                       }

                                       var6 = -1.0F;
                                       var7 = -1.0F;
                                       var10000 = var5;
                                    }

                                    if (var2 < 0L) {
                                       break label112;
                                    }

                                    if (var10000 == null) {
                                       return new Vulcan_b6(var6, var7);
                                    }
                                 }

                                 var6 = -1.0F;
                                 var10000 = var5;
                              }

                              if (var2 < 0L) {
                                 break label114;
                              }

                              if (var10000 == null) {
                                 return new Vulcan_b6(var6, var7);
                              }
                           }

                           var6 = -1.0F;
                           var7 = 1.0F;
                           var10000 = var5;
                        }

                        if (var2 < 0L) {
                           break label116;
                        }

                        if (var10000 == null) {
                           return new Vulcan_b6(var6, var7);
                        }
                     }

                     var7 = 1.0F;
                     var10000 = var5;
                  }

                  if (var2 < 0L) {
                     break label125;
                  }

                  if (var10000 == null) {
                     return new Vulcan_b6(var6, var7);
                  }
               }

               var6 = 1.0F;
               var7 = 1.0F;
               if (var2 <= 0L) {
                  break label120;
               }

               var10000 = var5;
            }

            if (var10000 == null) {
               return new Vulcan_b6(var6, var7);
            }
         }

         var6 = 0.0F;
      }

      var7 = 0.0F;
      return new Vulcan_b6(var6, var7);
   }

   public float Vulcan_l(Object[] var1) {
      long var2 = (Long)var1[0];
      Vulcan_iE var4 = (Vulcan_iE)var1[1];
      long var10000 = b ^ var2;
      int[] var9 = Vulcan_I();
      double var7 = var4.Vulcan_J(new Object[0]).Vulcan_E(new Object[0]);
      var7 += (double)var4.Vulcan_J(new Object[0]).Vulcan_j(new Object[0]) * 0.2D * var7;
      int[] var5 = var9;
      float var10 = (float)var7;
      if (var5 != null) {
         int var6 = AbstractCheck.Vulcan_V();
         ++var6;
         AbstractCheck.Vulcan_H(var6);
      }

      return var10;
   }

   public static void Vulcan_n(int[] var0) {
      Vulcan_Z = var0;
   }

   public static int[] Vulcan_I() {
      return Vulcan_Z;
   }

   static {
      long var0 = b ^ 29746001671777L;
      Vulcan_n((int[])null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[3];
      int var7 = 0;
      String var6 = "~4\u0096\u0003\u001d8pc\u0010âÛ\u009blCï\u000bT\u0014\u0019IQH2á,\b%(S©'gâì";
      int var8 = "~4\u0096\u0003\u001d8pc\u0010âÛ\u009blCï\u000bT\u0014\u0019IQH2á,\b%(S©'gâì".length();
      char var5 = '\b';
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var12 = b(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var12;
         if ((var4 += var5) >= var8) {
            d = var9;
            return;
         }

         var5 = var6.charAt(var4);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   private static String b(byte[] var0) {
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
