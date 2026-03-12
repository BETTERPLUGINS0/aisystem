package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;

@CheckData(
   name = "CrashB",
   description = "Sent creative mode inventory click packets while not in creative mode"
)
public class CrashB extends Check implements PacketCheck {
   public CrashB(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CREATIVE_INVENTORY_ACTION && this.player.gamemode != GameMode.CREATIVE) {
         event.setCancelled(true);
         this.player.onPacketCancel();
         this.flagAndAlert();
      }

   }
}
