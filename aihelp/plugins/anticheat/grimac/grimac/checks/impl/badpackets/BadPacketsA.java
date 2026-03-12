package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;

@CheckData(
   name = "BadPacketsA",
   description = "Sent duplicate slot id"
)
public class BadPacketsA extends Check implements PacketCheck {
   private int lastSlot = -1;

   public BadPacketsA(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
         int slot = (new WrapperPlayClientHeldItemChange(event)).getSlot();
         if (slot == this.lastSlot && this.flagAndAlert("slot=" + slot) && this.shouldModifyPackets()) {
            event.setCancelled(true);
            this.player.onPacketCancel();
         }

         this.lastSlot = slot;
      }

   }
}
