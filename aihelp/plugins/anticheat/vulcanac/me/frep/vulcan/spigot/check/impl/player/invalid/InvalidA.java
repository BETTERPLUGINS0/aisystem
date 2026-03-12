package me.frep.vulcan.spigot.check.impl.player.invalid;

import java.lang.invoke.MethodHandles;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Invalid",
   type = 'A',
   complexType = "Invalid",
   description = "Possible transaction disabler. Do not ban for this check."
)
public class InvalidA extends AbstractCheck {
   private int Vulcan_e;
   private static final long b = Vulcan_n.a(7911282988365574013L, -4066092935003299649L, MethodHandles.lookup().lookupClass()).a(99166740519106L);

   public InvalidA(Vulcan_iE var1) {
      long var2 = b ^ 64811666146574L;
      super(var1);
      AbstractCheck[] var10000 = InvalidJ.Vulcan_M();
      this.Vulcan_e = 0;
      AbstractCheck[] var4 = var10000;
      if (var4 == null) {
         int var5 = AbstractCheck.Vulcan_V();
         ++var5;
         AbstractCheck.Vulcan_H(var5);
      }

   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
