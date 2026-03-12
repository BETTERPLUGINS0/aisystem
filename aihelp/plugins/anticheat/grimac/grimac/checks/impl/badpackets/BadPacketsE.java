package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(
   name = "BadPacketsE"
)
public class BadPacketsE extends Check implements PacketCheck {
   private int noReminderTicks;
   private final int maxNoReminderTicks;
   private final boolean isViaPleaseStopUsingProtocolHacksOnYourServer;

   public BadPacketsE(GrimPlayer player) {
      super(player);
      this.maxNoReminderTicks = this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8) ? 20 : 19;
      this.isViaPleaseStopUsingProtocolHacksOnYourServer = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) || PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() != PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION && event.getPacketType() != PacketType.Play.Client.PLAYER_POSITION) {
         if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) && !this.player.packetStateData.lastPacketWasTeleport) {
            if (++this.noReminderTicks > this.maxNoReminderTicks) {
               this.flagAndAlert("ticks=" + this.noReminderTicks);
            }
         } else if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE || this.isViaPleaseStopUsingProtocolHacksOnYourServer && this.player.inVehicle()) {
            this.noReminderTicks = 0;
         }
      } else {
         this.noReminderTicks = 0;
      }

   }

   public void handleRespawn() {
      this.noReminderTicks = 0;
   }
}
