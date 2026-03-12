package ac.grim.grimac.checks.impl.crash;

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
   name = "CrashG",
   description = "Sent negative sequence id"
)
public class CrashG extends BlockPlaceCheck {
   public CrashG(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.USE_ITEM && this.isSupportedVersion()) {
         WrapperPlayClientUseItem use = new WrapperPlayClientUseItem(event);
         if (use.getSequence() < 0) {
            this.flagAndAlert();
            event.setCancelled(true);
            this.player.onPacketCancel();
         }
      }

   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (blockBreak.sequence < 0 && this.isSupportedVersion()) {
         this.flagAndAlert();
         blockBreak.cancel();
      }

   }

   public void onBlockPlace(BlockPlace place) {
      if (place.sequence < 0 && this.isSupportedVersion()) {
         this.flagAndAlert();
         place.resync();
      }

   }

   private boolean isSupportedVersion() {
      return this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_19);
   }
}
