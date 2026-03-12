package ac.grim.grimac.events.packets;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;

public class PacketPlayerAbilities extends Check implements PacketCheck {
   private boolean lastSentPlayerCanFly = false;
   private int maxFlyingPing = 1000;

   public PacketPlayerAbilities(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.PLAYER_ABILITIES) {
         WrapperPlayClientPlayerAbilities abilities = new WrapperPlayClientPlayerAbilities(event);
         this.player.isFlying = abilities.isFlying() && this.player.canFly;
      }

   }

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.PLAYER_ABILITIES) {
         WrapperPlayServerPlayerAbilities abilities = new WrapperPlayServerPlayerAbilities(event);
         this.player.sendTransaction();
         if (this.lastSentPlayerCanFly && !abilities.isFlightAllowed()) {
            int noFlying = this.player.lastTransactionSent.get();
            if (this.maxFlyingPing != -1) {
               this.player.runNettyTaskInMs(() -> {
                  if (this.player.lastTransactionReceived.get() < noFlying) {
                     this.player.getSetbackTeleportUtil().executeViolationSetback();
                  }

               }, this.maxFlyingPing);
            }
         }

         this.lastSentPlayerCanFly = abilities.isFlightAllowed();
         this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
            this.player.canFly = abilities.isFlightAllowed();
            this.player.isFlying = abilities.isFlying();
         });
      }

   }

   public void onReload(ConfigManager config) {
      this.maxFlyingPing = config.getIntElse("max-ping-out-of-flying", 1000);
   }
}
