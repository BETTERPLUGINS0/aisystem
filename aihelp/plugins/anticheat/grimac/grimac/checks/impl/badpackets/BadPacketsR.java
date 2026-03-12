package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckData(
   name = "BadPacketsR",
   decay = 0.25D,
   experimental = true
)
public class BadPacketsR extends Check implements PacketCheck {
   private int positions = 0;
   private long clock = 0L;
   private long lastTransTime;
   private int oldTransId = 0;

   public BadPacketsR(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (isTransaction(event.getPacketType()) && this.player.packetStateData.lastTransactionPacketWasValid) {
         long ms = (this.player.getPlayerClockAtLeast() - this.clock) / 1000000L;
         long diff = System.currentTimeMillis() - this.lastTransTime;
         if (diff > 2000L && ms > 2000L) {
            if (this.positions == 0 && this.clock != 0L && this.player.cameraEntity.isSelf() && !this.player.compensatedEntities.self.isDead) {
               this.flag("time=" + ms + "ms, lst=" + diff + "ms, positions=" + this.positions);
            } else {
               this.reward();
            }

            this.player.compensatedEntities.entitiesRemovedThisTick.clear();
            this.player.compensatedWorld.removeInvalidPistonLikeStuff(this.oldTransId);
            this.positions = 0;
            this.clock = this.player.getPlayerClockAtLeast();
            this.lastTransTime = System.currentTimeMillis();
            this.oldTransId = this.player.lastTransactionSent.get();
         }
      }

      if ((event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION) && !this.player.inVehicle()) {
         ++this.positions;
      } else if ((event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE || event.getPacketType() == PacketType.Play.Client.VEHICLE_MOVE) && this.player.inVehicle()) {
         ++this.positions;
      }

   }
}
