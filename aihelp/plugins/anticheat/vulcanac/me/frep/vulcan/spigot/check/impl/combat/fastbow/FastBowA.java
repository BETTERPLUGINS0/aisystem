package me.frep.vulcan.spigot.check.impl.combat.fastbow;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;

@CheckInfo(
   name = "Fast Bow",
   type = 'A',
   complexType = "Limit",
   description = "Shooting a bow too quickly"
)
public class FastBowA extends AbstractCheck {
   private long Vulcan_H;
   private static String Vulcan_h;
   private static final String[] b;

   public FastBowA(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] var1) {
      long var2 = (Long)var1[0];
      PacketEvent var4 = (PacketEvent)var1[1];
   }

   public void Vulcan_i(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_Z(String var0) {
      Vulcan_h = var0;
   }

   public static String Vulcan_i() {
      return Vulcan_h;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[6];
      int var3 = 0;
      String var2 = "tkO\u0001]\u0003uvW\u0007[\\n5z\u00050\u0007\u0017]e>o\u00140";
      int var4 = "tkO\u0001]\u0003uvW\u0007[\\n5z\u00050\u0007\u0017]e>o\u00140".length();
      char var1 = 5;
      Vulcan_Z("iCoa2");
      int var0 = -1;

      label55:
      while(true) {
         byte var10000 = 115;
         ++var0;
         String var10001 = var2.substring(var0, var0 + var1);
         byte var10002 = -1;

         while(true) {
            char[] var13;
            label50: {
               char[] var14 = var10001.toCharArray();
               int var10004 = var14.length;
               int var6 = 0;
               byte var17 = var10000;
               byte var16 = var10000;
               var13 = var14;
               int var10 = var10004;
               char[] var18;
               int var10006;
               if (var10004 <= 1) {
                  var18 = var14;
                  var10006 = var6;
               } else {
                  var16 = var10000;
                  var10 = var10004;
                  if (var10004 <= var6) {
                     break label50;
                  }

                  var18 = var14;
                  var10006 = var6;
               }

               while(true) {
                  char var27 = var18[var10006];
                  byte var28;
                  switch(var6 % 7) {
                  case 0:
                     var28 = 68;
                     break;
                  case 1:
                     var28 = 74;
                     break;
                  case 2:
                     var28 = 115;
                     break;
                  case 3:
                     var28 = 33;
                     break;
                  case 4:
                     var28 = 125;
                     break;
                  case 5:
                     var28 = 30;
                     break;
                  default:
                     var28 = 126;
                  }

                  var18[var10006] = (char)(var27 ^ var17 ^ var28);
                  ++var6;
                  if (var16 == 0) {
                     var10006 = var16;
                     var18 = var13;
                     var17 = var16;
                  } else {
                     if (var10 <= var6) {
                        break;
                     }

                     var18 = var13;
                     var17 = var16;
                     var10006 = var6;
                  }
               }
            }

            String var20 = (new String(var13)).intern();
            switch(var10002) {
            case 0:
               var5[var3++] = var20;
               if ((var0 += var1) >= var4) {
                  b = var5;
                  return;
               }

               var1 = var2.charAt(var0);
               break;
            default:
               var5[var3++] = var20;
               if ((var0 += var1) < var4) {
                  var1 = var2.charAt(var0);
                  continue label55;
               }

               var2 = "&9\u001dS\u000f\u0003'$\u0005";
               var4 = "&9\u001dS\u000f\u0003'$\u0005".length();
               var1 = 5;
               var0 = -1;
            }

            var10000 = 33;
            ++var0;
            var10001 = var2.substring(var0, var0 + var1);
            var10002 = 0;
         }
      }
   }
}
