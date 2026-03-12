package me.frep.vulcan.spigot.check.impl.player.ghosthand;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;

@CheckInfo(
   name = "Ghost Hand",
   type = 'A',
   complexType = "Bed",
   description = "Invalid bed break."
)
public class GhostHandA extends AbstractCheck {
   private static String[] Vulcan_D;
   private static final String[] d;

   public GhostHandA(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] var1) {
      long var2 = (Long)var1[0];
      PacketEvent var4 = (PacketEvent)var1[1];
   }

   public void Vulcan_i(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_x(String[] var0) {
      Vulcan_D = var0;
   }

   public static String[] Vulcan_c() {
      return Vulcan_D;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[7];
      int var3 = 0;
      String var2 = "\"A\u007ftE3{)F0\bfJa~E,a{\u0007\u0004mICi\u0004Y\bfJa~E,a{\u0003\u0004mI";
      int var4 = "\"A\u007ftE3{)F0\bfJa~E,a{\u0007\u0004mICi\u0004Y\bfJa~E,a{\u0003\u0004mI".length();
      String[] var10000 = new String[3];
      char var1 = '\n';
      Vulcan_x(var10000);
      int var0 = -1;

      label55:
      while(true) {
         byte var7 = 55;
         ++var0;
         String var10001 = var2.substring(var0, var0 + var1);
         byte var10002 = -1;

         while(true) {
            char[] var14;
            label50: {
               char[] var15 = var10001.toCharArray();
               int var10004 = var15.length;
               int var6 = 0;
               byte var18 = var7;
               byte var17 = var7;
               var14 = var15;
               int var11 = var10004;
               char[] var19;
               int var10006;
               if (var10004 <= 1) {
                  var19 = var15;
                  var10006 = var6;
               } else {
                  var17 = var7;
                  var11 = var10004;
                  if (var10004 <= var6) {
                     break label50;
                  }

                  var19 = var15;
                  var10006 = var6;
               }

               while(true) {
                  char var28 = var19[var10006];
                  byte var29;
                  switch(var6 % 7) {
                  case 0:
                     var29 = 113;
                     break;
                  case 1:
                     var29 = 31;
                     break;
                  case 2:
                     var29 = 58;
                     break;
                  case 3:
                     var29 = 38;
                     break;
                  case 4:
                     var29 = 17;
                     break;
                  case 5:
                     var29 = 112;
                     break;
                  default:
                     var29 = 37;
                  }

                  var19[var10006] = (char)(var28 ^ var18 ^ var29);
                  ++var6;
                  if (var17 == 0) {
                     var10006 = var17;
                     var19 = var14;
                     var18 = var17;
                  } else {
                     if (var11 <= var6) {
                        break;
                     }

                     var19 = var14;
                     var18 = var17;
                     var10006 = var6;
                  }
               }
            }

            String var21 = (new String(var14)).intern();
            switch(var10002) {
            case 0:
               var5[var3++] = var21;
               if ((var0 += var1) >= var4) {
                  d = var5;
                  return;
               }

               var1 = var2.charAt(var0);
               break;
            default:
               var5[var3++] = var21;
               if ((var0 += var1) < var4) {
                  var1 = var2.charAt(var0);
                  continue label55;
               }

               var2 = "A(\f\ng\u0004:1\u0000v>l\u0003u";
               var4 = "A(\f\ng\u0004:1\u0000v>l\u0003u".length();
               var1 = 3;
               var0 = -1;
            }

            var7 = 114;
            ++var0;
            var10001 = var2.substring(var0, var0 + var1);
            var10002 = 0;
         }
      }
   }
}
