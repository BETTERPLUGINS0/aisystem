package me.frep.vulcan.spigot.check.impl.movement.entityflight;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Entity Flight",
   type = 'B',
   complexType = "Hover",
   description = "Hovering while riding an entity."
)
public class EntityFlightB extends AbstractCheck {
   private static String Vulcan_P;
   private static final String[] b;

   public EntityFlightB(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   public static void Vulcan_S(String var0) {
      Vulcan_P = var0;
   }

   public static String Vulcan_y() {
      return Vulcan_P;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[10];
      int var3 = 0;
      Vulcan_S("fzv7Db");
      String var2 = ",\u0017\\7Gy\u0017W;V)@}\u0013J\u000eW>P&Qp/\u0005>G,[rw\u0007W+Z&_ow\r,\u000fZ\"i<.\u00123G$m!\u0007W+Z&_ow\u0016,\u0005\\(Vu/W\u0017\\7Gy\u0017W;V)@}\u0013J\u0018,\fX Xy>\u00181\u0013\r[n9\u0012\u0002\u0013!Qp>\u0016\u0006\u000e\u000f,\u001cR(Qp\u0017W;V)@}\u0013J";
      int var4 = ",\u0017\\7Gy\u0017W;V)@}\u0013J\u000eW>P&Qp/\u0005>G,[rw\u0007W+Z&_ow\r,\u000fZ\"i<.\u00123G$m!\u0007W+Z&_ow\u0016,\u0005\\(Vu/W\u0017\\7Gy\u0017W;V)@}\u0013J\u0018,\fX Xy>\u00181\u0013\r[n9\u0012\u0002\u0013!Qp>\u0016\u0006\u000e\u000f,\u001cR(Qp\u0017W;V)@}\u0013J".length();
      char var1 = 15;
      int var0 = -1;

      label55:
      while(true) {
         byte var10000 = 124;
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
                     var28 = 11;
                     break;
                  case 1:
                     var28 = 35;
                     break;
                  case 2:
                     var28 = 79;
                     break;
                  case 3:
                     var28 = 57;
                     break;
                  case 4:
                     var28 = 72;
                     break;
                  case 5:
                     var28 = 96;
                     break;
                  default:
                     var28 = 54;
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

               var2 = "8Q?I>\u001f@jQ(C4\u001d\u0018\rC`5M\u0006SA}\\(K\u0002N";
               var4 = "8Q?I>\u001f@jQ(C4\u001d\u0018\rC`5M\u0006SA}\\(K\u0002N".length();
               var1 = 14;
               var0 = -1;
            }

            var10000 = 19;
            ++var0;
            var10001 = var2.substring(var0, var0 + var1);
            var10002 = 0;
         }
      }
   }
}
