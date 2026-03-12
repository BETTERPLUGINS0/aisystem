package me.frep.vulcan.spigot.check.impl.combat.velocity;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Velocity",
   type = 'C',
   complexType = "Ignored Vertical",
   description = "Vertical velocity modifications."
)
public class VelocityC extends AbstractCheck {
   public VelocityC(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] var1) {
      long var3 = (Long)var1[0];
      PacketEvent var2 = (PacketEvent)var1[1];
      long var5 = var3 ^ 60090584450998L;
      if (this.Vulcan_O(new Object[]{var2, var5})) {
      }

   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }
}
