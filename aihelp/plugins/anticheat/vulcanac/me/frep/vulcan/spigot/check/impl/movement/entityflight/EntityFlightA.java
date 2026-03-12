package me.frep.vulcan.spigot.check.impl.movement.entityflight;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Entity Flight",
   type = 'A',
   complexType = "Ascension",
   description = "Ascending while riding an entity."
)
public class EntityFlightA extends AbstractCheck {
   private static final String[] b;

   public EntityFlightA(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   private void lambda$handle$5() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$4() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$3() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$2() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$1() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private void lambda$handle$0() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[8];
      int var3 = 0;
      String var2 = "\u001a\b(a,~|\u001cl#j+od\u0018q\u000f\u001a\u000f&b\"wXa(\"c3z\\|\u0007a8.l,h8\u0007a8.l,h8\u0016\u001a\u0016(b%r`a\u0004(}4~Xa(\"c3z\\|\u0018\u001a\u001f,j+~q.\"gG(iv$\u0011gk\"wq \u0015z";
      int var4 = "\u001a\b(a,~|\u001cl#j+od\u0018q\u000f\u001a\u000f&b\"wXa(\"c3z\\|\u0007a8.l,h8\u0007a8.l,h8\u0016\u001a\u0016(b%r`a\u0004(}4~Xa(\"c3z\\|\u0018\u001a\u001f,j+~q.\"gG(iv$\u0011gk\"wq \u0015z".length();
      char var1 = 16;
      int var0 = -1;

      label55:
      while(true) {
         byte var10000 = 91;
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
                     var28 = 26;
                     break;
                  case 1:
                     var28 = 23;
                     break;
                  case 2:
                     var28 = 28;
                     break;
                  case 3:
                     var28 = 84;
                     break;
                  case 4:
                     var28 = 28;
                     break;
                  case 5:
                     var28 = 64;
                     break;
                  default:
                     var28 = 94;
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

               var2 = "agU\u0013a@\u001a_[H\u0015e]\u000fa\u007fS\u0006O\u0005#\u001aSY\u0018H\u0001'\u0007";
               var4 = "agU\u0013a@\u001a_[H\u0015e]\u000fa\u007fS\u0006O\u0005#\u001aSY\u0018H\u0001'\u0007".length();
               var1 = '\r';
               var0 = -1;
            }

            var10000 = 32;
            ++var0;
            var10001 = var2.substring(var0, var0 + var1);
            var10002 = 0;
         }
      }
   }
}
