package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;

@CheckData(
   name = "BadPacketsH",
   description = "Sent unexpected sequence id",
   experimental = true
)
public class BadPacketsH extends BlockPlaceCheck {
   private int lastSequence;
   private final boolean isSupportedVersion;

   public BadPacketsH(GrimPlayer player) {
      super(player);
      this.isSupportedVersion = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_19);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.USE_ITEM && this.shouldCancel((new WrapperPlayClientUseItem(event)).getSequence())) {
         event.setCancelled(true);
         this.player.onPacketCancel();
      }

   }

   public void onBlockPlace(BlockPlace place) {
      if (this.shouldCancel(place.sequence) && this.shouldCancel()) {
         place.resync();
      }

   }

   public void onBlockBreak(BlockBreak blockBreak) {
      switch(blockBreak.action) {
      case START_DIGGING:
      case FINISHED_DIGGING:
         if (this.shouldCancel(blockBreak.sequence)) {
            blockBreak.cancel();
         }
         break;
      case CANCELLED_DIGGING:
         if (blockBreak.sequence != 0 && this.flagAndAlert("expected=0, id=" + blockBreak.sequence) && this.shouldModifyPackets()) {
            blockBreak.cancel();
         }
      }

   }

   public boolean shouldCancel(int sequence) {
      int expected = this.lastSequence + 1;
      this.lastSequence = sequence;
      return this.isSupportedVersion && sequence != expected && this.flagAndAlert("expected=" + expected + ", id=" + sequence) && this.shouldModifyPackets();
   }

   public void onWorldChange() {
      this.lastSequence = 0;
   }
}
