package ac.grim.grimac.checks.impl.timer;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;

@CheckData(
   name = "TimerLimit",
   setback = 10.0D
)
public class TimerLimit extends Timer {
   private long limitAbuseOverPing;

   public TimerLimit(GrimPlayer player) {
      super(player);
   }

   public void doCheck(PacketReceiveEvent event) {
      if (this.timerBalanceRealTime > System.nanoTime()) {
         if (!event.isCancelled() && this.flagAndAlert() && this.shouldSetback()) {
            this.player.getSetbackTeleportUtil().executeNonSimulatingSetback();
         }

         this.timerBalanceRealTime = (long)((double)this.timerBalanceRealTime - 5.0E7D);
      }

      this.limitFallBehind();
   }

   protected void limitFallBehind() {
      long playerClock = this.lastMovementPlayerClock;
      if (this.limitAbuseOverPing != -1L && System.nanoTime() - playerClock > this.limitAbuseOverPing) {
         playerClock = System.nanoTime() - this.limitAbuseOverPing;
      }

      this.timerBalanceRealTime = Math.max(this.timerBalanceRealTime, playerClock - this.clockDrift);
   }

   public void onReload(ConfigManager config) {
      super.onReload(config);
      this.limitAbuseOverPing = config.getLongElse(this.getConfigName() + ".ping-abuse-limit-threshold", 1000L);
      if (this.limitAbuseOverPing != -1L) {
         this.limitAbuseOverPing *= 1000000L;
      }

   }
}
