package me.frep.vulcan.spigot.check.impl.movement.jesus;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Jesus",
   type = 'A',
   complexType = "Ground",
   description = "Walking on water."
)
public class JesusA extends AbstractCheck {
   public JesusA(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
