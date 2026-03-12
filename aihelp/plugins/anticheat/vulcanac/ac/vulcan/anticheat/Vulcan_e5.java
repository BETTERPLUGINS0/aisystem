package ac.vulcan.anticheat;

import java.io.Serializable;

public final class Vulcan_e5 extends Vulcan_eS implements Serializable {
   private static final long serialVersionUID = 71849363892710L;
   private final Number Vulcan_x;
   private final Number Vulcan_j;
   private transient int Vulcan_N;
   private transient String Vulcan_Z;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-7092332795469269942L, 698327650426192033L, (Object)null).a(23973121992989L);
   private static final String[] d;

   public Vulcan_e5(Number param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_e5(Number param1, Number param2) {
      // $FF: Couldn't be decompiled
   }

   public Number Vulcan_b(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_x;
   }

   public Number Vulcan_t(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_j;
   }

   public boolean Vulcan_V(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      long var1 = a ^ 98196639925184L;
      long var3 = var1 ^ 26989243467745L;
      long var5 = var1 ^ 10713336260984L;
      long var7 = var1 ^ 2993362184529L;
      String var9 = Vulcan_eS.Vulcan_y();

      String var10000;
      label20: {
         try {
            var10000 = this.Vulcan_Z;
            if (var9 != null) {
               return var10000;
            }

            if (var10000 != null) {
               break label20;
            }
         } catch (IllegalArgumentException var11) {
            throw a(var11);
         }

         Vulcan_o var10 = new Vulcan_o(32);
         var10.Vulcan_FS(new Object[]{d[8], new Long(var3)});
         Number var10002 = this.Vulcan_x;
         var10.Vulcan_Dw(new Object[]{new Long(var7), var10002});
         var10.Vulcan__O(new Object[]{new Integer(44), new Long(var5)});
         var10002 = this.Vulcan_j;
         var10.Vulcan_Dw(new Object[]{new Long(var7), var10002});
         var10.Vulcan__O(new Object[]{new Integer(93), new Long(var5)});
         this.Vulcan_Z = var10.toString();
      }

      var10000 = this.Vulcan_Z;
      return var10000;
   }

   static {
      String[] var5 = new String[9];
      int var3 = 0;
      String var2 = "!L\\4y*`\u0017AK4z*~\u0001\u0004[q70kUPQq7,l\u0018A\u0019`n/hUEJ4c7hUVXzp:-\u001bQTvr-~$!L\\4y*`\u0017AK4z*~\u0001\u0004Pyg3h\u0018AW`7\u001cb\u0018TXfv=a\u0010%!L\\4y*`\u0017AKg72x\u0006P\u0019}z/a\u0010I\\zc\u007fN\u001aIIue>o\u0019A\u001c!L\\4y*`\u0017AKg72x\u0006P\u0019zx+-\u0017A\u0019zb3a\u001b!L\\4y*`\u0017AK4z*~\u0001\u0004W{c\u007fo\u0010\u0004Wa{3\u001a!L\\4y*`\u0017AK4z*~\u0001\u0004W{c\u007fo\u0010\u0004wuY\u001a!L\\4y*`\u0017AK4z*~\u0001\u0004W{c\u007fo\u0010\u0004wuY";
      int var4 = "!L\\4y*`\u0017AK4z*~\u0001\u0004[q70kUPQq7,l\u0018A\u0019`n/hUEJ4c7hUVXzp:-\u001bQTvr-~$!L\\4y*`\u0017AK4z*~\u0001\u0004Pyg3h\u0018AW`7\u001cb\u0018TXfv=a\u0010%!L\\4y*`\u0017AKg72x\u0006P\u0019}z/a\u0010I\\zc\u007fN\u001aIIue>o\u0019A\u001c!L\\4y*`\u0017AKg72x\u0006P\u0019zx+-\u0017A\u0019zb3a\u001b!L\\4y*`\u0017AK4z*~\u0001\u0004W{c\u007fo\u0010\u0004Wa{3\u001a!L\\4y*`\u0017AK4z*~\u0001\u0004W{c\u007fo\u0010\u0004wuY\u001a!L\\4y*`\u0017AK4z*~\u0001\u0004W{c\u007fo\u0010\u0004wuY".length();
      char var1 = '8';
      int var0 = -1;

      while(true) {
         int var7;
         int var8;
         byte var10;
         byte var12;
         char[] var10001;
         char[] var10002;
         int var10003;
         char[] var10004;
         int var10005;
         char var22;
         byte var23;
         label83: {
            ++var0;
            var10002 = var2.substring(var0, var0 + var1).toCharArray();
            var10003 = var10002.length;
            var7 = 0;
            var10 = 22;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 22;
               var10005 = var7;
            } else {
               var10 = 22;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 22;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 99;
                  break;
               case 1:
                  var23 = 50;
                  break;
               case 2:
                  var23 = 47;
                  break;
               case 3:
                  var23 = 2;
                  break;
               case 4:
                  var23 = 1;
                  break;
               case 5:
                  var23 = 73;
                  break;
               default:
                  var23 = 27;
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
            var2 = "'JZ2\u007f,f\u0011GMa14~\u0000V\u001fptyd\u0015\u0002Kztyx\u0012OZ2e {\u0016\u0006!CQut\u0002";
            var4 = "'JZ2\u007f,f\u0011GMa14~\u0000V\u001fptyd\u0015\u0002Kztyx\u0012OZ2e {\u0016\u0006!CQut\u0002".length();
            var1 = '$';
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 16;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 16;
                     var10005 = var7;
                  } else {
                     var10 = 16;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 16;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 99;
                        break;
                     case 1:
                        var23 = 50;
                        break;
                     case 2:
                        var23 = 47;
                        break;
                     case 3:
                        var23 = 2;
                        break;
                     case 4:
                        var23 = 1;
                        break;
                     case 5:
                        var23 = 73;
                        break;
                     default:
                        var23 = 27;
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
                  d = var5;
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
