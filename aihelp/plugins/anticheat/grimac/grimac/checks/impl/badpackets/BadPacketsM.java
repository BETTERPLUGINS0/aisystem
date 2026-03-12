package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.Combat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCombatEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDeathCombatEvent;

@CheckData(
   name = "BadPacketsM",
   description = "Tried to respawn while alive",
   experimental = true
)
public class BadPacketsM extends Check implements PacketCheck {
   private int exempt;

   public BadPacketsM(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && (new WrapperPlayClientClientStatus(event)).getAction() == WrapperPlayClientClientStatus.Action.PERFORM_RESPAWN) {
         if (this.exempt > 0) {
            --this.exempt;
            return;
         }

         if (!this.player.compensatedEntities.self.isDead && this.flagAndAlert() && this.shouldModifyPackets()) {
            event.setCancelled(true);
            this.player.onPacketCancel();
         }
      }

   }

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.CHANGE_GAME_STATE && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && (new WrapperPlayServerChangeGameState(event)).getReason() == WrapperPlayServerChangeGameState.Reason.WIN_GAME) {
         this.player.addRealTimeTaskNow(() -> {
            ++this.exempt;
         });
      }

      if (event.getPacketType() == PacketType.Play.Server.DEATH_COMBAT_EVENT && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && (new WrapperPlayServerDeathCombatEvent(event)).getPlayerId() == this.player.entityID) {
         this.player.addRealTimeTaskNow(() -> {
            ++this.exempt;
         });
      }

      if (event.getPacketType() == PacketType.Play.Server.COMBAT_EVENT && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
         WrapperPlayServerCombatEvent packet = new WrapperPlayServerCombatEvent(event);
         if (packet.getCombat() == Combat.ENTITY_DEAD && packet.getPlayerId() == this.player.entityID) {
            this.player.addRealTimeTaskNow(() -> {
               ++this.exempt;
            });
         }
      }

   }
}
