package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Bad Packets",
   type = 'D',
   complexType = "Elytra",
   description = "."
)
public class BadPacketsD extends AbstractCheck {
   public BadPacketsD(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] var1) {
      long var3 = (Long)var1[0];
      PacketEvent var2 = (PacketEvent)var1[1];
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }
}
