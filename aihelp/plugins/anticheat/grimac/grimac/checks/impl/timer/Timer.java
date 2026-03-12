package ac.grim.grimac.checks.impl.timer;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

@CheckData(
   name = "Timer",
   configName = "TimerA",
   setback = 10.0D
)
public class Timer extends Check implements PacketCheck {
   protected long timerBalanceRealTime = 0L;
   protected long knownPlayerClockTime = (long)((double)System.nanoTime() - 6.0E10D);
   protected long lastMovementPlayerClock = (long)((double)System.nanoTime() - 6.0E10D);
   protected long clockDrift;
   protected boolean hasGottenMovementAfterTransaction = false;

   public Timer(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (this.hasGottenMovementAfterTransaction && this.checkForTransaction(event.getPacketType())) {
         this.knownPlayerClockTime = this.lastMovementPlayerClock;
         this.lastMovementPlayerClock = this.player.getPlayerClockAtLeast();
         this.hasGottenMovementAfterTransaction = false;
      }

      if (this.shouldCountPacketForTimer(event.getPacketType())) {
         this.hasGottenMovementAfterTransaction = true;
         this.timerBalanceRealTime = (long)((double)this.timerBalanceRealTime + 5.0E7D);
         this.doCheck(event);
      }
   }

   public void doCheck(PacketReceiveEvent event) {
      if (this.timerBalanceRealTime > System.nanoTime()) {
         if (this.flagAndAlert()) {
            if (this.shouldModifyPackets()) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }

            if (this.shouldSetback()) {
               this.player.getSetbackTeleportUtil().executeNonSimulatingSetback();
            }
         }

         this.timerBalanceRealTime = (long)((double)this.timerBalanceRealTime - 5.0E7D);
      }

      this.limitFallBehind();
   }

   protected void limitFallBehind() {
      this.timerBalanceRealTime = Math.max(this.timerBalanceRealTime, this.lastMovementPlayerClock - this.clockDrift);
   }

   public boolean checkForTransaction(PacketTypeCommon packetType) {
      return packetType == PacketType.Play.Client.PONG || packetType == PacketType.Play.Client.WINDOW_CONFIRMATION;
   }

   public boolean shouldCountPacketForTimer(PacketTypeCommon packetType) {
      return this.isTickPacket(packetType);
   }

   public void onReload(ConfigManager config) {
      this.clockDrift = (long)(config.getDoubleElse(this.getConfigName() + ".drift", 120.0D) * 1000000.0D);
   }
}
