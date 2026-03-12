package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;

@CheckData(
   name = "BadPacketsU",
   description = "Sent impossible use item packet"
)
public class BadPacketsU extends Check implements PacketCheck {
   public BadPacketsU(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
         WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(event);
         if (packet.getFace() == BlockFace.OTHER) {
            int expectedY = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) ? 4095 : 255;
            boolean failedItemCheck = packet.getItemStack().isPresent() && this.isEmpty((ItemStack)packet.getItemStack().get()) && this.player.getClientVersion().isOlderThan(ClientVersion.V_1_9);
            Vector3i pos = packet.getBlockPosition();
            Vector3f cursor = packet.getCursorPosition();
            if (failedItemCheck || pos.x != -1 || pos.y != expectedY || pos.z != -1 || cursor.x != 0.0F || cursor.y != 0.0F || cursor.z != 0.0F || packet.getSequence() != 0) {
               String verbose = String.format("xyz=%s, %s, %s, cursor=%s, %s, %s, item=%s, sequence=%s", pos.x, pos.y, pos.z, cursor.x, cursor.y, cursor.z, !failedItemCheck, packet.getSequence());
               if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                  this.player.onPacketCancel();
                  event.setCancelled(true);
               }
            }
         }
      }

   }

   private boolean isEmpty(ItemStack itemStack) {
      return itemStack.getType() == null || itemStack.getType() == ItemTypes.AIR;
   }
}
